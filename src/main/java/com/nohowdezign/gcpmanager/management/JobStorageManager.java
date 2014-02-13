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
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobStorageManager {
    private static List<File> jobFilesDownloaded = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");
    public static int timeToKeepFileInDays = 3; //Set the default to 3 days

    public void checkLifetimeOfFiles() {
        if(!jobFilesDownloaded.isEmpty()) {
            for(File jobFileDownloaded : jobFilesDownloaded) {
                String[] splitDaysAndHours = getCurrentTime().split(" ");
                String[] splitDays = splitDaysAndHours[0].split("-");
                String timeToCheckWith = splitDays[0] + String.valueOf(Integer.valueOf(splitDays[1]) - timeToKeepFileInDays) + splitDays[2] + " ......";

                if(sdf.format(jobFileDownloaded.lastModified()).matches(timeToCheckWith)) {
                    jobFileDownloaded.delete();
                    System.out.println("Just deleted a file.");
                }
            }
        }
    }

    public void addJobFileDownloaded(File fileDownloaded) {
        jobFilesDownloaded.add(fileDownloaded);
    }

    private String getCurrentTime() {
        Date now = new Date();

        return sdf.format(now.getTime());
    }

}
