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
 * CmdReaderModuleConfig Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the RFID Reader/Module Configuration commands (section 4.1)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdReaderModuleConfig {
	
	public enum OperationMode {
		Continuous((byte)0x00),
		NonContinuous((byte)0x01);
		
		private byte bOperationMode;
		
		OperationMode(byte bOperationMode) {
			this.bOperationMode = bOperationMode;
		}

		public byte getValue() {
			return bOperationMode;
		}
	}


	/**
	 * RFID_RadioSetDeviceID Object
	 * 
	 * <P>This class implements the RFID_RadioSetDeviceID command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioSetDeviceID extends MtiCmd {
		public RFID_RadioSetDeviceID() {
			mCmdHead = CmdHead.RFID_RadioSetDeviceID;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param deviceId
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte deviceId) {
			mParam.clear();
			mParam.add(deviceId);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioGetDeviceID Object
	 * 
	 * <P>This class implements the RFID_RadioGetDeviceID command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioGetDeviceID extends MtiCmd {
		public RFID_RadioGetDeviceID() {
			mCmdHead = CmdHead.RFID_RadioGetDeviceID;
		}

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
		 * getDeviceId
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Device Id
		 */
		public byte getDeviceId(byte[] response) {
			return response[STATUS_POS + 1];
		}
	}

	
	/**
	 * RFID_RadioSetOperationMode Object
	 * 
	 * <P>This class implements the RFID_RadioSetOperationMode command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioSetOperationMode extends MtiCmd {
		public RFID_RadioSetOperationMode() {

			mCmdHead = CmdHead.RFID_RadioSetOperationMode;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param operationMode (Continuous or NonContinuous)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(OperationMode operationMode) {
			mParam.clear();
			mParam.add(operationMode.bOperationMode);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param operationMode (0 or 1)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte operationMode) {
			mParam.clear();
			mParam.add(operationMode);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioGetOperationMode Object
	 * 
	 * <P>This class implements the RFID_RadioGetOperationMode command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioGetOperationMode extends MtiCmd {
		public RFID_RadioGetOperationMode() {
			mCmdHead = CmdHead.RFID_RadioGetOperationMode;
		}

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
		 * getOperationMode
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Operation Mode
		 */
		public byte getOperationMode(byte[] response) {
			return response[STATUS_POS + 1];
		}
	}

	
	/**
	 * RFID_RadioSetCurrentLinkProfile Object
	 * 
	 * <P>This class implements the RFID_RadioSetCurrentLinkProfile command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioSetCurrentLinkProfile extends MtiCmd {
		public RFID_RadioSetCurrentLinkProfile() {
			mCmdHead = CmdHead.RFID_RadioSetCurrentLinkProfile;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param linkProfile to write
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte linkProfile) {
			mParam.clear();
			mParam.add(linkProfile);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioGetCurrentLinkProfile Object
	 * 
	 * <P>This class implements the RFID_RadioGetCurrentLinkProfile command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioGetCurrentLinkProfile extends MtiCmd {
		public RFID_RadioGetCurrentLinkProfile() {
			mCmdHead = CmdHead.RFID_RadioGetCurrentLinkProfile;
		}

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
		 * getCurrentLinkProfile
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Current Link Profile
		 */
		public byte getCurrentLinkProfile(byte[] response) {
			return response[STATUS_POS + 1];
		}
	}

	
	/**
	 * RFID_RadioWriteRegister Object
	 * 
	 * <P>This class implements the RFID_RadioWriteRegister command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioWriteRegister extends MtiCmd {
		public RFID_RadioWriteRegister() {
			mCmdHead = CmdHead.RFID_RadioWriteRegister;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param 16-bit address of the register to write
		 * @param 32-bit value to read
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address, int value) {
			mParam.clear();
			addParam(address);
			addParam(value);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioReadRegister Object
	 * 
	 * <P>This class implements the RFID_RadioReadRegister command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioReadRegister extends MtiCmd {
		public RFID_RadioReadRegister() {
			mCmdHead = CmdHead.RFID_RadioReadRegister;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds and sends the command out the serial port.
		 * 
		 * @param 16-bit address of the register to read
		 * 
		 * @return RFID_STATUS or RFID_ERROR code
		 */
		public byte[] setCmd(short address) {
			mParam.clear();
			addParam(address);
			return composeCmd();
		}
		
		/** 
		 * getRegisterValue
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Register Value
		 */
		public int getRegisterValue(byte[] response) {
			return getInt(response, STATUS_POS + 1);
		}
	}

	
	
	/**
	 * RFID_RadioWriteBankedRegister Object
	 * 
	 * <P>This class implements the RFID_RadioWriteBankedRegister command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioWriteBankedRegister extends MtiCmd {
		public RFID_RadioWriteBankedRegister() {
			mCmdHead = CmdHead.RFID_RadioWriteBankedRegister;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param 16-bit address of the register to write
		 * @param 16-bit bankSelector of the register to write
		 * @param 32-bit value to read
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address, short bankSelector, int value) {
			mParam.clear();
			addParam(address);
			addParam(bankSelector);
			addParam(value);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioReadBankedRegister Object
	 * 
	 * <P>This class implements the RFID_RadioReadBankedRegister command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioReadBankedRegister extends MtiCmd {
		public RFID_RadioReadBankedRegister() {
			mCmdHead = CmdHead.RFID_RadioReadBankedRegister;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param 16-bit address of the register to read
		 * @param 16-bit bankSelector of the register to read
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address, int bankSelector) {
			mParam.clear();
			addParam(address);
			addParam(bankSelector);
			return composeCmd();
		}
		
		/** 
		 * getBankedRegisterValue
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Banked Register Value
		 */
		public int getBankedRegisterValue(byte[] response) {
			return getInt(response, STATUS_POS + 1);
		}
	}

	
	/**
	 * RFID_RadioReadRegisterInfo Object
	 * 
	 * <P>This class implements the RFID_RadioReadRegisterInfo command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioReadRegisterInfo extends MtiCmd {
		public RFID_RadioReadRegisterInfo() {
			mCmdHead = CmdHead.RFID_RadioReadRegisterInfo;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param 16-bit address of the register to read
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short address) {
			mParam.clear();
			addParam(address);
			return composeCmd();
		}
		
		/** 
		 * getRegisterType
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Register Type
		 */
		public byte getRegisterType(byte[] response) {
			return response[STATUS_POS + 1];
		}
		
		/** 
		 * getAccessType
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Access Type
		 */
		public byte getAccessType(byte[] response) {
			return response[STATUS_POS + 2];
		}
		
		/** 
		 * getBankSize
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Bank Size
		 */
		public byte getBankSize(byte[] response) {
			return response[STATUS_POS + 3];
		}
		
		/** 
		 * getSelectorAddress
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Selector Address
		 */
		public short getSelectorAddress(byte[] response) {
			return getShort(response, STATUS_POS + 4);
		}
		
		/** 
		 * getCurrentSelector
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Current Selector
		 */
		public short getCurrentSelector(byte[] response) {
			return getShort(response, STATUS_POS + 6);
		}
	}

	
	/**
	 * RFID_RadioSetInventoryPacketFormat Object
	 * 
	 * <P>This class implements the RFID_RadioSetInventoryPacketFormat command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioSetInventoryPacketFormat extends MtiCmd {
		public RFID_RadioSetInventoryPacketFormat() {
			mCmdHead = CmdHead.RFID_RadioSetInventoryPacketFormat;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param Inventory Packet Format to write
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte packetFormat) {
			mParam.clear();
			mParam.add(packetFormat);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioGetCurrentLinkProfile Object
	 * 
	 * <P>This class implements the RFID_RadioGetInventoryPacketFormat command as defined
	 * in section 4.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioGetInventoryPacketFormat extends MtiCmd {
		public RFID_RadioGetInventoryPacketFormat() {
			mCmdHead = CmdHead.RFID_RadioGetInventoryPacketFormat;
		}

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
		 * getPacketFormat
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Current Packet Format
		 */
		public byte getCurrentPacketFormat(byte[] response) {
			return response[STATUS_POS + 1];
		}
	}
}
