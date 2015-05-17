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

/**
 * TagData Object
 * <P>Attributes corresponding to a single tag read.
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class TagData {
	public String epc = null;
	public byte antPort = 0x00;
	public short rssi = 0;
	public byte phase = 0x00;
	public int freqKHz = 0;
	public byte temp = 0x00;
	public boolean crcValid = false;
	public long timeStamp = 0;
	public int shotCount = 0;
	public MotionState motionState = MotionState.Idle;

	public enum MotionState {
		Idle,
		PossibleMotion,
		InMotion
	}

	/** 
	 * TagData<P>
	 * Class Constructor
	 */
	public TagData( ) {

	}

	/** 
	 * update<P>
	 * This method uses RSSI to determine possible tag motion.
	 * Sets the motion bit is the change in RSSI exceeds the threshold
	 */
	public void update(TagData tagData, int motionThresholdDb) {
		// Update the last read on
		this.timeStamp = tagData.timeStamp;
		// Look for possible motion
		boolean motionDetect = false;
		if (this.rssi > tagData.rssi) {
			motionDetect = ((this.rssi - tagData.rssi) > (motionThresholdDb * 10));
		} else {
			motionDetect = ((tagData.rssi - this.rssi) > (motionThresholdDb * 10));
		}
		// Process based on the current MotionState
		if (motionState.compareTo(MotionState.InMotion) == 0) {
			// Do nothing here once we reach this state
		} else if (motionState.compareTo(MotionState.PossibleMotion) == 0) {
			// See if motion is detected again
			if (motionDetect) {
				motionState = MotionState.InMotion; // Change state
				this.rssi = tagData.rssi; // Update the RSSI to the new location
			} else {
				motionState = MotionState.Idle; // We had a false alarm
			}
		} else { // MotionState.Idle
			if (motionDetect) {
				motionState = MotionState.PossibleMotion; // We might have motion
			}
		}
	}

	/** 
	 * equals<P>
	 * This method overrides the equals method for this class.
	 * Two TagData objects are equal if their epc and
	 * antenna port values are the same.
	 * @return True if the objects are equal
	 */
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if ((obj instanceof InventoryData) == false) {
	        return false;
	    }
	    try {
		    final TagData other = (TagData) obj;
		    if (this.epc.equalsIgnoreCase(other.epc) && (this.antPort == other.antPort)) {
		        return true;
		    }
	    } catch (NullPointerException npe) {
		    return false;
	    }
	    return false;
	}
}
