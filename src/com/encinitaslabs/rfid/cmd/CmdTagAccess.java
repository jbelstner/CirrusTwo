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

/**
 * CmdTagAccess Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the ISO 18000-6C Tag Access Parameters commands (section 4.3.3/4)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdTagAccess {

	public enum Selected {
		Any((byte)0x00),
		Deasserted((byte)0x02),
		Asserted((byte)0x03);
		
		private byte bSelected;
		
		Selected(byte bSelected) {
			this.bSelected = bSelected;
		}

		public byte getValue() {
			return bSelected;
		}
	}

	public enum Session {
		S0((byte)0x00),
		S1((byte)0x01),
		S2((byte)0x02),
		S3((byte)0x03),
		SL((byte)0x04);
		
		private byte bSession;
		
		Session(byte bSession) {
			this.bSession = bSession;
		}

		public byte getValue() {
			return bSession;
		}
	}

	public enum Target {
		A((byte)0x00),
		B((byte)0x01);
		
		private byte bTarget;
		
		Target(byte bTarget) {
			this.bTarget = bTarget;
		}

		public byte getValue() {
			return bTarget;
		}
	}

	public enum Algorithm {
		FixedQ((byte)0x00),
		DynamicQ((byte)0x01);
		
		private byte bAlgorithm;
		
		Algorithm(byte bAlgorithm) {
			this.bAlgorithm = bAlgorithm;
		}

		public byte getValue() {
			return bAlgorithm;
		}
	}

	public enum ToggleTarget {
		No((byte)0x00),
		Yes((byte)0x01);
		
		private byte bToggleTarget;
		
		ToggleTarget(byte bToggleTarget) {
			this.bToggleTarget = bToggleTarget;
		}

		public byte getValue() {
			return bToggleTarget;
		}
	}

	public enum RepeatUntilNoTags {
		No((byte)0x00),
		Yes((byte)0x01);
		
		private byte bRepeatUntilNoTags;
		
		RepeatUntilNoTags(byte bRepeatUntilNoTags) {
			this.bRepeatUntilNoTags = bRepeatUntilNoTags;
		}

		public byte getValue() {
			return bRepeatUntilNoTags;
		}
	}

	
	/**
	 * RFID_18K6CSetQueryTagGroup Object
	 * 
	 * <P>This class implements the RFID_18K6CSetQueryTagGroup command as defined
	 * in section 4.3.3 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetQueryTagGroup extends MtiCmd {
		public RFID_18K6CSetQueryTagGroup(){
			mCmdHead = CmdHead.RFID_18K6CSetQueryTagGroup;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param selected
		 * @param session
		 * @param target
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Selected selected, Session session, Target target) {
			mParam.clear();
			mParam.add(selected.bSelected);
			mParam.add(session.bSession);
			mParam.add(target.bTarget);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param selected
		 * @param session
		 * @param target
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte selected, byte session, byte target) {
			mParam.clear();
			mParam.add(selected);
			mParam.add(session);
			mParam.add(target);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetQueryTagGroup Object
	 * 
	 * <P>This class implements the RFID_18K6CGetQueryTagGroup command as defined
	 * in section 4.3.3 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetQueryTagGroup extends MtiCmd {
		public RFID_18K6CGetQueryTagGroup(){
			mCmdHead = CmdHead.RFID_18K6CGetQueryTagGroup;
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
		
		public byte getSelectedFlag(byte[] response) {
			return response[STATUS_POS+1];
		}
		
		public short getSession(byte[] response) {
			return response[STATUS_POS+2];
		}
		
		public short getTarget(byte[] response) {
			return response[STATUS_POS+3];
		}
	}

	
	/**
	 * RFID_18K6CSetCurrentSingulationAlgorithm Object
	 * 
	 * <P>This class implements the RFID_18K6CSetCurrentSingulationAlgorithm command as defined
	 * in section 4.3.3 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetCurrentSingulationAlgorithm extends MtiCmd {
		public RFID_18K6CSetCurrentSingulationAlgorithm(){
			mCmdHead = CmdHead.RFID_18K6CSetCurrentSingulationAlgorithm;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param Algorithm algorithm
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Algorithm algorithm) {
			mParam.clear();
			mParam.add(algorithm.bAlgorithm);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param algorithm
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte algorithm) {
			mParam.clear();
			mParam.add(algorithm);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetCurrentSingulationAlgorithm Object
	 * 
	 * <P>This class implements the RFID_18K6CGetCurrentSingulationAlgorithm command as defined
	 * in section 4.3.3 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetCurrentSingulationAlgorithm extends MtiCmd {
		public RFID_18K6CGetCurrentSingulationAlgorithm(){
			mCmdHead = CmdHead.RFID_18K6CGetCurrentSingulationAlgorithm;
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
		
		public byte getSingulationAlgorithm(byte[] response) {
			return response[STATUS_POS+1];
		}
	}

	
	/**
	 * RFID_18K6CSetCurrentSingulationAlgorithmParameters Object
	 * 
	 * <P>This class implements the RFID_18K6CSetCurrentSingulationAlgorithmParameters command as defined
	 * in section 4.3.3 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetCurrentSingulationAlgorithmParameters extends MtiCmd {
		public RFID_18K6CSetCurrentSingulationAlgorithmParameters(){
			mCmdHead = CmdHead.RFID_18K6CSetCurrentSingulationAlgorithmParameters;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param algorithm
		 * @param qValue
		 * @param retryCount
		 * @param toggleTarget
		 * @param repeatUntilNoTags
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Algorithm algorithm, byte qValue, byte retryCount, ToggleTarget toggleTarget, RepeatUntilNoTags repeatUntilNoTags) {
			mParam.clear();
			mParam.add(algorithm.bAlgorithm);
			mParam.add(qValue);
			mParam.add(retryCount);
			mParam.add(toggleTarget.bToggleTarget);
			mParam.add(repeatUntilNoTags.bRepeatUntilNoTags);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param algorithm
		 * @param qValue
		 * @param retryCount
		 * @param toggleTarget
		 * @param repeatUntilNoTags
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte algorithm, byte qValue, byte retryCount, byte toggleTarget, byte repeatUntilNoTags) {
			mParam.clear();
			mParam.add(algorithm);
			mParam.add(qValue);
			mParam.add(retryCount);
			mParam.add(toggleTarget);
			mParam.add(repeatUntilNoTags);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param algorithm
		 * @param startQvalue
		 * @param minQvalue
		 * @param maxQvaule
		 * @param retryCount
		 * @param toggleTarget
		 * @param thresholdMultiplier
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Algorithm algorithm, byte startQvalue, byte minQvalue, byte maxQvalue, byte retryCount, ToggleTarget toggleTarget, byte thresholdMultiplier) {
			mParam.clear();
			mParam.add(algorithm.bAlgorithm);
			mParam.add(startQvalue);
			mParam.add(minQvalue);
			mParam.add(maxQvalue);
			mParam.add(retryCount);
			mParam.add(toggleTarget.bToggleTarget);
			mParam.add(thresholdMultiplier);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param algorithm
		 * @param startQvalue
		 * @param minQvalue
		 * @param maxQvaule
		 * @param retryCount
		 * @param toggleTarget
		 * @param thresholdMultiplier
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte algorithm, byte startQvalue, byte minQvalue, byte maxQvalue, byte retryCount, byte toggleTarget, byte thresholdMultiplier) {
			mParam.clear();
			mParam.add(algorithm);
			mParam.add(startQvalue);
			mParam.add(minQvalue);
			mParam.add(maxQvalue);
			mParam.add(retryCount);
			mParam.add(toggleTarget);
			mParam.add(thresholdMultiplier);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetCurrentSingulationAlgorithmParameters Object
	 * 
	 * <P>This class implements the RFID_18K6CGetCurrentSingulationAlgorithmParameters command as defined
	 * in section 4.3.3 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetCurrentSingulationAlgorithmParameters extends MtiCmd {
		public RFID_18K6CGetCurrentSingulationAlgorithmParameters(){
			mCmdHead = CmdHead.RFID_18K6CGetCurrentSingulationAlgorithmParameters;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param Algorithm algorithm
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Algorithm algorithm) {
			mParam.clear();
			mParam.add(algorithm.bAlgorithm);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param algorithm
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte algorithm) {
			mParam.clear();
			mParam.add(algorithm);
			return composeCmd();
		}
		
		public byte getQValue(byte[] response) {
			return response[STATUS_POS+1];
		}
		
		public byte getFixedQRetryCount(byte[] response) {
			return response[STATUS_POS+2];
		}
		
		public byte getFixedQToggleTarget(byte[] response) {
			return response[STATUS_POS+3];
		}
		
		public byte getRepeatUntilNoTags(byte[] response) {
			return response[STATUS_POS+4];
		}
		
		public byte getStartQValue(byte[] response) {
			return response[STATUS_POS+1];
		}
		
		public byte getMinQValue(byte[] response) {
			return response[STATUS_POS+2];
		}
		
		public byte getMaxQValue(byte[] response) {
			return response[STATUS_POS+3];
		}
		
		public byte getDynamicQRetryCount(byte[] response) {
			return response[STATUS_POS+4];
		}
		
		public byte getDynamicQToggleTarget(byte[] response) {
			return response[STATUS_POS+5];
		}
		
		public byte getThresholdMultiPlier(byte[] response) {
			return response[STATUS_POS+6];
		}
	}

	
	/**
	 * RFID_18K6CSetTagAccessPassword Object
	 * 
	 * <P>This class implements the RFID_18K6CSetTagAccessPassword command as defined
	 * in section 4.3.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetTagAccessPassword extends MtiCmd {
		public RFID_18K6CSetTagAccessPassword(){
			mCmdHead = CmdHead.RFID_18K6CSetTagAccessPassword;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param password
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(int password) {
			mParam.clear();
			addParam(password);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetTagAccessPassword Object
	 * 
	 * <P>This class implements the RFID_18K6CGetTagAccessPassword command as defined
	 * in section 4.3.4 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetTagAccessPassword extends MtiCmd {
		public RFID_18K6CGetTagAccessPassword(){
			mCmdHead = CmdHead.RFID_18K6CGetTagAccessPassword;
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
		
		public String getPassword(byte[] response) {
			byte[] password = Arrays.copyOfRange(response, STATUS_POS + 1, STATUS_POS + 5);

			return byteArrayToString(password, false);
		}
	}

	
	/**
	 * RFID_18K6CSetTagWriteDataBuffer Object
	 * 
	 * <P>This class implements the RFID_18K6CSetTagWriteDataBuffer command as defined
	 * in section 4.3.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetTagWriteDataBuffer extends MtiCmd {
		public RFID_18K6CSetTagWriteDataBuffer(){
			mCmdHead = CmdHead.RFID_18K6CSetTagWriteDataBuffer;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bufferIndex
		 * @param bufferData
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bufferIndex,short bufferData) {
			mParam.clear();
			mParam.add(bufferIndex);
			addParam(bufferData);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetTagWriteDataBuffer Object
	 * 
	 * <P>This class implements the RFID_18K6CGetTagWriteDataBuffer command as defined
	 * in section 4.3.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetTagWriteDataBuffer extends MtiCmd {
		public RFID_18K6CGetTagWriteDataBuffer(){
			mCmdHead = CmdHead.RFID_18K6CGetTagWriteDataBuffer;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bufferIndex
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bufferIndex) {
			mParam.clear();
			mParam.add(bufferIndex);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetGuardBufferTagNum Object
	 * 
	 * <P>This class implements the RFID_18K6CGetGuardBufferTagNum command as defined
	 * in section 4.3.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	public static final class RFID_18K6CGetGuardBufferTagNum extends MtiCmd {
		public RFID_18K6CGetGuardBufferTagNum(){
			mCmdHead = CmdHead.RFID_18K6CGetGuardBufferTagNum;
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
		 * getNumTags
		 * 
		 * This method parses a RFID_18K6CGetGuardBufferTagNum response buffer
		 * and returns the number of tags stored in the guard buffer.
		 * @param response The response buffer.
		 * @return A short corresponding to the number of tags stored
		 */
		public static short getNumTags(byte[] response) {
			return (short) getShort(response, RESP_DATA_INDEX + NUM_TAGS_INDEX);
		}
	}

	
	/**
	 * RFID_18K6CGetGuardBufferTagInfo Object
	 * 
	 * <P>This class implements the RFID_18K6CGetGuardBufferTagInfo command as defined
	 * in section 4.3.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetGuardBufferTagInfo extends MtiCmd {
		public RFID_18K6CGetGuardBufferTagInfo(){
			mCmdHead = CmdHead.RFID_18K6CGetGuardBufferTagInfo;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param bufferIndex
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte bufferIndex) {
			mParam.clear();
			mParam.add((byte)0);
			mParam.add(bufferIndex);
			return composeCmd();
		}
	}
}
