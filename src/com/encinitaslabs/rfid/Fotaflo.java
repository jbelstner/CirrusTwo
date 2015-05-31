/*
 * Copyright (c) 2013 - 2015, Encinitas Laboratories, Inc. 
 * All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Encinitas Laboratories, Incorporated and its
 * suppliers if any.  The intellectual and technical concepts contained
 * herein are proprietary to Encinitas Laboratories, Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Encinitas Laboratories, Incorporated.
 *
 * Please contact:
 * Encinitas Laboratories, Inc.
 * 1310 Ravean Court
 * Encinitas, CA 92024 USA
 * http://www.encinitaslabs.com
 * for additional information or to ask a question.
 */
package com.encinitaslabs.rfid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;


/**
 * Fotaflo Object
 * <P>Attributes and functionality corresponding to Fotaflo customer.
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class Fotaflo {
	
	private String username = null;
	private String password = null;
	private String photoUrl = null;
	private String deviceId = null;
	private String location = null;
	private Log logObject = null;

	/** 
	 * Fotaflo<P>
	 * Class Constructor
	 */
	public Fotaflo ( String deviceId_, String location_ ) {
		if (deviceId_ != null) {
			deviceId = deviceId_;
		} else {
			deviceId = "New Camera";
		}
		if (location_ != null) {
			location = location_;
		} else {
			location = "MINSK-Testwww";
		}
	}
	
	/** 
	 * setCredentials<P>
	 * This method sets the username and password credentials for
	 * the Fotaflo picture server.
	 * @param username_ The picture server username
	 * @param password_ The picture server password
	 */
	public void setCredentials( String username_, String password_ ) {
		if (username_ != null) {
			username = username_;
		} else {
			username = "Encinitas";
		}
		if (password_ != null) {
			password = password_;
		} else {
			password = "Carlsbad";
		}
	}

	/** 
	 * setUploadPath<P>
	 * This method sets the URL of the Fotaflo picture server.
	 * @param username_ The picture server username
	 */
	public void setUploadUrl( String url_ ) {
		if (url_ != null) {
			photoUrl = url_;
		} else {
			photoUrl = "http://199.58.116.35:8081/fotaflo-test/pictures/upload";
		}
	}

	/**
     * postImageToServer<P>
     * THis method posts photos to the Fotaflo picture server.
     * @param filename The filename of the image
     * @param tags The serialized string of tags associated with that image
     */
	public Boolean postImageToServer( String filename, String tags ) throws Exception {
		Boolean success = true;
        String credentials = username + ':' + password;
        Base64 encoder = new Base64();
        byte[] credArray = credentials.getBytes();
        String encoding = encoder.encodeToString(credArray);

        URL url = new URL(photoUrl);

        HttpURLConnection uc = (HttpURLConnection)url.openConnection();
        uc.setRequestProperty("Authorization", "Basic " + encoding);
        uc.setRequestProperty("location", location);
        uc.setRequestProperty("filename", filename);
        uc.setRequestProperty("FileDate", new Date().getTime()+"");
        uc.setRequestProperty("deviceId", deviceId);
        uc.setRequestProperty("tags", tags);
        uc.setRequestMethod("POST");

        uc.setDoInput(true);
        uc.setDoOutput(true);

        OutputStream content = null;
        InputStream source = null;

        try {
        	File file = new File(filename);
            if (file.exists()) {
    			log( "Uploading " + filename, Log.Level.Information );
                source = new FileInputStream(file);
                content = uc.getOutputStream();

                IOUtils.copy(source, content);
                content.flush();
                content.close();

                // Get Response
                InputStream is = uc.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
    			log( "Server Response " + response.toString(), Log.Level.Information );
                uc.disconnect();
            } else {
    			log( filename + " does not exist!", Log.Level.Warning );
    			success = false;
            }
        } finally {
            if (content != null){
                content.close();
            }
            if (source != null){
                source.close();
            }
        }
        return success;
	}

	/** 
	 * setLogObject<P>
	 * This method is used for making log entries.
	 */
	public void setLogObject(Log logObject_) {
		logObject = logObject_;
	}
	
	/** 
	 * log<P>
	 * This method is used for making log entries.
	 */
	private void log(String entry, Log.Level logLevel) {
		if (logObject != null) {
			logObject.makeEntry(entry, logLevel);
		} else {
			System.out.println(entry);
		}
	}
}
