package com.nohowdezign.gcpmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import th.co.geniustree.google.cloudprint.api.GoogleCloudPrint;
import th.co.geniustree.google.cloudprint.api.exception.CloudPrintAuthenticationException;
import th.co.geniustree.google.cloudprint.api.exception.CloudPrintException;
import th.co.geniustree.google.cloudprint.api.model.Job;
import th.co.geniustree.google.cloudprint.api.model.response.FecthJobResponse;

/**
 * Tech team printer: 3033258a-b8bd-109f-f5cc-5e68e4c0bc56
 * @author Noah Howard
 *
 */
public class Test {
	private static final Logger LOG = LoggerFactory.getLogger(Test.class);
    private static final GoogleCloudPrint cloudPrint = new GoogleCloudPrint();
    private static Gson gson = new Gson();
	
	public final static void main(String[] args) {
        String email = "studenttechteam@rsu20.org";
        String password = "BAHST3chTeam";

        try {
			cloudPrint.connect(email, password, "rsu20-printmanager-1.0");
		} catch (CloudPrintAuthenticationException e) {
			e.printStackTrace();
		}
        
        while(true) {
        	try {
    			fetchJob("a6fdd9c5-201c-0f0f-46df-6f848b970369");
    		} catch (CloudPrintException e) {
    			e.printStackTrace();
    		}
        }
	}
	
	public static void fetchJob(String printerId) throws CloudPrintException {
        FecthJobResponse response = cloudPrint.fetchJob(printerId);
        if (!response.isSuccess()) {
            LOG.debug("message = > {}", response.getMessage());
            return;
        }

        for (Job job : response.getJobs()) {
            LOG.debug("job response => {}", job);
        }
    }
	
}
