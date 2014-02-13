package com.nohowdezign.gcpmanager.website.auth;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class AuthenticationManager {
    private String passwordToUse;

    public void initialize(int port) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            Socket connectionSocket;
            try {
                connectionSocket = serverSocket.accept();

                InetAddress clientIP = connectionSocket.getInetAddress();

                BufferedReader clientInput = new BufferedReader
                        (new InputStreamReader(connectionSocket.getInputStream()));

                DataOutputStream clientOutput = new DataOutputStream(connectionSocket.getOutputStream());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkClientInputForPassword(BufferedReader clientInput) throws Exception {
        if(clientInput.readLine().startsWith("password:")) {
            String[] passwordJsonSplit = clientInput.readLine().split(":");

            if(passwordJsonSplit[1].equals(passwordToUse)) {
            }
        }
    }

    public String getPasswordToUse() {
        return passwordToUse;
    }

    public void setPasswordToUse(String passwordToUse) {
        this.passwordToUse = passwordToUse;
    }
}
