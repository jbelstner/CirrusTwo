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
 * CmdReaderModuleControl Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the RFID Reader/Module Control Operation commands (section 4.5)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdReaderModuleControl {

	public enum PowerState {
		Full((byte)0x00),
		Standby((byte)0x01);
		
		private byte bPowerState;
		
		PowerState(byte bPowerState) {
			this.bPowerState = bPowerState;
		}

		public byte getValue() {
			return bPowerState;
		}
	}

	
	/**
	 * RFID_ControlCancel Object
	 * 
	 * <P>This class implements the RFID_ControlCancel command as defined
	 * in section 4.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_ControlCancel extends MtiCmd {
		public RFID_ControlCancel(){
			mCmdHead = CmdHead.RFID_ControlCancel;
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
	 * RFID_ControlPause Object
	 * 
	 * <P>This class implements the RFID_ControlPause command as defined
	 * in section 4.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_ControlPause extends MtiCmd {
		public RFID_ControlPause(){
			mCmdHead = CmdHead.RFID_ControlPause;
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
	 * RFID_ControlResume Object
	 * 
	 * <P>This class implements the RFID_ControlResume command as defined
	 * in section 4.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_ControlResume extends MtiCmd {
		public RFID_ControlResume(){
			mCmdHead = CmdHead.RFID_ControlResume;
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
	 * RFID_ControlSoftReset Object
	 * 
	 * <P>This class implements the RFID_ControlSoftReset command as defined
	 * in section 4.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_ControlSoftReset extends MtiCmd {
		public RFID_ControlSoftReset(){
			mCmdHead = CmdHead.RFID_ControlSoftReset;
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
	 * RFID_ControlResetToBootloader Object
	 * 
	 * <P>This class implements the RFID_ControlResetToBootloader command as defined
	 * in section 4.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_ControlResetToBootloader extends MtiCmd {
		public RFID_ControlResetToBootloader(){
			mCmdHead = CmdHead.RFID_ControlResetToBootloader;
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
	 * RFID_ControlSetPowerState Object
	 * 
	 * <P>This class implements the RFID_ControlSetPowerState command as defined
	 * in section 4.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_ControlSetPowerState extends MtiCmd {
		public RFID_ControlSetPowerState(){
			mCmdHead = CmdHead.RFID_ControlSetPowerState;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param powerState
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(PowerState powerState) {
			mParam.clear();
			mParam.add(powerState.bPowerState);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param powerState
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte powerState) {
			mParam.clear();
			mParam.add(powerState);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_ControlGetPowerState Object
	 * 
	 * <P>This class implements the RFID_ControlGetPowerState command as defined
	 * in section 4.5 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_ControlGetPowerState extends MtiCmd {
		public RFID_ControlGetPowerState(){
			mCmdHead = CmdHead.RFID_ControlGetPowerState;
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
