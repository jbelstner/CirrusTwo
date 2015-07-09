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
	private final Integer WAIT_FOR_FILE = 30;
	private final Integer WAIT_INTERVAL = 500;
	private final Integer WAIT_FOR_EXEC = 200;
	private LinkedBlockingQueue<String> pictureQueue = null;
	private LinkedBlockingQueue<String> commandQueue = null;
    private AtomicBoolean busy = null;
	private String make = null;
	private String model = null;
	private String version = null;
	private String serialNumber = null;
	private Log logObject = null;
	private ImageFormat imageFormat = ImageFormat.SmallNormal;
	private String shotsPerTrigger = "1";
	private Boolean cameraOn = false;
	
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
		busy = new AtomicBoolean(false);
		commandQueue = new LinkedBlockingQueue<String>();

		// MANAGE THE CAMERA COMMAND QUEUE
		Thread tagInfoWorker = new Thread () {
			public void run() {
				Runtime rt = Runtime.getRuntime();
				while ( true ) {
					try {
						// This method blocks until a camera command is available
						String command = commandQueue.take();
						// The camera is busy
						busy.set(true);
						// Process the camera command
						processCommand(command, rt);
						// The camera is no longer busy
						busy.set(false);
					} catch (Exception e) {
						log("Error processing tagInfoQueue\n" + e.toString(), Log.Level.Error);
					}
				}
			}
		};
		tagInfoWorker.start();
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
				log("Camera power ON", Log.Level.Information);
				cameraOn = true;
			} else {
				Runtime.getRuntime().exec("./camera_power.sh 1");				
				log("Camera power OFF", Log.Level.Information);
				cameraOn = false;
			}
			return true;
		} catch (IOException e) {
			log( "Unable to enable camera power!", Log.Level.Error );
			return false;
		}
	}

	/** 
	 * isReady<P>
	 * This method returns True if the camera is busy or not on.
	 * @return A Boolean.
	 */
	public Boolean isBusy() {
		return (!cameraOn || busy.get());
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
	 * requestCameraInfo<P>
	 * This method queues a gphoto2 command.
	 */
	public Boolean requestCameraInfo( ) {
		boolean success = false;
		try {
			commandQueue.put("summary");
			success = true;				
		} catch (Exception e) {
			log( "Unable to queue the summary command!\n" + e.toString(), Log.Level.Error );
		}
		return success;
	}

	/** 
	 * updateImageFormat<P>
	 * This method queues a gphoto2 command.
	 */
	public Boolean updateImageFormat( ) {
		boolean success = false;
		try {
			commandQueue.put("set-config");
			success = true;				
		} catch (Exception e) {
			log( "Unable to queue the set-config command!\n" + e.toString(), Log.Level.Error );
		}
		return success;
	}

	/** 
	 * setShotsPerTrigger<P>
	 * This method stores local variables.
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
	 * takePhoto<P>
	 * This method queues a gphoto2 command.
	 * @throws IOException
	 */
	public Boolean takePhoto( String tagInfo ) {
		boolean success = false;
		try {
			commandQueue.put("capture-image-and-download " + tagInfo);
			success = true;				
		} catch (Exception e) {
			log( "Unable to queue the capture command!\n" + e.toString(), Log.Level.Error );
		}
		return success;
	}

	/** 
	 * processCommand<P>
	 * This method processes the queued camera command.
	 */
	private void processCommand(String command, Runtime rt) {
		// Parse the command
		String cmd[] = command.split(" ");
		String method = cmd[0];
		if (method.equalsIgnoreCase("set-config")) {
			setConfig(rt);
		} else if (method.equalsIgnoreCase("summary")) {
			summary(rt);
		} else if (method.equalsIgnoreCase("capture-image-and-download")) {
			if (cmd.length == 2) {
				captureImageAndDownload(rt, cmd[1]);
			}
		} else {
			log( "Invalid camera command", Log.Level.Warning );
		}
	}

	/** 
	 * setConfig<P>
	 * This method executes a gphoto2 command.
	 */
	private void setConfig(Runtime rt) {
		try {
			// Send the gphoto2 command
			log("gphoto2 --set-config /main/imgsettings/imageformat=" + imageFormat.getValue(), Log.Level.Debug);
			rt.exec("gphoto2 --set-config /main/imgsettings/imageformat=" + imageFormat.getValue());
		} catch (IOException e) {
			log("Error --set-config\n" + e.toString(), Log.Level.Error);
		}
	}

	/** 
	 * summary<P>
	 * This method executes a gphoto2 command.
	 * @throws IOException 
	 */
	private void summary(Runtime rt) {
		try {
			// Send the gphoto2 command
			log("gphoto2 --summary", Log.Level.Debug);
			Process proc = rt.exec("gphoto2 --summary");
			Thread.sleep(WAIT_FOR_EXEC);
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
		} catch (Exception e) {
			log("Error --summary\n" + e.toString(), Log.Level.Error);
		}
	}

	/** 
	 * captureImageAndDownload<P>
	 * This method executes a gphoto2 command.
	 */
	private void captureImageAndDownload(Runtime rt, String subEpc) {
		try {
			// Send the gphoto2 command
			log("gphoto2 --capture-image-and-download -F=" + shotsPerTrigger + " -I=1", Log.Level.Debug);
			rt.exec("gphoto2 --capture-image-and-download -F=" + shotsPerTrigger + " -I=1");
			// Now wait for the file(s) to download
        	for (Integer i = 0; i < Integer.parseInt(shotsPerTrigger); i++) {
			    String fileName = defaultName + i + ".jpg";
			    File file = new File(fileName);
			    int wait = WAIT_FOR_FILE;
			    while (!file.exists()) {
					Thread.sleep(WAIT_INTERVAL);
					wait--;
		    		if (wait <= 0) { break; }
			    }
			    // Queue the filename for upload if it exists
			    if (file.exists()) {
					log("Received " + fileName, Log.Level.Debug);
				    Long timeStamp = new Date().getTime();
				    // Rename the file
				    String newName = subEpc + "-" + timeStamp + ".jpg";
					Runtime.getRuntime().exec("mv " + fileName + " " + newName);
					Thread.sleep(250);
					pictureQueue.put(newName);
					Thread.sleep(1000);
			    } else {
	    			log( "Timeout waiting for file download!", Log.Level.Warning );
	    			// Cycle power on the camera
    				enablePower(false);
					Thread.sleep(1000);
    				enablePower(true);
					Thread.sleep(6000);
	    			log( "Camera Ready", Log.Level.Information );
	    			break; // break out of the for loop
			    }				        		
        	}
			log("gphoto2 --delete-all-files", Log.Level.Debug);
			rt.exec("gphoto2 --delete-all-files");
			Thread.sleep(1000);
		} catch (Exception e) {
			log("Error --capture-image-and-download\n" + e.toString(), Log.Level.Error);
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
