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
import java.io.InputStreamReader;
import java.util.Date;

public class Sensors {

	private Integer ambientTemperature = null;
	private Integer relativeHumidity = null;
	private Boolean motionDetected = false;
	private Boolean imageAvailable = false;

	/** 
	 * Sensors<P>
	 * Class Constructor
	 */
	public Sensors( ) {

	}

	/** 
	 * getAmbientTemp<P>
	 * This method returns the current Ambient Temp.
	 * A value of null indicates an uninitialized value.
	 * @return An Integer value of temperature in degrees C.
	 */
	public String getAmbientTemp() {
		if (ambientTemperature == null) {
			return null;
		} else {
			return ambientTemperature.toString();
		}
	}

	/** 
	 * getRelativeHumidity<P>
	 * This method returns the current Relative Humidity.
	 * A value of null indicates an uninitialized value.
	 * @return An Integer value of humidity in %.
	 */
	public String getRelativeHumidity() {
		if (relativeHumidity == null) {
			return null;
		} else {
			return relativeHumidity.toString();
		}
	}

	/** 
	 * captureImage<P>
	 * This method returns the current Relative Humidity.
	 * A value of null indicates an uninitialized value.
	 * @return True if the command executed successfully.
	 */
	public Boolean captureImage() {

		return false;
	}

	/** 
	 * getImageAvailable<P>
	 * This method returns true if an image is available for download.
	 * @return A Boolean.
	 */
	public Boolean getImageAvailable() {
		return imageAvailable;
	}

	/** 
	 * getMotionDetected<P>
	 * This method returns the current state of the motion detector.
	 * @return A Boolean.
	 */
	public Boolean getMotionDetected() {
		return motionDetected;
	}

	/** 
	 * resetMotionDetector<P>
	 * This method resets the possible latched state of motionDetected.
	 * @return A Boolean.
	 */
	public Boolean resetMotionDetector() {
		motionDetected = false;
		return motionDetected;
	}

	/** 
	 * checkForMotion<P>
	 * This method checks the state of the motion sensor GPIO and returns
	 * true, latching the motionDetected state.
	 * @return A Boolean indicating if motion was present or not.
	 */
	public boolean checkForMotion() {
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("./check_motion.sh");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = stdInput.readLine();
			if ((line != null) && (line.startsWith("1")) && !motionDetected) {
				motionDetected = true;
				return true;
			} else {
				return false;				
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}	

	/** 
	 * getSensorIndicationJsonParams<P>
	 * This method builds the JSON RPC params element of 
	 * the sensor_event indication object.
	 * @return String The params object
	 */
	public String getSensorIndicationJsonParams( String facility_id, String device_id ) {
		Date date = new Date();
		Long timestamp = date.getTime();
		StringBuilder params = new StringBuilder("{");
		// When and Who
		params.append("\"sent_on\":" + timestamp + ",");
		params.append("\"facility_id\":\"" + facility_id + "\",");
		params.append("\"data\":[");
		if (motionDetected) {
			// Motion Detected
			params.append("{\"device_id\":\"" + device_id + "\",");
			params.append("\"event_type\":\"motion_detected\",");
			params.append("\"event_date\":" + timestamp + ",");
			params.append("\"location\":{ }");
			params.append("},");
		}
		if (imageAvailable) {
			// Image Available
			params.append("{\"device_id\":\"" + device_id + "\",");
			params.append("\"event_type\":\"image_available\",");
			params.append("\"event_date\":" + timestamp + ",");
			params.append("\"location\":{ }");
			params.append("},");
		}
		if (params.charAt(params.length() - 1) == ',') {
			params.deleteCharAt(params.length() - 1);
		}
		// End of records
		params.append("]}");
		// Return the string
		return params.toString();
	}
}
