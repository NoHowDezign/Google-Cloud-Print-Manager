package com.nohowdezign.gcpmanager.properties;

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
 * 
 * The interface for the JSON file that contains properties.
 */
public class PrintManagerProperties {
	private String email = "";
	private String password = "";
	private String printerId = "";
    private String administrativePassword = "";
    private int timeToKeepFileInDays = 3;
    private int amountOfPagesPerPrintJob = 20;
    private int[] timeRestraintsForPrinter;
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getPrinterId() {
		return printerId;
	}

    public int getTimeToKeepFileInDays() {
        return timeToKeepFileInDays;
    }

    public String getAdministrativePassword() {
        return administrativePassword;
    }

    public int getAmountOfPagesPerPrintJob() {
        return amountOfPagesPerPrintJob;
    }

    public int[] getTimeRestraintsForPrinter() {
        return timeRestraintsForPrinter;
    }
}
