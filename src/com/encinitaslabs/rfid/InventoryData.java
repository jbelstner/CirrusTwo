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
 * InventoryData Object
 * <P>Attributes and functionality corresponding to a tag read.
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class InventoryData {
	private String epc = null;
	private Byte antPort = null;
	private Long date = null;
	private Integer rssi = null;
	private Integer readCount = null;
	private final Short rssiDeltaThreshold = 100;
	
	/** 
	 * InventoryData<P>
	 * Class Constructor
	 * @param response The byte buffer containing the inventory response
	 * @param port The antenna port active at the time of this inventory
	 */
	public InventoryData(TagData tagData) {
		// Save the fixed part of the object
		epc = tagData.epc;
		antPort = tagData.antPort;
		date = tagData.timeStamp;
		Short rssi_short = tagData.rssi;
		rssi = rssi_short.intValue();
		readCount = 1;
	}
	
	/** 
	 * addOccurrence<P>
	 * This method adds another read occurrence of the same epc.
	 * @return The objects are equal
	 */
	public Boolean addOccurrence(TagData tagData) {
		// Update the last read on
		date = tagData.timeStamp;
		Short rssi_short = tagData.rssi;
		// Look for possible motion
		boolean possibleMotion = Math.abs(rssi_short.intValue() - rssi) > rssiDeltaThreshold;
		// Calculate a new average RSSI
		rssi = (rssi_short.intValue() + (rssi * (readCount - 1))) / readCount;
		readCount++;
		return possibleMotion;
	}

	/** 
	 * equals<P>
	 * This method overrides the equals method for this class.
	 * Two Inventory data objects are equal if their epc and
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
		    final InventoryData other = (InventoryData) obj;
		    if (this.epc.equalsIgnoreCase(other.epc) && (this.antPort == other.antPort)) {
		        return true;
		    }
	    } catch (NullPointerException npe) {
		    return false;
	    }
	    return false;
	}

	/** 
	 * getEpc<P>
	 * This method returns the EPC for the tag was read.
	 * @return A string corresponding to the EPC
	 */
	public String getEpc() {
		return epc;
	}

	/** 
	 * getAntPort<P>
	 * This method returns antenna port used to read this tag.
	 * @return A byte corresponding to the Antenna Port
	 */
	public Byte getAntPort() {
		return antPort;
	}

	/** 
	 * getNumTagReads<P>
	 * This method returns the number of times this tag was read.
	 * @return The number of times this tag was read
	 */
	public Integer getNumTagReads() {
		return readCount;
	}

	/** 
	 * getDate<P>
	 * This method returns the last date and time this tag was read.
	 * @return A long corresponding to the uS epoch time
	 */
	public Long getDate() {
		return date;
	}

	/** 
	 * getRssi<P>
	 * This method returns RSSI at the time this tag was read.
	 * @return A long corresponding to the RSSI in dBm
	 */
	public Integer getRssi() {
		return rssi;
	}
}
	
