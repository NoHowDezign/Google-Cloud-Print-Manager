package com.nohowdezign.gcpmanager.printers;
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

import org.apache.commons.codec.digest.DigestUtils;
import th.co.geniustree.google.cloudprint.api.GoogleCloudPrint;
import th.co.geniustree.google.cloudprint.api.model.Printer;
import th.co.geniustree.google.cloudprint.api.model.response.RegisterPrinterResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrinterManager {
    private GoogleCloudPrint cloudPrint;

    public PrinterManager(GoogleCloudPrint cloudPrint) {
        this.cloudPrint = cloudPrint;
    }

    public void initializePrinter(File printerDefenitionFile, String nameOfPrinterToCheck) throws Exception {
        if(doesPrinterExist(nameOfPrinterToCheck)) {
            registerPrinter(printerDefenitionFile, nameOfPrinterToCheck);
        }
    }

    /**
     * Check if the printer exists
     * @param cloudPrinterToCheck is the name of the printer you want to check.
     */
    private boolean doesPrinterExist(String cloudPrinterToCheck) throws Exception {
        for(Printer printer : cloudPrint.searchPrinters().getPrinters()) {
            if(printer.getName().equals(cloudPrinterToCheck)) {
                return true;
            }
        }

        return false;
    }

    private void registerPrinter(File capabilitiesFile, String printerName) throws Exception {
        InputStream inputStream = null;
        inputStream = new FileInputStream(capabilitiesFile);

        RegisterPrinterResponse response = cloudPrint.registerPrinter(
                createPrinterWithCapabilities(inputStream, capabilitiesFile));
        if (!response.isSuccess()) {
            return;
        }
    }

    private Printer createPrinterWithCapabilities(InputStream inputStream,
                                                  File capabilitiesFile) throws Exception {
        Printer printer = new Printer();
        printer.setProxy("pamarin");
        Set<String> tags = new HashSet<String>();
        tags.add("register");
        printer.setTags(tags);
        String capsHash = DigestUtils.sha512Hex(inputStream);
        printer.setCapsHash(capsHash);
        printer.setStatus("REGISTER");
        printer.setCapabilities(capabilitiesFile);
        printer.setDefaults(capabilitiesFile);

        return printer;
    }

}
