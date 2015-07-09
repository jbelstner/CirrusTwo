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

/**
 * CmdReaderModuleFirmwareAccess Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the RFID Reader/Module Firmware Access commands (section 4.6)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdReaderModuleFirmwareAccess {

	public enum ErrorType {
		CurrentError((byte)0x00),
		LastError((byte)0x01);
		
		private byte bErrorType;
		
		ErrorType(byte bErrorType) {
			this.bErrorType = bErrorType;
		}

		public byte getValue() {
			return bErrorType;
		}
	}

	public enum RegionOperation {
		US_CA((byte)0),
		EU((byte)1),
		EU2((byte)2),
		TW((byte)3),
		CN((byte)4),
		KR((byte)5),
		AU_NZ((byte)6),
		BR((byte)7),
		IL((byte)8),
		IN((byte)9),
		Custom((byte)10);
		
		private byte bRegionOperation;
		
		RegionOperation(byte bRegionOperation) {
			this.bRegionOperation = bRegionOperation;
		}

		public byte getValue() {
			return bRegionOperation;
		}
	}

	
	/**
	 * RFID_MacGetFirmwareVersion Object
	 * 
	 * <P>This class implements the RFID_MacGetFirmwareVersion command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	public static final class RFID_MacGetFirmwareVersion extends MtiCmd {
		public RFID_MacGetFirmwareVersion(){
			mCmdHead = CmdHead.RFID_MacGetFirmwareVersion;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}

		/** 
		 * getVersion
		 * 
		 * This method is called to copy the FW version returned on the serial port
		 * following a MacGetFirmwareVersion command.
		 * 
		 * @param response The response buffer.
		 * @return A string corresponding to the firmware version
		 */
		public static String getVersion(byte[] response) {
			String fwVersion = "0.0.0"; // default string
			byte status = response[RESP_DATA_INDEX];
			if (status == 0x00) {
				fwVersion = Byte.toString(response[RESP_DATA_INDEX + 1]) + "." + 
							Byte.toString(response[RESP_DATA_INDEX + 2]) + "." +
							Byte.toString(response[RESP_DATA_INDEX + 3]);
			}
			return fwVersion;
		}
	}

	
	/**
	 * RFID_MacGetDebug Object
	 * 
	 * <P>This class implements the RFID_MacGetDebug command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacGetDebug extends MtiCmd {
		public RFID_MacGetDebug(){
			mCmdHead = CmdHead.RFID_MacGetDebug;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacClearError Object
	 * 
	 * <P>This class implements the RFID_MacClearError command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacClearError extends MtiCmd {
		public RFID_MacClearError(){
			mCmdHead = CmdHead.RFID_MacClearError;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacGetError Object
	 * 
	 * <P>This class implements the RFID_MacGetError command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	public static final class RFID_MacGetError extends MtiCmd {
		public RFID_MacGetError(){
			mCmdHead = CmdHead.RFID_MacGetError;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param errorType
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(ErrorType errorType) {
			mParam.clear();
			mParam.add(errorType.bErrorType);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param errorType
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte errorType) {
			mParam.clear();
			mParam.add(errorType);
			return composeCmd();
		}

		/** 
		 * parseResponse
		 * <P>This method is called to extract the error code from the response packet.
		 * 
		 * @param response The response buffer
		 * @return The Integer error code
		 */
		public static int parseResponse(byte[] response) {
			return getInt(response, STATUS_POS+1);
		}
	}

	
	/**
	 * RFID_MacGetBootloaderVersion Object
	 * 
	 * <P>This class implements the RFID_MacGetBootloaderVersion command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacGetBootloaderVersion extends MtiCmd {
		public RFID_MacGetBootloaderVersion(){
			mCmdHead = CmdHead.RFID_MacGetBootloaderVersion;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacWriteOemData Object
	 * 
	 * <P>This class implements the RFID_MacWriteOemData command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacWriteOemData extends MtiCmd {
		public RFID_MacWriteOemData(){
			mCmdHead = CmdHead.RFID_MacWriteOemData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param address
		 * @param data
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address, int data) {
			mParam.clear();
			addParam(address);
			addParam(data);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacReadOemData Object
	 * 
	 * <P>This class implements the RFID_MacReadOemData command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacReadOemData extends MtiCmd {
		public RFID_MacReadOemData(){
			mCmdHead = CmdHead.RFID_MacReadOemData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param The address
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address) {
			mParam.clear();
			addParam(address);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacBypassWriteRegister Object
	 * 
	 * <P>This class implements the RFID_MacBypassWriteRegister command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacBypassWriteRegister extends MtiCmd {
		public RFID_MacBypassWriteRegister(){
			mCmdHead = CmdHead.RFID_MacBypassWriteRegister;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param address
		 * @param data
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address, short data) {
			mParam.clear();
			addParam(address);
			addParam(data);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacBypassReadRegister Object
	 * 
	 * <P>This class implements the RFID_MacBypassReadRegister command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacBypassReadRegister extends MtiCmd {
		public RFID_MacBypassReadRegister(){
			mCmdHead = CmdHead.RFID_MacBypassReadRegister;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param address
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address) {
			mParam.clear();
			addParam(address);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacSetRegion Object
	 * 
	 * <P>This class implements the RFID_MacSetRegion command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacSetRegion extends MtiCmd {
		public RFID_MacSetRegion(){
			mCmdHead = CmdHead.RFID_MacSetRegion;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param regionOperation
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(RegionOperation regionOperation) {
			mParam.clear();
			addParam(regionOperation.bRegionOperation);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param regionOperation
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte regionOperation) {
			mParam.clear();
			addParam(regionOperation);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacGetRegion Object
	 * 
	 * <P>This class implements the RFID_MacGetRegion command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacGetRegion extends MtiCmd {
		public RFID_MacGetRegion(){
			mCmdHead = CmdHead.RFID_MacGetRegion;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}
		
		public byte getRegion(byte[] response) {
			return response[STATUS_POS+1];
		}
	}

	
	/**
	 * RFID_MacGetOEMCfgVersion Object
	 * 
	 * <P>This class implements the RFID_MacGetOEMCfgVersion command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacGetOEMCfgVersion extends MtiCmd {
		public RFID_MacGetOEMCfgVersion(){
			mCmdHead = CmdHead.RFID_MacGetOEMCfgVersion;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}
	}

	
	/**
	 * RFID_MacGetOEMCfgUpdateNumber Object
	 * 
	 * <P>This class implements the RFID_MacGetOEMCfgUpdateNumber command as defined
	 * in section 4.6 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_MacGetOEMCfgUpdateNumber extends MtiCmd {
		public RFID_MacGetOEMCfgUpdateNumber(){
			mCmdHead = CmdHead.RFID_MacGetOEMCfgUpdateNumber;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}
	}
}
