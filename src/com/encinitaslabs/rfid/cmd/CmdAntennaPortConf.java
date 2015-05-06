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
 * CmdAntennaPortConf Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the Antenna Port Configuration commands (section 4.2)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdAntennaPortConf {
	
	public enum State {
		Disabled((byte)0x00),
		Enabled((byte)0x01);
		
		private byte bState;
		
		State(byte bState) {
			this.bState = bState;
		}

		public byte getValue() {
			return bState;
		}
	}
	
	
	/**
	 * RFID_AntennaPortSetState Object
	 * 
	 * <P>This class implements the RFID_AntennaPortSetState command as defined
	 * in section 4.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_AntennaPortSetState extends MtiCmd {
		public RFID_AntennaPortSetState(){
			mCmdHead = CmdHead.RFID_AntennaPortSetState;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param antennaPort (0 or 1)
		 * @param state (Disabled or Enabled)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte antennaPort, State state) {
			mParam.clear();
			mParam.add(antennaPort);
			mParam.add(state.bState);
			return composeCmd();
		}
		
		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param antennaPort (0 or 1)
		 * @param state (0 or 1)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte antennaPort, byte state) {
			mParam.clear();
			mParam.add(antennaPort);
			mParam.add(state);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_AntennaPortGetState Object
	 * 
	 * <P>This class implements the RFID_AntennaPortGetState command as defined
	 * in section 4.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_AntennaPortGetState extends MtiCmd {
		public RFID_AntennaPortGetState() {
			mCmdHead = CmdHead.RFID_AntennaPortGetState;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param antenna port (0 or 1)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte antennaPort) {
			mParam.clear();
			mParam.add(antennaPort);
			return composeCmd();
		}
		
		/** 
		 * getDeviceId
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Antenna State
		 */
		public byte getAntennaState(byte[] response) {
			return response[STATUS_POS+1];
		}
	}
	

	/**
	 * RFID_AntennaPortSetConfiguration Object
	 * 
	 * <P>This class implements the RFID_AntennaPortSetConfiguration command as defined
	 * in section 4.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_AntennaPortSetConfiguration extends MtiCmd {
		public RFID_AntennaPortSetConfiguration(){
			mCmdHead = CmdHead.RFID_AntennaPortSetConfiguration;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param antennaPort (0 or 1)
		 * @param powerLevel
		 * @param dwellTime
		 * @param numberInventoryCycles
		 * @param physicalPort (0 or 1)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte antennaPort, short powerLevel, short dwellTime, short numberInventoryCycles, byte physicalPort) {
			mParam.clear();
			mParam.add(antennaPort);
			addParam(powerLevel);
			addParam(dwellTime);
			addParam(numberInventoryCycles);
			mParam.add(physicalPort);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_AntennaPortGetConfiguration Object
	 * 
	 * <P>This class implements the RFID_AntennaPortGetConfiguration command as defined
	 * in section 4.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_AntennaPortGetConfiguration extends MtiCmd {
		public RFID_AntennaPortGetConfiguration() {
			mCmdHead = CmdHead.RFID_AntennaPortGetConfiguration;
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param antennaPort
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte antennaPort) {
			mParam.clear();
			mParam.add(antennaPort);
			return composeCmd();
		}
		
		/** 
		 * getPowerLevel
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Power Level
		 */
		public short getPowerLevel(byte[] response) {
			return getShort(response, STATUS_POS + 1);
		}
		
		/** 
		 * getDwellTime
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Dwell Time
		 */
		public short getDwellTime(byte[] response) {
			return getShort(response, STATUS_POS + 3);
		}
		
		/** 
		 * getNumberInventoryCycles
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Number of Inventory Cycles
		 */
		public short getNumberInventoryCycles(byte[] response) {
			return getShort(response, STATUS_POS + 5);
		}
	}
	

	/**
	 * RFID_AntennaPortSetSenseThreshold Object
	 * 
	 * <P>This class implements the RFID_AntennaPortSetSenseThreshold command as defined
	 * in section 4.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_AntennaPortSetSenseThreshold extends MtiCmd {
		public RFID_AntennaPortSetSenseThreshold(){
			mCmdHead = CmdHead.RFID_AntennaPortSetSenseThreshold;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param antennaSenseThreshold
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(int antennaSenseThreshold) {
			mParam.clear();
			addParam(antennaSenseThreshold);
			return composeCmd();
		}
	}
	
	
	/**
	 * RFID_AntennaPortGetSenseThreshold Object
	 * 
	 * <P>This class implements the RFID_AntennaPortGetSenseThreshold command as defined
	 * in section 4.2 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_AntennaPortGetSenseThreshold extends MtiCmd {
		public RFID_AntennaPortGetSenseThreshold(){
			mCmdHead = CmdHead.RFID_AntennaPortGetSenseThreshold;
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
		 * getSenseThreshold
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Sense Threshold
		 */
		public int getSenseThreshold(byte[] response) {
			return getInt(response, 1);
		}
	}
}
