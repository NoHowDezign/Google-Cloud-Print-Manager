package com.nohowdezign.gcpmanager.properties;/*
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

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class PropertiesManager {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesManager.class);
    private static Gson gson = new Gson();
    private PrintManagerProperties props;

    public void loadProperties() {
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
    }

    public PrintManagerProperties getProperties() {
        return props;
    }
}
