package com.nohowdezign.gcpmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;

import com.nohowdezign.gcpmanager.management.JobLimiter;
import com.nohowdezign.gcpmanager.management.JobStorageManager;
import com.nohowdezign.gcpmanager.management.JobStorageManagerThread;
import com.nohowdezign.gcpmanager.printers.PrinterManager;
import com.nohowdezign.gcpmanager.website.auth.AuthenticationManager;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import th.co.geniustree.google.cloudprint.api.GoogleCloudPrint;
import th.co.geniustree.google.cloudprint.api.exception.CloudPrintAuthenticationException;
import th.co.geniustree.google.cloudprint.api.exception.CloudPrintException;
import th.co.geniustree.google.cloudprint.api.model.Job;
import th.co.geniustree.google.cloudprint.api.model.JobStatus;
import th.co.geniustree.google.cloudprint.api.model.response.FecthJobResponse;

/**
 * Copyright 2013 nh_99 Development
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author nh_99
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final GoogleCloudPrint cloudPrint = new GoogleCloudPrint();
    private static Gson gson = new Gson();
    private static int amountOfPagesPerPrintJob;
    private static int[] timeRestraintsForPrinter;
	
	public final static void main(String[] args) {
		//Remove old log, it does not need to be there anymore.
		File oldLog = new File("./CloudPrintManager.log");
        
        if(oldLog.exists()) {
        	oldLog.delete();
        }
        
        //Create a file reader for the props file
		Reader propsStream = null;
		try {
			propsStream = new FileReader("./props.json");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		PrintManagerProperties props = null;
		if(propsStream != null) {
			props = gson.fromJson(propsStream, PrintManagerProperties.class);
		} else {
			logger.error("Property file does not exist. Please create one.");
		}
		
		//Set the variables to what is in the props file
        String email = props.getEmail();
        String password = props.getPassword();
        String printerId = props.getPrinterId();
        amountOfPagesPerPrintJob = props.getAmountOfPagesPerPrintJob();
        timeRestraintsForPrinter = props.getTimeRestraintsForPrinter();

        JobStorageManager.timeToKeepFileInDays = props.getTimeToKeepFileInDays();

        //AuthenticationManager authenticationManager = new AuthenticationManager();
        //authenticationManager.setPasswordToUse(props.getAdministrativePassword());
        //authenticationManager.initialize(1337); //Start the authentication manager on port 1337

        try {
			cloudPrint.connect(email, password, "cloudprintmanager-1.0");
		} catch (CloudPrintAuthenticationException e) {
			logger.error(e.getMessage());
		}
        
        //TODO: Get a working website ready
        //Thread adminConsole = new Thread(new HttpServer(80), "HttpServer");
        //adminConsole.start();

        Thread printJobManager = new Thread(new JobStorageManagerThread(), "JobStorageManager");
        printJobManager.start();

        try {
            if(System.getProperty("os.name").toLowerCase().startsWith("win")) {
                logger.error("Your operating system is not supported. Please switch to linux ya noob.");
                System.exit(1);
            } else {
                PrinterManager printerManager = new PrinterManager(cloudPrint);

                File cupsPrinterDir = new File("/etc/cups/ppd");

                if(cupsPrinterDir.isDirectory() && cupsPrinterDir.canRead()) {
                    for(File cupsPrinterPPD : cupsPrinterDir.listFiles()) {
                        //Init all of the CUPS printers in the manager
                        printerManager.initializePrinter(cupsPrinterPPD, cupsPrinterPPD.getName());
                    }
                } else {
                    logger.error("Please run this with a higher access level.");
                    System.exit(1);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        while(true) {
        	try {
    			getPrintingJobs(printerId);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
	}
	
	/**
	 * Method to fetch all print jobs on a printer.
	 * @param printerId is the ID of the printer you want to scan
	 */
	public static void getPrintingJobs(String printerId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        FecthJobResponse response = cloudPrint.fetchJob(printerId);
        
        if (!response.isSuccess()) {
            return;
        }

        for (Job job : response.getJobs()) {
            if(job.getStatus().equals(JobStatus.QUEUED)) {
                JobLimiter limiter = new JobLimiter(cloudPrint);

                limiter.checkJob(job, amountOfPagesPerPrintJob, timeRestraintsForPrinter);
            } else if(job.getStatus().equals(JobStatus.IN_PROGRESS)) {
                logger.info("Job created by {} at {}.", job.getOwnerId(), sdf.format(Long.parseLong(job.getCreateTime())));
                dowloadFile(job);
            }
        }
    }

    public static void dowloadFile(Job job) throws CloudPrintException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");

        File directory = new File("./jobstorage/" + sdf.format(Long.parseLong(job.getCreateTime())) + "/" + job.getOwnerId() + "/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = FilenameUtils.removeExtension(job.getTitle()) + ".pdf";
        File outputFile = new File(directory, fileName);

        if(!outputFile.exists()) {
            cloudPrint.downloadFile(job.getFileUrl(), outputFile);
            logger.info("mod date: {}", sdf.format(outputFile.lastModified()));
            JobStorageManager manager = new JobStorageManager();
            manager.addJobFileDownloaded(outputFile);
        }
    }
	
}