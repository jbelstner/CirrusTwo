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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Camera Object
 * <P>Attributes and functionality corresponding to a gphoto2 camera.
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class Camera {
	
	private final String make_s = "Manufacturer: ";
	private final String model_s = "Model: ";
	private final String version_s = "  Version: ";
	private final String serialNumber_s = "  Serial Number: ";
	private final String defaultImageName = "capt0000.jpg";
	private LinkedBlockingQueue<String> pictureQueue = null;
    private AtomicBoolean waiting = null;
	private String make = null;
	private String model = null;
	private String version = null;
	private String serialNumber = null;
	private Log logObject = null;
	
	/** 
	 * Camera<P>
	 * Class Constructor
	 */
	public Camera ( LinkedBlockingQueue<String> pictureQueue_, Log logObject_ ) {
		pictureQueue = pictureQueue_;
		logObject = logObject_;		
		waiting = new AtomicBoolean(false);
	}
	
	/** 
	 * enablePower<P>
	 * This method returns if we successfully enabled the camera power.
	 * The output is active low.
	 * @return A Boolean.
	 */
	public Boolean enablePower(Boolean enable) {
		try {
			if (enable) {
				Runtime.getRuntime().exec("./camera_power.sh 0");
			} else {
				Runtime.getRuntime().exec("./camera_power.sh 1");				
			}
			return true;
		} catch (IOException e) {
			log( "Unable to enable camera power!", Log.Level.Error );
			return false;
		}
	}

	/** 
	 * isReady<P>
	 * This method returns if the camera is ready.
	 * @return A Boolean.
	 */
	public Boolean isReady() {
		return !waiting.get();
	}

	/** 
	 * summary<P>
	 * This method executes a gphoto2 command.
	 * @throws IOException 
	 */
	public void summary() throws IOException {
		// Send the gphoto2 command
		Process proc = Runtime.getRuntime().exec("gphoto2 --summary");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = "";
		while ((line = stdInput.readLine()) != null) {
			if (line != null) {
				// Look for the Camera Manufacturer:
				if (line.startsWith(make_s)) {
					make = line.substring(make_s.length(), line.length()).trim();
				}
				// Look for the Camera Model:
				else if (line.startsWith(model_s)) {
					model = line.substring(model_s.length(), line.length()).trim();
				}
				// Look for the Camera SW Version:
				else if (line.startsWith(version_s)) {
					version = line.substring(version_s.length(), line.length()).trim();
				}
				// Look for the Camera Serial Number:
				else if (line.startsWith(serialNumber_s)) {
					serialNumber = line.substring(serialNumber_s.length(), line.length()).trim();
				}
			}
		}
	}

	/** 
	 * getManufacturer<P>
	 * This method returns the camera manufacturer.
	 * A value of null indicates an uninitialized value.
	 * @return A String.
	 */
	public String getManufacturer() {
		return make;
	}

	/** 
	 * getModel<P>
	 * This method returns the camera model.
	 * A value of null indicates an uninitialized value.
	 * @return A String.
	 */
	public String getModel() {
		return model;
	}

	/** 
	 * getVersion<P>
	 * This method returns the camera SW version.
	 * A value of null indicates an uninitialized value.
	 * @return A String.
	 */
	public String getVersion() {
		return version;
	}

	/** 
	 * getSerialNumber<P>
	 * This method returns the camera Serial Number.
	 * A value of null indicates an uninitialized value.
	 * @return A String.
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/** 
	 * captureImageAndDownload<P>
	 * This method executes a gphoto2 command.
	 * @throws IOException
	 */
	public Boolean captureImageAndDownload( String tagInfo, Integer shotsPerTrigger ) {
		final String subEpc = tagInfo;
		boolean success = true;
		// Check if we can do this
		if (waiting.compareAndSet(false, true)) {
			try {
				// Send the gphoto2 command
				Runtime.getRuntime().exec("gphoto2 --capture-image-and-download");
				// Wait in this thread for the file to download
				Thread waitForDownload = new Thread () {
					public void run() {
				        try {
						    File file = new File(defaultImageName);
						    int loops = 0;
						    while (!file.exists()) {
					    		loops++;
					    		if (loops == 0x3FFFF) { break; } // ~ 1 minute
						    }
						    // Do only if the file exists
						    if (file.exists()) {
							    Long timeStamp = new Date().getTime();
							    // Rename the file
							    String newName = subEpc + "-" + timeStamp + ".jpg";
								Runtime.getRuntime().exec("mv " + defaultImageName + " " + newName);
								Thread.sleep(250);
								// Pass to the main CirrusII application for upload
								pictureQueue.put(newName);
						    } else {
				    			log( "Timeout waiting for file download!", Log.Level.Warning );
				    			// Cycle power on the camera
			    				enablePower(false);
								Thread.sleep(1000);
			    				enablePower(true);
								Thread.sleep(2000);
						    }
				        } catch(Exception e){
			    			log( "Error waiting for file!\n" + e.toString(), Log.Level.Warning );
				        }
						// Ready to take a new picture
						waiting.set(false);
					}
				};
				waitForDownload.start();
				
			} catch (IOException e) {
				log( "Unable to take a picture!\n" + e.toString(), Log.Level.Error );
				waiting.set(false);
				success = false;
			}
		} else {
			success = false;
		}
		return success;
	}

	/** 
	 * captureImage<P>
	 * This method executes a gphoto2 command.
	 */
	public void captureImage() {
		// Check if we can do this
		if (waiting.get() == false) {
			try {
				// Send the gphoto2 command
				Runtime.getRuntime().exec("gphoto2 --capture-image");
			} catch (IOException e) {
				log( "Unable to take a picture!\n" + e.toString(), Log.Level.Error );
			}
		}
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
