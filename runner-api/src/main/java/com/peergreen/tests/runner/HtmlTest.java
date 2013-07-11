/**
 * Copyright 2013 Peergreen S.A.S.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.tests.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

/**
 * Utility methods to check content
 * @author Florent Benoit
 */
public class HtmlTest {

    public String getContent(String url) throws IOException {
        return getContent(url, null, null);
    }

    public String getContent(String url, String login, String password) throws IOException {
        return getContent(new URL(url));
    }

    public String getContent(URL url) throws IOException {
        return getContent(url, null, null);
    }


    public String getContent(URL url, String login, String password) throws IOException {
        StringBuilder sb = new StringBuilder();

        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("GET");
        httpConnection.setRequestProperty("Accept", "text/html");
        if (login != null && password != null) {
            String userpass = login + ":" + password;
            String encodedAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            httpConnection.setRequestProperty ("Authorization", encodedAuth);
        }


        try (InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream()); BufferedReader bufferedReader  = new BufferedReader(isr)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
        }
        return sb.toString();
    }


    protected void waitForStatus200(URL url, long timeout, String username, String password) {
        String userpass = username + ":" + password;
        String encodedAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
        waitForStatus200(url, timeout, encodedAuth);
    }

    protected void waitForStatus200(URL url, long timeout) {
        waitForStatus200(url, timeout, null);
    }

    protected void waitForStatus200(URL url, long timeout, String encodedPassword) {
        long now = System.currentTimeMillis();
        long end = now + timeout;

        int responseCode = 0;
        while (responseCode != 200 && System.currentTimeMillis() < end) {
            HttpURLConnection httpConnection;
            try {
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setRequestProperty("Accept", "text/html");
                if (encodedPassword != null) {
                    httpConnection.setRequestProperty ("Authorization", encodedPassword);
                }

                responseCode = httpConnection.getResponseCode();
            } catch (IOException e) {
                // wait until the timeout expired
            }
            if (responseCode == 200) {
                return;
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Assert.fail("Unable to wait");
            }
        }
        Assert.fail("Unable to reach HTTP status 200 for URL '" + url + "'.");

    }






}
