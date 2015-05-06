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
 * CmdReaderModuleTestSupport Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the RFID Reader/Module Region Test Support commands (section 4.8)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdReaderModuleTestSupport {

	public enum PhysicalPort {
		Transmit((byte)0x00),
		Receive((byte)0x01);
		
		private byte bPhysicalPort;
		
		PhysicalPort(byte bPhysicalPort) {
			this.bPhysicalPort = bPhysicalPort;
		}

		public byte getValue() {
			return bPhysicalPort;
		}
	}

	public enum ChannelFlag {
		RegionOperation((byte)0x00),
		SingleChannel((byte)0x01);
		
		private byte bChannelFlag;
		
		ChannelFlag(byte bChannelFlag) {
			this.bChannelFlag = bChannelFlag;
		}

		public byte getValue() {
			return bChannelFlag;
		}
	}

	public enum ContinuousOperation {
		Disabled((byte)0x00),
		Enabled((byte)0x01);
		
		private byte bContinuousOperation;
		
		ContinuousOperation(byte bContinuousOperation) {
			this.bContinuousOperation = bContinuousOperation;
		}

		public byte getValue() {
			return bContinuousOperation;
		}
	}

	public enum Control {
		Continuous((byte)0x00),
		Pulsing((byte)0x01);
		
		private byte bControl;
		
		Control(byte bControl) {
			this.bControl = bControl;
		}

		public byte getValue() {
			return bControl;
		}
	}

	public enum TempSensor {
		PA((byte)0x00),
		Ambient((byte)0x01);
		
		private byte bTempSensor;
		
		TempSensor(byte bTempSensor) {
			this.bTempSensor = bTempSensor;
		}

		public byte getValue() {
			return bTempSensor;
		}
	}

	public enum RfSensor {
		Forward((byte)0x00),
		Reverse((byte)0x01);
		
		private byte bRfSensor;
		
		RfSensor(byte bRfSensor) {
			this.bRfSensor = bRfSensor;
		}

		public byte getValue() {
			return bRfSensor;
		}
	}

	
	/**
	 * RFID_TestSetAntennaPortConfiguration Object
	 * 
	 * <P>This class implements the RFID_TestSetAntennaPortConfiguration command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestSetAntennaPortConfiguration extends MtiCmd {
		public RFID_TestSetAntennaPortConfiguration(){
			mCmdHead = CmdHead.RFID_TestSetAntennaPortConfiguration;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param physicalPort
		 * @param powerLevel
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(PhysicalPort physicalPort, short powerLevel) {
			mParam.clear();
			mParam.add(physicalPort.bPhysicalPort);
			addParam(powerLevel);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param physicalPort
		 * @param powerLevel
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte physicalPort, short powerLevel) {
			mParam.clear();
			mParam.add(physicalPort);
			addParam(powerLevel);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_TestGetAntennaPortConfiguration Object
	 * 
	 * <P>This class implements the RFID_TestGetAntennaPortConfiguration command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestGetAntennaPortConfiguration extends MtiCmd {
		public RFID_TestGetAntennaPortConfiguration(){
			mCmdHead = CmdHead.RFID_TestGetAntennaPortConfiguration;
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
		public byte[] setCmd() {
			mParam.clear();
			return composeCmd();
		}
	}

	
	/**
	 * RFID_TestSetFrequencyConfiguration Object
	 * 
	 * <P>This class implements the RFID_TestSetFrequencyConfiguration command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestSetFrequencyConfiguration extends MtiCmd {
		public RFID_TestSetFrequencyConfiguration(){
			mCmdHead = CmdHead.RFID_TestSetFrequencyConfiguration;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param channelFlag
		 * @param exactFrequency
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(ChannelFlag channelFlag, int exactFrequency) {
			mParam.clear();
			mParam.add(channelFlag.bChannelFlag);
			addParam(exactFrequency);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param channelFlag
		 * @param exactFrequency
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte channelFlag, int exactFrequency) {
			mParam.clear();
			mParam.add(channelFlag);
			addParam(exactFrequency);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_TestGetFrequencyConfiguration Object
	 * 
	 * <P>This class implements the RFID_TestGetFrequencyConfiguration command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestGetFrequencyConfiguration extends MtiCmd {
		public RFID_TestGetFrequencyConfiguration(){
			mCmdHead = CmdHead.RFID_TestGetFrequencyConfiguration;
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
	 * RFID_TestSetRandomDataPulseTime Object
	 * 
	 * <P>This class implements the RFID_TestSetRandomDataPulseTime command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestSetRandomDataPulseTime extends MtiCmd {
		public RFID_TestSetRandomDataPulseTime(){
			mCmdHead = CmdHead.RFID_TestSetRandomDataPulseTime;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param onTime
		 * @param offTime
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(short onTime, short offTime) {
			mParam.clear();
			addParam(onTime);
			addParam(offTime);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_TestGetRandomDataPulseTime Object
	 * 
	 * <P>This class implements the RFID_TestGetRandomDataPulseTime command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestGetRandomDataPulseTime extends MtiCmd {
		public RFID_TestGetRandomDataPulseTime(){
			mCmdHead = CmdHead.RFID_TestGetRandomDataPulseTime;
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
	 * RFID_TestSetInventoryConfiguration Object
	 * 
	 * <P>This class implements the RFID_TestSetInventoryConfiguration command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestSetInventoryConfiguration extends MtiCmd {
		public RFID_TestSetInventoryConfiguration(){
			mCmdHead = CmdHead.RFID_TestSetInventoryConfiguration;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param continuousOperation
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(ContinuousOperation continuousOperation) {
			mParam.clear();
			mParam.add(continuousOperation.bContinuousOperation);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param continuousOperation
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte continuousOperation) {
			mParam.clear();
			mParam.add(continuousOperation);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_TestSetInventoryConfiguration Object
	 * 
	 * <P>This class implements the RFID_TestSetInventoryConfiguration command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestGetInventoryConfiguration extends MtiCmd {
		public RFID_TestGetInventoryConfiguration(){
			mCmdHead = CmdHead.RFID_TestGetInventoryConfiguration;
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
	 * RFID_TestSetInventoryConfiguration Object
	 * 
	 * <P>This class implements the RFID_TestSetInventoryConfiguration command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestTurnOnCarrierWave extends MtiCmd {
		public RFID_TestTurnOnCarrierWave(){
			mCmdHead = CmdHead.RFID_TestTurnOnCarrierWave;
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
	 * RFID_TestTurnOffCarrierWave Object
	 * 
	 * <P>This class implements the RFID_TestTurnOffCarrierWave command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestTurnOffCarrierWave extends MtiCmd {
		public RFID_TestTurnOffCarrierWave(){
			mCmdHead = CmdHead.RFID_TestTurnOffCarrierWave;
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
	 * RFID_TestInjectRandomData Object
	 * 
	 * <P>This class implements the RFID_TestInjectRandomData command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestInjectRandomData extends MtiCmd {
		public RFID_TestInjectRandomData(){
			mCmdHead = CmdHead.RFID_TestInjectRandomData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param count
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(int count) {
			mParam.clear();
			addParam(count);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_TestInjectRandomData Object
	 * 
	 * <P>This class implements the RFID_TestInjectRandomData command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_TestTransmitRandomData extends MtiCmd {
		public RFID_TestTransmitRandomData(){
			mCmdHead = CmdHead.RFID_TestTransmitRandomData;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param control
		 * @param duration
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Control control, int duration, byte randomType) {
			mParam.clear();
			mParam.add(control.bControl);
			addParam(duration);
			mParam.add(randomType);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param control
		 * @param duration
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte control, int duration, byte randomType) {
			mParam.clear();
			mParam.add(control);
			addParam(duration);
			mParam.add(randomType);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_EngGetTemperature Object
	 * 
	 * <P>This class implements the RFID_TestInjectRandomData command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_EngGetTemperature extends MtiCmd {
		public RFID_EngGetTemperature(){
			mCmdHead = CmdHead.RFID_EngGetTemperature;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param source (0 = PA, 1 = Ambient)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(TempSensor source) {
			mParam.clear();
			mParam.add(source.getValue());
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param source (0 = PA, 1 = Ambient)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte source) {
			mParam.clear();
			mParam.add(source);
			return composeCmd();
		}

		/** 
		 * getTemperature
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return Temperature
		 */
		public short getTemperature(byte[] response) {
			return getShort(response, STATUS_POS + 1);
		}
	}

	
	/**
	 * RFID_EngGetTemperature Object
	 * 
	 * <P>This class implements the RFID_TestInjectRandomData command as defined
	 * in section 4.8 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_EngGetRFPower extends MtiCmd {
		public RFID_EngGetRFPower(){
			mCmdHead = CmdHead.RFID_EngGetRFPower;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param source (0 = Forward, 1 = Reverse)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte source) {
			mParam.clear();
			mParam.add(source);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param source (0 = Forward, 1 = Reverse)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(RfSensor source) {
			mParam.clear();
			mParam.add(source.getValue());
			return composeCmd();
		}

		/** 
		 * getPowerLevel
		 * 
		 * This method is used to parse the command response.
		 * 
		 * @return RF Power
		 */
		public short getPowerLevel(byte[] response) {
			return getShort(response, STATUS_POS + 1);
		}
	}
}
