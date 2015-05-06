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
package com.encinitaslabs.rfid.utils;

import java.util.Arrays;

/**
 * Crc16 Object
 * 
 * <P>This class provides member functions for calculating a 16-bit CRC.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class Crc16 {
	private static final int poly = 0x1021;
	private static final int[] crcTable = new int[256];

	static {
	    for(int i = 0; i < 256; i++) {
	        int fcs = 0;
	        int d = i << 8;
	        for (int k = 0; k < 8; k++) {
	            if (((fcs ^ d) & 0x8000) != 0)
	                fcs = (fcs << 1) ^ poly;
	            else
	                fcs = (fcs << 1);
	            d <<= 1;
	            fcs &= 0xffff;
	        }
	        crcTable[i] = fcs;
	    }
    }

	/** 
	 * calculate
	 * 
	 * This method calculates the 16-bit CRC against the given array.
	 * 
	 * @param bytes		The byte array containing the data
	 * @param length	The length in bytes to calculate the CRC across
	 * 
	 * @return 			The 16-bit CRC
	 */
	public static int calculate(byte[] bytes, int length) {
		int work = 0xffff;
		
		for(int i = 0; i < length; i++)
			work = (crcTable[(bytes[i] ^ (work >>> 8)) & 0xff ] ^ (work << 8)) & 0xffff;
		
		return work;
	}
	
	/** 
	 * check
	 * 
	 * This method checks the 16-bit CRC against the given array.
	 * 
	 * @param bytes		The byte array containing the data
	 * @param length	The length in bytes to calculate the CRC across
	 * 
	 * @return			True if the 16-bit CRC is correct
	 */
	public static boolean check(byte[] bytes, int length) {
		byte[] baData;
		byte buffer;
		
		baData = Arrays.copyOfRange(bytes, 0, length);
		buffer = baData[length-2];
		baData[length-2] = baData[length-1];
		baData[length-1] = buffer;

		return (calculate(baData, length) == 0x1d0f);
	}
}
