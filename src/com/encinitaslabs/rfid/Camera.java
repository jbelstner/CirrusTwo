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
	private final String defaultName = "capt000";
	private LinkedBlockingQueue<String> pictureQueue = null;
    private AtomicBoolean waiting = null;
	private String make = null;
	private String model = null;
	private String version = null;
	private String serialNumber = null;
	private Log logObject = null;
	private ImageFormat imageFormat = ImageFormat.SmallNormal;
	private String shotsPerTrigger = "1";
	
	public enum ImageFormat {
		LargeFine((String)"0"),
		LargeNormal((String)"1"),
		MediumFine((String)"2"),
		MediumNormal((String)"3"),
		SmallFine((String)"4"),
		SmallNormal((String)"5"),
		Small((String)"6"),
		Tiny((String)"7"),
		RawLarge((String)"8"),
		Raw((String)"9");
		
		private String bImageFormat;
		
		ImageFormat(String bImageFormat_) {
			this.bImageFormat = bImageFormat_;
		}

		public String getValue() {
			return bImageFormat;
		}
	}

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
	 * captureImage<P>
	 * This method executes a gphoto2 command.
	 */
	public Boolean captureImage() {
		boolean success = true;
		// Check if we can do this
		if (waiting.get() == false) {
			try {
				// Send the gphoto2 command
				Runtime.getRuntime().exec("gphoto2 --capture-image");
			} catch (IOException e) {
				log( "Unable to take a picture!\n" + e.toString(), Log.Level.Error );
				success = false;
			}
		} else {
			success = false;
		}
		return success;
	}

	/** 
	 * captureImageAndDownload<P>
	 * This method executes a gphoto2 command.
	 * @throws IOException
	 */
	public Boolean captureImageAndDownload( String tagInfo ) {
		final String subEpc = tagInfo;
		boolean success = true;
		// Check if we can do this
		if (waiting.compareAndSet(false, true)) {
			try {
				// Send the gphoto2 command
				Runtime.getRuntime().exec("gphoto2 --capture-image-and-download -F=" + shotsPerTrigger + " -I=1");
				// Wait in this thread for the file to download
				Thread waitForDownload = new Thread () {
					public void run() {
				        try {
				        	for (Integer i = 0; i < Integer.parseInt(shotsPerTrigger); i++) {
							    String fileName = defaultName + i + ".jpg";
							    File file = new File(fileName);
							    int loops = 0;
							    while (!file.exists()) {
						    		loops++;
						    		if (loops == 0xFFFF) { break; } // ~ 15 seconds
							    }
							    // Do only if the file exists
							    if (file.exists()) {
								    Long timeStamp = new Date().getTime();
								    // Rename the file
								    String newName = subEpc + "-" + timeStamp + ".jpg";
									Runtime.getRuntime().exec("mv " + fileName + " " + newName);
									Thread.sleep(250);
									// Pass to the main CirrusII application for upload
									pictureQueue.put(newName);
							    } else {
					    			log( "Timeout waiting for file download!", Log.Level.Warning );
					    			// Cycle power on the camera
				    				enablePower(false);
									Thread.sleep(1000);
				    				enablePower(true);
									Thread.sleep(6000);
					    			log( "Camera Ready", Log.Level.Information );
							    }				        		
				        	}
				        } catch(Exception e){
			    			log( "Error waiting for file!\n" + e.toString(), Log.Level.Warning );
				        }
						// Ready to take a new picture
						waiting.set(false);
					}
				};
				waitForDownload.start();
				success = true;
				
			} catch (IOException e) {
				log( "Unable to take a picture!\n" + e.toString(), Log.Level.Error );
				waiting.set(false);
			}
		}
		return success;
	}

	/** 
	 * setImageFormat<P>
	 * This method executes a gphoto2 command.
	 */
	public void setImageFormat(String format) {
		if (format != null) {
			try {
				imageFormat = ImageFormat.valueOf(format);
	        } catch(IllegalArgumentException iae) {
	        	imageFormat = ImageFormat.SmallNormal;
	        }
		}
	}

	/** 
	 * setImageFormat<P>
	 * This method executes a gphoto2 command.
	 */
	public void setImageFormat(ImageFormat format) {
		imageFormat = format;
	}

	/** 
	 * updateImageFormat<P>
	 * This method executes a gphoto2 command.
	 */
	public Boolean updateImageFormat( ) {
		boolean success = false;
		// Check if we can do this
		if (waiting.get() == false) {
			try {
				// Send the gphoto2 command
				Runtime.getRuntime().exec("gphoto2 --set-config /main/imgsettings/imageformat=" + imageFormat.getValue());
				success = true;
			} catch (IOException e) {
				log( "Unable to update the image format!\n" + e.toString(), Log.Level.Error );
			}
		}
		return success;
	}

	/** 
	 * setShotsPerTrigger<P>
	 * This method executes a gphoto2 command.
	 */
	public Boolean setShotsPerTrigger(String shots_) {
		boolean success = false;
		if (shots_ != null) {
			int shots = Integer.parseInt(shots_);
			if ((shots > 0) && (shots < 4)) {
				shotsPerTrigger = shots_;							
				success = true;
			}
		}
		return success;
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
