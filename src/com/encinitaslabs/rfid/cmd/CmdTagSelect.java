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
 * CmdTagSelect Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the ISO 18000-6C Tag Select Operation commands (section 4.3.1/2)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdTagSelect {
	
	public enum ActiveState {
		Disabled((byte)0x00),
		Enabled((byte)0x01);
		
		private byte bActiveState;
		
		ActiveState(byte bActiveState) {
			this.bActiveState = bActiveState;
		}

		public byte getValue() {
			return bActiveState;
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

	public enum Target {
		S0((byte)0x00),
		S1((byte)0x01),
		S2((byte)0x02),
		S3((byte)0x03),
		Selected((byte)0x05);
		
		private byte bTarget;
		
		Target(byte bTarget) {
			this.bTarget = bTarget;
		}

		public byte getValue() {
			return bTarget;
		}
	}
	
	public enum Truncation {
		Disable((byte)0x00),
		Enable((byte)0x01);
		
		private byte bTruncation;
		
		Truncation(byte bTruncation) {
			this.bTruncation = bTruncation;
		}

		public byte getValue() {
			return bTruncation;
		}
	}
	
	public enum Match {
		Exclude((byte)0x00),
		Include((byte)0x01);
		
		private byte bMatch;
		
		Match(byte bMatch) {
			this.bMatch = bMatch;
		}

		public byte getValue() {
			return bMatch;
		}
	}

	
	/**
	 * RFID_18K6CSetActiveSelectCriteria Object
	 * 
	 * <P>This class implements the RFID_18K6CSetActiveSelectCriteria command as defined
	 * in section 4.3.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetActiveSelectCriteria extends MtiCmd {
		public RFID_18K6CSetActiveSelectCriteria(){
			mCmdHead = CmdHead.RFID_18K6CSetActiveSelectCriteria;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * @param activeState (Disable or Enable)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex, ActiveState activeState) {
			mParam.clear();
			mParam.add(criteriaIndex);
			mParam.add(activeState.bActiveState);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * @param activeState (0 or 1)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex, byte activeState) {
			mParam.clear();
			mParam.add(criteriaIndex);
			mParam.add(activeState);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetActiveSelectCriteria Object
	 * 
	 * <P>This class implements the RFID_18K6CGetActiveSelectCriteria command as defined
	 * in section 4.3.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetActiveSelectCriteria extends MtiCmd {
		public RFID_18K6CGetActiveSelectCriteria(){
			mCmdHead = CmdHead.RFID_18K6CGetActiveSelectCriteria;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex) {
			mParam.clear();
			mParam.add(criteriaIndex);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CSetSelectCriteria Object
	 * 
	 * <P>This class implements the RFID_18K6CSetSelectCriteria command as defined
	 * in section 4.3.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetSelectCriteria extends MtiCmd {
		public RFID_18K6CSetSelectCriteria(){
			mCmdHead = CmdHead.RFID_18K6CSetSelectCriteria;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * @param bank (Reserved, EPC, TID, User)
		 * @param short offset
		 * @param count
		 * @param target (S0, S1, S2, S3, S4, Selected)
		 * @param action
		 * @param truncation (Disable, Enable)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex, Bank bank, short offset, byte count,
				Target target, byte action, Truncation truncation) {
			mParam.clear();
			mParam.add(criteriaIndex);
			mParam.add(bank.bBank);
			addParam(offset);
			mParam.add(count);
			mParam.add(target.bTarget);
			mParam.add(action);
			mParam.add(truncation.bTruncation);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * @param bank (0, 1, 2, 3)
		 * @param offset
		 * @param count
		 * @param target (0, 1, 2, 3, 4, 5)
		 * @param action
		 * @param truncation (0, 1)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex, byte bank, short offset, byte count,
				byte target, byte action, byte truncation) {
			mParam.clear();
			mParam.add(criteriaIndex);
			mParam.add(bank);
			addParam(offset);
			mParam.add(count);
			mParam.add(target);
			mParam.add(action);
			mParam.add(truncation);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetSelectCriteria Object
	 * 
	 * <P>This class implements the RFID_18K6CGetSelectCriteria command as defined
	 * in section 4.3.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetSelectCriteria extends MtiCmd {
		public RFID_18K6CGetSelectCriteria(){
			mCmdHead = CmdHead.RFID_18K6CGetSelectCriteria;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex) {
			mParam.clear();
			mParam.add(criteriaIndex);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CSetSelectMaskData Object
	 * 
	 * <P>This class implements the RFID_18K6CSetSelectMaskData command as defined
	 * in section 4.3.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetSelectMaskData extends MtiCmd {
		public RFID_18K6CSetSelectMaskData(){
			mCmdHead = CmdHead.RFID_18K6CSetSelectMaskData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * @param maskIndex
		 * @param maskData0
		 * @param maskData1
		 * @param maskData2
		 * @param maskData3
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex, byte maskIndex, byte maskData0,
				byte maskData1, byte maskData2, byte maskData3) {
			mParam.clear();
			mParam.add(criteriaIndex);
			mParam.add(maskIndex);
			mParam.add(maskData0);
			mParam.add(maskData1);
			mParam.add(maskData2);
			mParam.add(maskData3);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetSelectMaskData Object
	 * 
	 * <P>This class implements the RFID_18K6CGetSelectMaskData command as defined
	 * in section 4.3.1 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetSelectMaskData extends MtiCmd {
		public RFID_18K6CGetSelectMaskData(){
			mCmdHead = CmdHead.RFID_18K6CGetSelectMaskData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param criteriaIndex
		 * @param maskIndex
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte criteriaIndex, byte maskIndex) {
			mParam.clear();
			mParam.add(criteriaIndex);
			mParam.add(maskIndex);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CSetPostMatchCriteria Object
	 * 
	 * <P>This class implements the RFID_18K6CSetPostMatchCriteria command as defined
	 * in section 4.3.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetPostMatchCriteria extends MtiCmd {
		public RFID_18K6CSetPostMatchCriteria(){
			mCmdHead = CmdHead.RFID_18K6CSetPostMatchCriteria;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param match
		 * @param offset
		 * @param count
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Match match, short offset, short count) {
			mParam.clear();
			mParam.add(match.bMatch);
			addParam(offset);
			addParam(count);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param match
		 * @param offset
		 * @param count
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte match, short offset, short count) {
			mParam.clear();
			mParam.add(match);
			addParam(offset);
			addParam(count);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetPostMatchCriteria Object
	 * 
	 * <P>This class implements the RFID_18K6CGetPostMatchCriteria command as defined
	 * in section 4.3.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetPostMatchCriteria extends MtiCmd {
		public RFID_18K6CGetPostMatchCriteria(){
			mCmdHead = CmdHead.RFID_18K6CGetPostMatchCriteria;
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
	 * RFID_18K6CSetPostMatchMaskData Object
	 * 
	 * <P>This class implements the RFID_18K6CSetPostMatchMaskData command as defined
	 * in section 4.3.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CSetPostMatchMaskData extends MtiCmd {
		public RFID_18K6CSetPostMatchMaskData(){
			mCmdHead = CmdHead.RFID_18K6CSetPostMatchMaskData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param maskIndex
		 * @param maskData0
		 * @param maskData1
		 * @param maskData2
		 * @param maskData3
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte maskIndex, byte maskData0, byte maskData1, byte maskData2, byte maskData3) {
			mParam.clear();
			mParam.add(maskIndex);
			mParam.add(maskData0);
			mParam.add(maskData1);
			mParam.add(maskData2);
			mParam.add(maskData3);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_18K6CGetPostMatchMaskData Object
	 * 
	 * <P>This class implements the RFID_18K6CGetPostMatchMaskData command as defined
	 * in section 4.3.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_18K6CGetPostMatchMaskData extends MtiCmd {
		public RFID_18K6CGetPostMatchMaskData(){
			mCmdHead = CmdHead.RFID_18K6CGetPostMatchMaskData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param maskIndex
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte maskIndex) {
			mParam.clear();
			mParam.add(maskIndex);
			return composeCmd();
		}
	}
	
}
