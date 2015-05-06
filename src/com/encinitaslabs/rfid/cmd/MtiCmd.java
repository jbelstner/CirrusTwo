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
package com.encinitaslabs.rfid.cmd;

import com.encinitaslabs.rfid.utils.Crc16;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * MtiCmd Object
 * 
 * <P>This abstract class is the base for the creation of all the commands
 * supported by the MTI RU-824/861 RFID Low Level Command set.
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public abstract class MtiCmd {
	public static final String TAG = "MtiCmd:";
	public static final boolean DEBUG = false;

	public static final int PORT_REL_INDEX = 0;
	public static final int PHAS_REL_INDEX = 1;
	public static final int TEMP_REL_INDEX = 2;
	public static final int FREQ_REL_INDEX = 4;
	
	public static final int TYPE_INDEX = 0;
	public static final int CMD_ID_INDEX = 5;
	public static final int REL_SEQ_INDEX = 5;
	public static final int RESP_DATA_INDEX = 6;
	public static final int FLAGS_INDEX = 7;
	public static final int LENGTH_INDEX = 10;
	public static final int ALT_DATA_INDEX = 14;
	public static final int ACCESS_CMD_INDEX = 18;
	public static final int GAIN_INDEX = 20;
	public static final int RSSI_INDEX = 22;
	public static final int DATA_INDEX = 26;
	public static final int NUM_TAGS_INDEX = 2;
	public static final int MAX_CMD_LENGTH = 64;
	public static final short GAIN_MASK = 0x0038;
	public static final short GAIN_24DB = 0x0000;
	public static final short GAIN_18DB = 0x0008;
	public static final short GAIN_12DB = 0x0018;
	public static final short GAIN_06DB = 0x0038;
	public static final int LENGTH_OF_MTIC = 16;
	public static final int LENGTH_OF_MTIR = 16;
	public static final int LENGTH_OF_MTIB = 24;
	public static final int LENGTH_OF_MTIE = 24;
	public static final int LENGTH_OF_MTII = 64;
	public static final int LENGTH_OF_MTIA = 64;
	public static final int LENGTH_OF_MTIW = 24;
	public static final byte[] cmdHeader = {(byte)0x43, (byte)0x49, (byte)0x54, (byte)0x4D}; // "MTIC"
	public static final int HEADER_SIZE = cmdHeader.length;
	public static final int MTI_HEADER = 0x4D544900;
	public static final int MTI_HDR_MASK = 0xFFFFFF00;
	public static final int MTI_HDR_LENGTH = 4;
	public static final int STATUS_POS = 6;
	// OEM Configuration Addresses
	public static final short MAIN_MODEL_NAME_ADDR = 0x000A;
	public static final short SUB_MODEL_NAME_ADDR = 0x000B;
	public static final short OEMCFG_VERSION_ADDR = 0x000C;
	public static final short MFG_NAME_HEADER_ADDR = 0x000F;
	public static final short MFG_NAME_ADDR = 0x0010;
	public static final short PROD_NAME_HEADER_ADDR = 0x0037;
	public static final short PROD_NAME_ADDR = 0x0038;
	public static final short SERIAL_NUM_HEADER_ADDR = 0x005F;
	public static final short SERIAL_NUM_ADDR = 0x0060;
	public static final short HOST_IF_SEL_ADDR = 0x00A0;
	public static final short BAUD_RATE_ADDR = 0x00A4;
	public static final short DEVICE_ID_ADDR = 0x1B01;
	public static final short CUST_COUNTRY_NAME_ADDR = 0x22CF;

	public static enum RfidStatusCode {
		RFID_OK((int)0x0000),
		RFID_Invalid_Parameter((int)0x00f0),
		RFID_Module_Failure((int)0x00f1),
		RFID_Checksum_Mismatch((int)0xff00),
		RFID_Missing_Command((int)0xff01),
		RFID_Missing_Response((int)0xff02),
		RFID_Missing_Begin((int)0xff03),
		RFID_Missing_Inventory((int)0xff04),
		RFID_Missing_Access((int)0xff05),
		RFID_Missing_End((int)0xff06),
		RFID_No_Response((int)0xff07),
		RFID_No_Reader_Attached((int)0xff08);
		
		private int rfidStatusCode;
		
		RfidStatusCode(int rfidStatusCode) {
			this.rfidStatusCode = rfidStatusCode;
		}
		
		public int getValue() {
			return rfidStatusCode;
		}
	}

	public static enum MtiPacketType {
		Command((byte)'C'),
		Response((byte)'R'),
		Begin((byte)'B'),
		End((byte)'E'),
		Inventory((byte)'I'),
		Access((byte)'A'),
		Work((byte)'W');
		
		private byte mtiPacketType;
		
		MtiPacketType(byte mtiPacketType) {
			this.mtiPacketType = mtiPacketType;
		}
		
		public byte getValue() {
			return mtiPacketType;
		}
	}

	private ByteBuffer shortBuffer = ByteBuffer.allocate(2);
	private ByteBuffer intBuffer = ByteBuffer.allocate(4);
		
	protected CmdHead mCmdHead;
	protected ArrayList<Byte> mParam = new ArrayList<Byte>();

	public byte[] mFinalCmd = new byte[MAX_CMD_LENGTH];


	/** 
	 * composeCmd<P>
	 * This method is called only by the individual command classes to assemble
	 * the pieces that comprise the RFID serial command, append a checksum and
	 * send the command out the serial port.
	 * What is finally sent to the reader is byte[] command.
	 * @return The packed RU-824/861 command
	 */
	protected byte[] composeCmd() {
		int cmdLength = 14;
		
		mFinalCmd = Arrays.copyOfRange(cmdHeader, 0, 16);
		
		mFinalCmd[HEADER_SIZE] = (byte)0xff;
		mFinalCmd[HEADER_SIZE+1] = mCmdHead.get1stCmd();
		
		int arrLength = mParam.size();
		for(int i = 0; i < arrLength; i++) {
			mFinalCmd[HEADER_SIZE+2+i] = mParam.get(i).byteValue();
		}
		
		int Crc = ~Crc16.calculate(mFinalCmd, cmdLength);
		mFinalCmd[cmdLength++] = (byte)((Crc & 0x000000ff));
		mFinalCmd[cmdLength++] = (byte)((Crc & 0x0000ff00) >>> 8);
		
		// log whole command for debug
		if (DEBUG) {
			System.out.println( "MtiCmd.composeCmd(): " + byteArrayToString(mFinalCmd, true) );
		}
		return mFinalCmd;
	}	

	/** 
	 * getCommandLength<P>
	 * This helper method that returns the length of the MTI command in the byte[].
	 * @param command First byte in the command/
	 * @return The length in bytes of the command
	 */
	public static int getCommandLength(byte command) {
		int length = 0;
		if (command == 'C') {
			length = MtiCmd.LENGTH_OF_MTIC;
		} else if (command == 'R') {
			length = MtiCmd.LENGTH_OF_MTIR;
		} else if (command == 'B') {
			length = MtiCmd.LENGTH_OF_MTIB;
		} else if (command == 'E') {
			length = MtiCmd.LENGTH_OF_MTIE;
		} else if (command == 'I') {
			length = MtiCmd.LENGTH_OF_MTII;
		} else if (command == 'A') {
			length = MtiCmd.LENGTH_OF_MTIA;
		} else if (command == 'W') {
			length = MtiCmd.LENGTH_OF_MTIW;
		} else {
			length = 0;
		}
		return length;
	}
	
	/** 
	 * getCommandType<P>
	 * This helper method that returns the length of the MTI command in the byte[].
	 * @param command First byte in the command/
	 * @return The name of the command
	 */
	public static String getCommandType(byte command) {
		String type = null;
		if (command == 'C') {
			type = "Command";
		} else if (command == 'R') {
			type = "Response";
		} else if (command == 'B') {
			type = "Begin";
		} else if (command == 'E') {
			type = "End";
		} else if (command == 'I') {
			type = "Inventory";
		} else if (command == 'A') {
			type = "Access";
		} else if (command == 'W') {
			type = "Work";
		} else {
			type = null;
		}
		return type;
	}
	
	/** 
	 * getCmdHead<P>
	 * This helper method that returns the enum of the MTI command in the byte[].
	 * @param command The serial command
	 * @return The MTI CmdHead enum value for this command
	 */
	public static CmdHead getCmdHead(byte[] command) {
		// This business here takes into account that the byte commands
		// 0x80 through 0x8B end up being negative values in Java.
		Byte byteIndex = command[MtiCmd.CMD_ID_INDEX];
		int index = byteIndex.intValue();
		if (index < 0) {
			index = index + 256;
		}
		return CmdHead.values()[index];
	}
	
	/** 
	 * byteArrayToString<P>
	 * This helper method converts the entire array of bytes to a human readable
	 * string value.
	 * @param BtoS The array of bytes to convert
	 * @param space True means add a space between each byte in the string
	 * @return The human readable string value
	 */
	public static String byteArrayToString(byte[] BtoS, boolean space) {
		String hexResult = "";
		int iLength = BtoS.length;
		
		if(space) {
			for (int i = 0; i < iLength; i++) {
				hexResult += ((BtoS[i] < 0 || BtoS[i] > 15)
							? Integer.toHexString(0xff & (int)BtoS[i])
							: "0" + Integer.toHexString(0xff & (int)BtoS[i]))
							+ ((i == BtoS.length - 1) ? "" : " ");
			}
		} else {
			for (int i = 0; i < iLength; i++) {
				hexResult += ((BtoS[i] < 0 || BtoS[i] > 15)
							? Integer.toHexString(0xff & (int)BtoS[i])
							: "0" + Integer.toHexString(0xff & (int)BtoS[i]));
			}
		}
		return hexResult.toUpperCase(Locale.getDefault());
	}
	
	/** 
	 * byteArrayToString<P>
	 * This helper method converts a portion of an array of bytes to a human readable
	 * string value.
	 * @param BtoS The array of bytes to convert
	 * @param length The number of bytes to convert (starting at the beginning)
	 * @param space True means add a space between each byte in the string
	 * @return The human readable string value
	 */
	public static String byteArrayToString(byte[] BtoS, int length, boolean space) {
		String hexResult = "";

		if(space) {
			for (int i = 0; i < length; i++) {
				hexResult += ((BtoS[i] < 0 || BtoS[i] > 15)
							? Integer.toHexString(0xff & (int)BtoS[i])
							: "0" + Integer.toHexString(0xff & (int)BtoS[i]))
							+ ((i == length - 1) ? "" : " ");
			}
		} else {
			for (int i = 0; i < length; i++) {
				hexResult += ((BtoS[i] < 0 || BtoS[i] > 15)
							? Integer.toHexString(0xff & (int)BtoS[i])
							: "0" + Integer.toHexString(0xff & (int)BtoS[i]));
			}
		}
		return hexResult.toUpperCase(Locale.getDefault());
	}
	

	/** 
	 * byteArrayToString<P>
	 * This helper method converts a portion of an array of bytes to a human readable
	 * string value.
	 * @param byteArray The array of bytes to convert
	 * @param startIndex The array index to begin conversion
	 * @param length The number of bytes to convert (starting at the startIndex)
	 * @param addSpace True means add a space between each byte in the string
	 * @return The human readable string value
	 */
	public static String byteArrayToString(byte[] byteArray, int startIndex, int length, boolean addSpace) {
		StringBuilder hexResult = new StringBuilder("");

		if (addSpace) {
			for (int i = startIndex; i < startIndex + length; i++) {
				if (byteArray[i] < 0 || byteArray[i] > 15) {
					hexResult.append(Integer.toHexString(0xff & (int)byteArray[i]));
				} else {
					hexResult.append("0");
					hexResult.append(Integer.toHexString(0xff & (int)byteArray[i]));
				}
				if (i < (startIndex + length - 1)) {
					hexResult.append(" ");
				}
			}
		} else {
			for (int i = startIndex; i < startIndex + length; i++) {
				if (byteArray[i] < 0 || byteArray[i] > 15) {
					hexResult.append(Integer.toHexString(0xff & (int)byteArray[i]));
				} else {
					hexResult.append("0");
					hexResult.append(Integer.toHexString(0xff & (int)byteArray[i]));
				}
			}
		}
		return hexResult.toString().toUpperCase();
	}
	

	/** 
	 * stringToByteArray<P>
	 * This helper method converts a human readable string value to an array of bytes. 
	 * @param StoB The human readable string value to convert
	 * @return The array of bytes
	 */
	public static byte[] stringToByteArray(String StoB) {
    	String subStr;
    	int iLength = StoB.length() / 2;
    	byte[] bytes = new byte[iLength];
    	
        for (int i = 0; i < iLength; i++) {
        	subStr = StoB.substring(2 * i, 2 * i + 2);
        	bytes[i] = (byte)Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
    
	/** 
	 * addParam<P>
	 * This helper method adds a short integer to the mParam byte array in
	 * little endian format. 
	 * @param param The short integer to add
	 * @return void
	 */
    public void addParam(short param) {
//		ByteBuffer shortBuffer = ByteBuffer.allocate(2);
		shortBuffer.clear();
		shortBuffer.order(ByteOrder.LITTLE_ENDIAN);
		shortBuffer.putShort(param);
		for(int i = 0; i < 2; i++)
			mParam.add(shortBuffer.get(i));
    }
    
	/** 
	 * addParam<P>
	 * This helper method adds an integer to the mParam byte array in
	 * little endian format. 
	 * @param param The integer to add
	 * @return void
	 */
    public void addParam(int param) {
//		ByteBuffer intBuffer = ByteBuffer.allocate(4);
    	intBuffer.clear();
		intBuffer.order(ByteOrder.LITTLE_ENDIAN);
		intBuffer.putInt(param);
		for(int i = 0; i < 4; i++)
			mParam.add(intBuffer.get(i));
    }
    
	/** 
	 * getInt<P>
	 * This helper method returns an 32-bit integer from the mResponse byte array,
	 * which is in little endian format. 
	 * @param response The response buffer
	 * @param startByte The zero-based index into the response buffer
	 * following the first seven bytes of common information
	 * @return The desired 32-bit integer
	 */
    public static int getInt(byte[] response, int startByte) {
    	byte[] subResponse = Arrays.copyOfRange(response, startByte, startByte + 4);
		ByteBuffer byteBuffer = ByteBuffer.wrap(subResponse);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		int iResponse = byteBuffer.getInt();
		
		return iResponse;
    }
    
	/** 
	 * getShort<P>
	 * This helper method returns a 16-bit integer from the mResponse byte array,
	 * which is in little endian format. 
	 * @param response The response buffer
	 * @param startByte The zero-based index into the response buffer
	 * following the first seven bytes of common information
	 * @return The desired 16-bit integer
	 */
    public static short getShort(byte[] response, int startByte) {
    	byte[] subResponse = Arrays.copyOfRange(response, startByte, startByte + 2);
		ByteBuffer byteBuffer = ByteBuffer.wrap(subResponse);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		short iResponse = byteBuffer.getShort();

		return iResponse;
    }
    
}

