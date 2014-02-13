package com.nohowdezign.gcpmanager.management;
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 NoHowDezign
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author nh_99
 *
 * This class limits different aspects of the job, such as page amounts.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import th.co.geniustree.google.cloudprint.api.GoogleCloudPrint;
import th.co.geniustree.google.cloudprint.api.model.Job;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobLimiter {
    private static final Logger logger = LoggerFactory.getLogger(JobLimiter.class);
    private GoogleCloudPrint cloudPrint;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");

    public JobLimiter(GoogleCloudPrint cloudPrint) {
        this.cloudPrint = cloudPrint;
    }

    public void checkJob(Job job, int pageLengthLimit, int[] timeRestraint) throws Exception {
        handleJobLimits(job, pageLengthLimit, timeRestraint);
    }

    private void handleJobLimits(Job job, int pageLengthLimit, int[] timeRestraint) throws Exception {
        if(!isDocumentWithinPageLimit(job, pageLengthLimit)) {
            logger.info(job.getOwnerId() + " is trying to print a document that is longer than the allowed length.");
            cloudPrint.deleteJob(job.getId());
        }

        if(!canJobBePrintedAtThisTime(timeRestraint)) {
            logger.info(job.getOwnerId() + " is trying to print outside of the allowed time restraint.");
            cloudPrint.deleteJob(job.getId());
        }
    }

    private boolean isDocumentWithinPageLimit(Job job, int pageLenghtLimit) {
        if(job.getNumberOfPages() < pageLenghtLimit) {
            return true;
        } else {
            return false;
        }
    }

    //The time restraint is an integer array because it will have a beginning time that people
    //cannot print at and an end time
    private boolean canJobBePrintedAtThisTime(int[] timeRestraint) {
        String[] currentSystemHour = getCurrentTime().split(":");

        logger.info("Current time: " + currentSystemHour[0]);

        if(Integer.valueOf(currentSystemHour[0]) > timeRestraint[0] || Integer.valueOf(currentSystemHour[0]) < timeRestraint[1]) {
            return false;
        }

        return true;
    }

    private String getCurrentTime() {
        Date now = new Date();

        return sdf.format(now.getTime());
    }

}
