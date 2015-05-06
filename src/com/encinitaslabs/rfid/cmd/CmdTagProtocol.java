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

import java.util.Arrays;

import com.encinitaslabs.rfid.TagData;

/**
 * CmdTagProtocol Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the ISO 18000-6C Tag Protocol Operation commands (section 4.4)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdTagProtocol {

	public enum PerformSelect {
		No((byte)0x00),
		Yes((byte)0x01);
		
		private byte bPerformSelect;
		
		PerformSelect(byte bPerformSelect) {
			this.bPerformSelect = bPerformSelect;
		}

		public byte getValue() {
			return bPerformSelect;
		}
	}

	public enum PerformPostMatch {
		No((byte)0x00),
		Yes((byte)0x01);
		
		private byte bPerformPostMatch;
		
		PerformPostMatch(byte bPerformPostMatch) {
			this.bPerformPostMatch = bPerformPostMatch;
		}

		public byte getValue() {
			return bPerformPostMatch;
		}
	}

	public enum PerformGuardMode {
		RealtimeMode((byte)0x00),
		ScreeningMode((byte)0x01),
		NoScreeningDisCmdWorkMode((byte)0x02),
		ScreeningDisCmdWorkMode((byte)0x03),
		NoScreeningEnCmdWorkMode((byte)0x04),
		ScreeningEnCmdWorkMode((byte)0x05);
		
		private byte bPerformGuardMode;
		
		PerformGuardMode(byte bPerformGuardMode) {
			this.bPerformGuardMode = bPerformGuardMode;
		}

		public byte getValue() {
			return bPerformGuardMode;
		}
	}

	public enum Bank {
		Reserved((byte)0x00),
		EPC((byte)0x01),
		TID((byte)0x02),
		User((byte)0x03);
		
		private byte bBank;
		
		Bank(byte bBank) {
			this.bBank = bBank;
		}

		public byte getValue() {
			return bBank;
		}
	}

	public enum PwdPermissions {
		Accessible((byte)0x00),
		AlwaysAccessible((byte)0x01),
		PasswordAccessible((byte)0x02),
		AlwaysNotAccessible((byte)0x03),
		NoChange((byte)0x04);
		
		private byte bPwdPermissions;
		
		PwdPermissions(byte bPwdPermissions) {
			this.bPwdPermissions = bPwdPermissions;
		}

		public byte getValue() {
			return bPwdPermissions;
		}
	}

	public enum MemBankPermissions {
		Writeable((byte)0x00),
		AlwaysWriteable((byte)0x01),
		PasswordWriteable((byte)0x02),
		AlwaysNotWriteable((byte)0x03),
		NoChange((byte)0x04);
		
		private byte bMemBankPermissions;
		
		MemBankPermissions(byte bMemBankPermissions) {
			this.bMemBankPermissions = bMemBankPermissions;
		}

		public byte getValue() {
			return bMemBankPermissions;
		}
	}

	public enum AccessCommand {
		Read((byte)0xC2),
		Write((byte)0xC3),
		Kill((byte)0xC4),
		Lock((byte)0xC5),
		Access((byte)0xC6),
		BlockWrite((byte)0xC7),
		BlockErase((byte)0xC8);
		
		private byte bAccessCommand;
		
		AccessCommand(byte bAccessCommand) {
			this.bAccessCommand = bAccessCommand;
		}

		public byte getValue() {
			return bAccessCommand;
		}
	}

	public enum DataFormat {
		R1000((byte)0x00),
		R2000((byte)0x01);
		
		private byte bDataFormat;
		
		DataFormat(byte bDataFormat) {
			this.bDataFormat = bDataFormat;
		}

		public byte getValue() {
			return bDataFormat;
		}
	}

	/**
	 * RFID_18K6CTagInventory Object
	 * 
	 * <P>This class implements the RFID_18K6CTagInventory command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	public static final class RFID_18K6CTagInventory extends MtiCmd {
		int iStatus = 0x00;
		
		public RFID_18K6CTagInventory(){
			mCmdHead = CmdHead.RFID_18K6CTagInventory;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param select
		 * @param postMatch
		 * @param guardMode
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(PerformSelect select, PerformPostMatch postMatch, PerformGuardMode guardMode) {
			mParam.clear();
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			mParam.add(guardMode.bPerformGuardMode);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param select
		 * @param postMatch
		 * @param guardMode
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte select, byte postMatch, byte guardMode) {
			mParam.clear();
			mParam.add(select);
			mParam.add(postMatch);
			mParam.add(guardMode);
			return composeCmd();
		}
		
		/** 
		 * parseResponse
		 * <P>This method is called to copy the values returned on the serial port
		 * following an Inventory Response. This method can be called several times
		 * until the Command-End packet is seen.
		 * 
		 * @param response The response buffer.
		 * @param addSpace Add a space between EPC bytes.
		 * @param tagData A storage class to put all the parsed data.
		 */
		public static void parseResponse(byte[] response, boolean addSpace, TagData tagData) {
			tagData.crcValid = ((response[FLAGS_INDEX] & (byte)0x01) == (byte)0x00);
			if (tagData.crcValid) {
				byte pkt_relseq = response[REL_SEQ_INDEX];
				short rpt_inflen = getShort(response, LENGTH_INDEX);
				boolean r2000 = ((response[FLAGS_INDEX] & (byte)0x02) == (byte)0x02);
				boolean extraHwData = (r2000 && ((response[FLAGS_INDEX] & (byte)0x08) == (byte)0x08));
				// Index to where the Tag Data is
				int tagDataIndex = DATA_INDEX;
				if (pkt_relseq > (byte)0x01) {
					tagDataIndex = ALT_DATA_INDEX;
				} else {
					tagData.rssi = getShort(response, RSSI_INDEX);
				}
				// The rpt_inflen is always > 3 (i.e. 3 = 0)
				int infoLengthBytes = (rpt_inflen - 3) * 4;
				// The epc length = total length - PC length (2 bytes) - CRC16 length (2 bytes)
				int epcLengthBytes = infoLengthBytes - 4;
				// Extract extra HW data if available
				if (extraHwData) {
					tagData.antPort = response[tagDataIndex + PORT_REL_INDEX];
					// phase is an weird 7-bit signed number, we want to mask the sign bit
					tagData.phase = (byte) (response[tagDataIndex + PHAS_REL_INDEX] & 0x3F);
					tagData.temp = response[tagDataIndex + TEMP_REL_INDEX];
					tagData.freqKHz = getInt(response, tagDataIndex + FREQ_REL_INDEX);
					// Adjust these parameters for the presence of the extra HW data
					tagDataIndex = tagDataIndex + 8;
					epcLengthBytes = epcLengthBytes - 8;
				}
				try {
					tagData.epc =  byteArrayToString(response, tagDataIndex + 2, epcLengthBytes, addSpace);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(TAG + "ArrayIndexOutOfBoundsException");
				} catch (IllegalArgumentException e) {
					System.out.println(TAG + "IllegalArgumentException");				
				} catch (NullPointerException e) {
					System.out.println(TAG + "NullPointerException");
				}
			}
		}
	}

	
	/**
	 * RFID_18K6CTagRead Object
	 * 
	 * <P>This class implements the RFID_18K6CTagRead command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	public static final class RFID_18K6CTagRead extends MtiCmd {
		int iStatus = 0x00;
		
		public RFID_18K6CTagRead(){
			mCmdHead = CmdHead.RFID_18K6CTagRead;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param count
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Bank bank, short offset, byte count, byte retryCount, PerformSelect select, PerformPostMatch postMatch) {
			mParam.clear();
			mParam.add(bank.bBank);
			addParam(offset);
			mParam.add(count);
			mParam.add(retryCount);
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param count
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bank, short offset, byte count, byte retryCount, byte select, byte postMatch) {
			mParam.clear();
			mParam.add(bank);
			addParam(offset);
			mParam.add(count);
			mParam.add(retryCount);
			mParam.add(select);
			mParam.add(postMatch);
			return composeCmd();
		}
		
		/** 
		 * parseAccessPacket
		 * <P>This method is called to copy the values returned on the serial port
		 * following a Tag Access Packet. This method can be called several times
		 * until the Command-End packet is seen.
		 * 
		 * @param response The response buffer.
		 * @param addSpace Add a space between bytes.
		 * @return data A string representing the data read
		 */
		public static String parseAccessPacket(byte[] response, boolean addSpace) {
			String data = null;
			boolean noError = ((response[FLAGS_INDEX] & (byte)0x01) == (byte)0x00);
			if (noError) {
				byte pkt_relseq = response[REL_SEQ_INDEX];
				short rpt_inflen = getShort(response, LENGTH_INDEX);
				// Index to where the Data is
				int tagDataIndex = DATA_INDEX;
				if (pkt_relseq > (byte)0x01) {
					tagDataIndex = ALT_DATA_INDEX;
				}
				// The rpt_inflen is always > 3 (i.e. 3 = 0)
				int infoLengthBytes = (rpt_inflen - 3) * 4;
				try {
					data =  byteArrayToString(response, tagDataIndex, infoLengthBytes, addSpace);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println(TAG + "ArrayIndexOutOfBoundsException");
				} catch (IllegalArgumentException e) {
					System.out.println(TAG + "IllegalArgumentException");				
				} catch (NullPointerException e) {
					System.out.println(TAG + "NullPointerException");
				}
			}
			return data;
		}

		/** 
		 * readData
		 * 
		 * This method is called to copy the tag data returned on the serial port
		 * following a Tag Access command. This method can be called several times
		 * until the Command-End packet is seen.
		 * 
		 * @param response The response buffer.
		 * @return Tag data
		 */
		public String readData(byte[] response) {
			short sDataLength = (short)((getShort(response, LENGTH_INDEX) - 3) * 4);
			try {
				byte[] subResponse = Arrays.copyOfRange(response, 26, 26 + sDataLength);
				return byteArrayToString(subResponse, sDataLength, true);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println(TAG + "ArrayIndexOutOfBoundsException");
			} catch (IllegalArgumentException e) {
				System.out.println(TAG + "IllegalArgumentException");				
			} catch (NullPointerException e) {
				System.out.println(TAG + "NullPointerException");
			}
			return null;
		}
	}
	
	
	/**
	 * RFID_18K6CTagWrite Object
	 * 
	 * <P>This class implements the RFID_18K6CTagWrite command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CTagWrite extends MtiCmd {
		public RFID_18K6CTagWrite(){
			mCmdHead = CmdHead.RFID_18K6CTagWrite;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param data
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Bank bank, short offset, short data, byte retryCount, PerformSelect select, PerformPostMatch postMatch) {
			mParam.clear();
			mParam.add(bank.bBank);
			addParam(offset);
			addParam(data);
			mParam.add(retryCount);
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param data
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bank, short offset, short data, byte retryCount, byte select, byte postMatch) {
			mParam.clear();
			mParam.add(bank);
			addParam(offset);
			addParam(data);
			mParam.add(retryCount);
			mParam.add(select);
			mParam.add(postMatch);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_18K6CTagKill Object
	 * 
	 * <P>This class implements the RFID_18K6CTagKill command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CTagKill extends MtiCmd {
		public RFID_18K6CTagKill(){
			mCmdHead = CmdHead.RFID_18K6CTagKill;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param killPwd
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(int killPwd, byte retryCount, PerformSelect select, PerformPostMatch postMatch) {
			mParam.clear();
			addParam(killPwd);
			mParam.add(retryCount);
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param killPwd
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(int killPwd, byte retryCount, byte select, byte postMatch) {
			mParam.clear();
			addParam(killPwd);
			mParam.add(retryCount);
			mParam.add(select);
			mParam.add(postMatch);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_18K6CTagLock Object
	 * 
	 * <P>This class implements the RFID_18K6CTagLock command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CTagLock extends MtiCmd {
		public RFID_18K6CTagLock(){
			mCmdHead = CmdHead.RFID_18K6CTagLock;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param killPwd
		 * @param accessPwd
		 * @param epcMemBank
		 * @param tidMemBank
		 * @param userMemBank
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(PwdPermissions killPwd, PwdPermissions accessPwd,
				MemBankPermissions epcMemBank, MemBankPermissions tidMemBank, MemBankPermissions userMemBank,
				byte retryCount, PerformSelect select, PerformPostMatch postMatch) {
			mParam.clear();
			mParam.add(killPwd.bPwdPermissions);
			mParam.add(accessPwd.bPwdPermissions);
			mParam.add(epcMemBank.bMemBankPermissions);
			mParam.add(tidMemBank.bMemBankPermissions);
			mParam.add(userMemBank.bMemBankPermissions);
			mParam.add(retryCount);
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param killPwd
		 * @param accessPwd
		 * @param epcMemBank
		 * @param tidMemBank
		 * @param userMemBank
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte killPwd, byte accessPwd, byte epcMemBank, byte tidMemBank, byte userMemBank,
				byte retryCount, byte select, byte postMatch) {
			mParam.clear();
			mParam.add(killPwd);
			mParam.add(accessPwd);
			mParam.add(epcMemBank);
			mParam.add(tidMemBank);
			mParam.add(userMemBank);
			mParam.add(retryCount);
			mParam.add(select);
			mParam.add(postMatch);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_18K6CTagMultipleWrite Object
	 * 
	 * <P>This class implements the RFID_18K6CTagMultipleWrite command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CTagMultipleWrite extends MtiCmd {
		public RFID_18K6CTagMultipleWrite(){
			mCmdHead = CmdHead.RFID_18K6CTagMultipleWrite;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param dataLength
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Bank bank, short offset, byte dataLength, byte retryCount,
				PerformSelect select, PerformPostMatch postMatch) {
			mParam.clear();
			mParam.add(bank.bBank);
			addParam(offset);
			mParam.add(dataLength);
			addParam((byte)0x0);
			mParam.add(retryCount);
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param dataLength
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bank, short offset, byte dataLength, byte retryCount, byte select, byte postMatch) {
			mParam.clear();
			mParam.add(bank);
			addParam(offset);
			mParam.add(dataLength);
			mParam.add((byte)0x0);
			mParam.add(retryCount);
			mParam.add(select);
			mParam.add(postMatch);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_18K6CTagBlockWrite Object
	 * 
	 * <P>This class implements the RFID_18K6CTagBlockWrite command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CTagBlockWrite extends MtiCmd {
		public RFID_18K6CTagBlockWrite(){
			mCmdHead = CmdHead.RFID_18K6CTagBlockWrite;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param dataLength
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Bank bank, short offset, byte dataLength, byte retryCount,
				PerformSelect select, PerformPostMatch postMatch) {
			mParam.clear();
			mParam.add(bank.bBank);
			addParam(offset);
			mParam.add(dataLength);
			addParam((byte)0x0);
			mParam.add(retryCount);
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param dataLength
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bank, short offset, byte dataLength, byte retryCount, byte select, byte postMatch) {
			mParam.clear();
			mParam.add(bank);
			addParam(offset);
			mParam.add(dataLength);
			mParam.add((byte)0x0);
			mParam.add(retryCount);
			mParam.add(select);
			mParam.add(postMatch);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_18K6CTagBlockErase Object
	 * 
	 * <P>This class implements the RFID_18K6CTagBlockErase command as defined
	 * in section 4.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CTagBlockErase extends MtiCmd {
		public RFID_18K6CTagBlockErase(){
			mCmdHead = CmdHead.RFID_18K6CTagBlockErase;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param dataLength
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Bank bank, short offset, byte dataLength, byte retryCount,
				PerformSelect select, PerformPostMatch postMatch) {
			mParam.clear();
			mParam.add(bank.bBank);
			addParam(offset);
			mParam.add(dataLength);
			addParam((byte)0x0);
			mParam.add(retryCount);
			mParam.add(select.bPerformSelect);
			mParam.add(postMatch.bPerformPostMatch);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bank
		 * @param offset
		 * @param dataLength
		 * @param retryCount
		 * @param select
		 * @param postMatch
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bank, short offset, byte dataLength, byte retryCount, byte select, byte postMatch) {
			mParam.clear();
			mParam.add(bank);
			addParam(offset);
			mParam.add(dataLength);
			mParam.add((byte)0x0);
			mParam.add(retryCount);
			mParam.add(select);
			mParam.add(postMatch);
			return composeCmd();
		}
	}
}
