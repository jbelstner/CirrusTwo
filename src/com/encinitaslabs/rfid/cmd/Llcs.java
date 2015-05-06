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

public class Llcs extends MtiCmd {

	/** 
	 * Llcs<P>
	 * Class Constructor
	 */
	public Llcs( ) {
		
	}
	
	/** 
	 * setDeviceID<P>
	 * This method returns MTI LLCS command 0x00
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setDeviceID( byte deviceId ) {
    	if ((deviceId >= 0) && (deviceId <= 254)) {
        	CmdReaderModuleConfig.RFID_RadioSetDeviceID mtiCmd = new CmdReaderModuleConfig.RFID_RadioSetDeviceID();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(deviceId);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getDeviceID<P>
	 * This method returns MTI LLCS command 0x01
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getDeviceID( ) {
    	CmdReaderModuleConfig.RFID_RadioGetDeviceID mtiCmd = new CmdReaderModuleConfig.RFID_RadioGetDeviceID();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd();
    	return (serialCmd);
    }

	/** 
	 * setOperationMode<P>
	 * This method returns MTI LLCS command 0x02
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setOperationMode( byte operationMode ) {
    	if ((operationMode >= 0) && (operationMode <= 1)) {
        	CmdReaderModuleConfig.RFID_RadioSetOperationMode mtiCmd = new CmdReaderModuleConfig.RFID_RadioSetOperationMode();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(operationMode);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getOperationMode<P>
	 * This method returns MTI LLCS command 0x03
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getOperationMode( ) {
    	CmdReaderModuleConfig.RFID_RadioGetOperationMode mtiCmd = new CmdReaderModuleConfig.RFID_RadioGetOperationMode();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd();
    	return (serialCmd);
    }

	/** 
	 * setCurrentLinkProfile<P>
	 * This method returns MTI LLCS command 0x04
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setCurrentLinkProfile( byte linkProfile ) {
    	if ((linkProfile >= 0) && (linkProfile <= 3)) {
        	CmdReaderModuleConfig.RFID_RadioSetCurrentLinkProfile mtiCmd = new CmdReaderModuleConfig.RFID_RadioSetCurrentLinkProfile();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(linkProfile);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getCurrentLinkProfile<P>
	 * This method returns MTI LLCS command 0x05
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getCurrentLinkProfile( ) {
    	CmdReaderModuleConfig.RFID_RadioGetCurrentLinkProfile mtiCmd = new CmdReaderModuleConfig.RFID_RadioGetCurrentLinkProfile();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd();
    	return (serialCmd);
    }

	/** 
	 * writeRegister<P>
	 * This method returns MTI LLCS command 0x06
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] writeRegister( short address, int value ) {
    	CmdReaderModuleConfig.RFID_RadioWriteRegister mtiCmd = new CmdReaderModuleConfig.RFID_RadioWriteRegister();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd(address, value);
    	return (serialCmd);
    }

	/** 
	 * readRegister<P>
	 * This method returns MTI LLCS command 0x07
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] readRegister( short address ) {
    	CmdReaderModuleConfig.RFID_RadioReadRegister mtiCmd = new CmdReaderModuleConfig.RFID_RadioReadRegister();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd(address);
    	return (serialCmd);
    }

	/** 
	 * writeRegister<P>
	 * This method returns MTI LLCS command 0x08
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] writeRegister( short address, short bankSelector, int value ) {
    	CmdReaderModuleConfig.RFID_RadioWriteBankedRegister mtiCmd = new CmdReaderModuleConfig.RFID_RadioWriteBankedRegister();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd(address, bankSelector, value);
    	return (serialCmd);
    }

	/** 
	 * readRegister<P>
	 * This method returns MTI LLCS command 0x09
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] readRegister( short address, short bankSelector ) {
    	CmdReaderModuleConfig.RFID_RadioReadBankedRegister mtiCmd = new CmdReaderModuleConfig.RFID_RadioReadBankedRegister();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd(address, bankSelector);
    	return (serialCmd);
    }

	/** 
	 * readRegisterInfo<P>
	 * This method returns MTI LLCS command 0x0A
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] readRegisterInfo( short address ) {
    	CmdReaderModuleConfig.RFID_RadioReadRegisterInfo mtiCmd = new CmdReaderModuleConfig.RFID_RadioReadRegisterInfo();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd(address);
    	return (serialCmd);
    }

	/** 
	 * setInventoryPacketFormat<P>
	 * This method returns MTI LLCS command 0x0E
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setInventoryPacketFormat( byte packetFormat ) {
    	if ((packetFormat >= 0) && (packetFormat <= 1)) {
        	CmdReaderModuleConfig.RFID_RadioSetInventoryPacketFormat mtiCmd = new CmdReaderModuleConfig.RFID_RadioSetInventoryPacketFormat();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(packetFormat);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getInventoryPacketFormat<P>
	 * This method returns MTI LLCS command 0x0F
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getInventoryPacketFormat( ) {
    	CmdReaderModuleConfig.RFID_RadioGetInventoryPacketFormat mtiCmd = new CmdReaderModuleConfig.RFID_RadioGetInventoryPacketFormat();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd();
    	return (serialCmd);
    }
    
	/** 
	 * antennaPortSetState<P>
	 * This method returns MTI LLCS command 0x10
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] antennaPortSetState( byte antennaPort, byte state ) {
    	if ((antennaPort >= 0) && (antennaPort <= 15) && (state >= 0) && (state <= 1)) {
    		CmdAntennaPortConf.RFID_AntennaPortSetState mtiCmd = new CmdAntennaPortConf.RFID_AntennaPortSetState();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(antennaPort, state);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * antennaPortGetState<P>
	 * This method returns MTI LLCS command 0x11
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] antennaPortGetState( byte antennaPort ) {
    	if ((antennaPort >= 0) && (antennaPort <= 15)) {
        	CmdAntennaPortConf.RFID_AntennaPortGetState mtiCmd = new CmdAntennaPortConf.RFID_AntennaPortGetState();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(antennaPort);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }
    
	/** 
	 * antennaPortSetConfiguration<P>
	 * This method returns MTI LLCS command 0x12
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] antennaPortSetConfiguration( byte antennaPort, short powerLevel, short dwellTime, short numberInventoryCycles, byte physicalPort ) {
    	if ((antennaPort >= 0) && (antennaPort <= 15) && 
    		(powerLevel >= 0) && (powerLevel <= 330) &&
    		(physicalPort >= 0) && (physicalPort <= 1)) {
    		CmdAntennaPortConf.RFID_AntennaPortSetConfiguration mtiCmd = new CmdAntennaPortConf.RFID_AntennaPortSetConfiguration();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(antennaPort, powerLevel, dwellTime, numberInventoryCycles, physicalPort);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * antennaPortGetConfiguration<P>
	 * This method returns MTI LLCS command 0x13
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] antennaPortGetConfiguration( byte antennaPort ) {
    	if ((antennaPort >= 0) && (antennaPort <= 15)) {
        	CmdAntennaPortConf.RFID_AntennaPortGetConfiguration mtiCmd = new CmdAntennaPortConf.RFID_AntennaPortGetConfiguration();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(antennaPort);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }
    
	/** 
	 * antennaPortSetSenseThreshold<P>
	 * This method returns MTI LLCS command 0x14
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] antennaPortSetSenseThreshold( int antennaSenseThreshold ) {
    	if ((antennaSenseThreshold >= 0) && (antennaSenseThreshold <= 1048575)) {
    		CmdAntennaPortConf.RFID_AntennaPortSetSenseThreshold mtiCmd = new CmdAntennaPortConf.RFID_AntennaPortSetSenseThreshold();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(antennaSenseThreshold);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * antennaPortGetSenseThreshold<P>
	 * This method returns MTI LLCS command 0x15
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] antennaPortGetSenseThreshold(  ) {
    	CmdAntennaPortConf.RFID_AntennaPortGetSenseThreshold mtiCmd = new CmdAntennaPortConf.RFID_AntennaPortGetSenseThreshold();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd();
    	return (serialCmd);
    }
    
	/** 
	 * setActiveSelectCriteria<P>
	 * This method returns MTI LLCS command 0x20
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setActiveSelectCriteria( byte criteriaIndex, byte activeState ) {
    	if ((criteriaIndex >= 0) && (criteriaIndex <= 7) && 
    		(activeState >= 0) && (activeState <= 1)) {
        	CmdTagSelect.RFID_18K6CSetActiveSelectCriteria mtiCmd = new CmdTagSelect.RFID_18K6CSetActiveSelectCriteria();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(criteriaIndex, activeState);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getActiveSelectCriteria<P>
	 * This method returns MTI LLCS command 0x21
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getActiveSelectCriteria( byte criteriaIndex ) {
    	if ((criteriaIndex >= 0) && (criteriaIndex <= 7)) {
        	CmdTagSelect.RFID_18K6CGetActiveSelectCriteria mtiCmd = new CmdTagSelect.RFID_18K6CGetActiveSelectCriteria();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(criteriaIndex);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }
    
	/** 
	 * setSelectCriteria<P>
	 * This method returns MTI LLCS command 0x22
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setSelectCriteria( byte criteriaIndex, byte bank, short offset, byte count, byte target, byte action, byte truncation ) {
    	if ((criteriaIndex >= 0) && (criteriaIndex <= 7) && (bank >= 0) && (bank <= 3) &&
        	(target >= 0) && (target <= 4) && (action >= 0) && (action <= 7) &&
        	(truncation >= 0) && (truncation <= 1)) {
        	CmdTagSelect.RFID_18K6CSetSelectCriteria mtiCmd = new CmdTagSelect.RFID_18K6CSetSelectCriteria();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(criteriaIndex, bank, offset, count, target, action, truncation);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getSelectCriteria<P>
	 * This method returns MTI LLCS command 0x23
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getSelectCriteria( byte criteriaIndex ) {
    	if ((criteriaIndex >= 0) && (criteriaIndex <= 7)) {
        	CmdTagSelect.RFID_18K6CGetSelectCriteria mtiCmd = new CmdTagSelect.RFID_18K6CGetSelectCriteria();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(criteriaIndex);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }
    
	/** 
	 * setSelectMaskData<P>
	 * This method returns MTI LLCS command 0x24
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setSelectMaskData( byte criteriaIndex, byte maskIndex, byte maskData0, byte maskData1, byte maskData2, byte maskData3 ) {
    	if ((criteriaIndex >= 0) && (criteriaIndex <= 7) && (maskIndex >= 0) && (maskIndex <= 7)) {
        	CmdTagSelect.RFID_18K6CSetSelectMaskData mtiCmd = new CmdTagSelect.RFID_18K6CSetSelectMaskData();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(criteriaIndex, maskIndex, maskData0, maskData1, maskData2, maskData3);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getSelectMaskData<P>
	 * This method returns MTI LLCS command 0x25
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getSelectMaskData( byte criteriaIndex, byte maskIndex ) {
    	if ((criteriaIndex >= 0) && (criteriaIndex <= 7) && (maskIndex >= 0) && (maskIndex <= 7)) {
        	CmdTagSelect.RFID_18K6CGetSelectMaskData mtiCmd = new CmdTagSelect.RFID_18K6CGetSelectMaskData();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(criteriaIndex, maskIndex);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }
    
	/** 
	 * setPostMatchCriteria<P>
	 * This method returns MTI LLCS command 0x26
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setPostMatchCriteria( byte match, short offset, short count ) {
    	if ((match >= 0) && (match <= 1) && (offset >= 0) && (offset <= 511) && (count >= 0) && (count <= 496)) {
        	CmdTagSelect.RFID_18K6CSetPostMatchCriteria mtiCmd = new CmdTagSelect.RFID_18K6CSetPostMatchCriteria();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(match, offset, count);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getPostMatchCriteria<P>
	 * This method returns MTI LLCS command 0x27
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getPostMatchCriteria( ) {
    	CmdTagSelect.RFID_18K6CGetPostMatchCriteria mtiCmd = new CmdTagSelect.RFID_18K6CGetPostMatchCriteria();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }
    
	/** 
	 * setPostMatchMaskData<P>
	 * This method returns MTI LLCS command 0x28
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setPostMatchMaskData( byte maskIndex, byte maskData0, byte maskData1, byte maskData2, byte maskData3 ) {
    	if ((maskIndex >= 0) && (maskIndex <= 15)) {
        	CmdTagSelect.RFID_18K6CSetPostMatchMaskData mtiCmd = new CmdTagSelect.RFID_18K6CSetPostMatchMaskData();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(maskIndex, maskData0, maskData1, maskData2, maskData3);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getPostMatchMaskData<P>
	 * This method returns MTI LLCS command 0x29
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getPostMatchMaskData( byte maskIndex ) {
    	if ((maskIndex >= 0) && (maskIndex <= 15)) {
        	CmdTagSelect.RFID_18K6CGetPostMatchMaskData mtiCmd = new CmdTagSelect.RFID_18K6CGetPostMatchMaskData();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(maskIndex);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }
    
	/** 
	 * setQueryTagGroup<P>
	 * This method returns MTI LLCS command 0x30
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setQueryTagGroup( byte selected, byte session, byte target ) {
    	if ((selected >= 0) && (selected <= 3) && (session >= 0) && (session <= 3) && (target >= 0) && (target <= 1)) {
        	CmdTagAccess.RFID_18K6CSetQueryTagGroup mtiCmd = new CmdTagAccess.RFID_18K6CSetQueryTagGroup();
        	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
        	serialCmd = mtiCmd.setCmd(selected, session, target);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getQueryTagGroup<P>
	 * This method returns MTI LLCS command 0x31
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getQueryTagGroup( ) {
    	CmdTagAccess.RFID_18K6CGetQueryTagGroup mtiCmd = new CmdTagAccess.RFID_18K6CGetQueryTagGroup();
    	byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    	serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }
    
	/** 
	 * setCurrentSingulationAlgorithm<P>
	 * This method returns MTI LLCS command 0x32
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setCurrentSingulationAlgorithm( byte algorithm ) {
    	if ((algorithm >= 0) && (algorithm <= 1)) {
    		CmdTagAccess.RFID_18K6CSetCurrentSingulationAlgorithm mtiCmd = new CmdTagAccess.RFID_18K6CSetCurrentSingulationAlgorithm();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(algorithm);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getCurrentSingulationAlgorithm<P>
	 * This method returns MTI LLCS command 0x33
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getCurrentSingulationAlgorithm( ) {
		CmdTagAccess.RFID_18K6CGetCurrentSingulationAlgorithm mtiCmd = new CmdTagAccess.RFID_18K6CGetCurrentSingulationAlgorithm();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * setCurrentSingulationParameters<P>
	 * This method returns MTI LLCS command 0x34a
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setCurrentSingulationParameters( byte algorithm, byte qValue, byte retryCount, byte toggleTarget, byte repeatUntilNoTags ) {
    	if ((algorithm == 0) && (qValue >= 0) && (qValue <= 15) &&
    		(toggleTarget >= 0) && (toggleTarget <= 1) && (repeatUntilNoTags >= 0) && (repeatUntilNoTags <= 1)) {
    		CmdTagAccess.RFID_18K6CSetCurrentSingulationAlgorithmParameters mtiCmd = new CmdTagAccess.RFID_18K6CSetCurrentSingulationAlgorithmParameters();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(algorithm, qValue, retryCount, toggleTarget, repeatUntilNoTags);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * setCurrentSingulationParameters<P>
	 * This method returns MTI LLCS command 0x34b
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setCurrentSingulationParameters( byte algorithm, byte startQvalue, byte minQvalue, byte maxQvalue, byte retryCount, byte toggleTarget, byte thresholdMultiplier ) {
    	if ((algorithm == 1) && (toggleTarget >= 0) && (toggleTarget <= 1) &&
    		(startQvalue >= 0) && (startQvalue <= 15) && (minQvalue >= 0) && (minQvalue <= 15) && (maxQvalue >= 0) && (maxQvalue <= 15)) {
    		CmdTagAccess.RFID_18K6CSetCurrentSingulationAlgorithmParameters mtiCmd = new CmdTagAccess.RFID_18K6CSetCurrentSingulationAlgorithmParameters();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(algorithm, startQvalue, minQvalue, maxQvalue, retryCount, toggleTarget, thresholdMultiplier);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

/** 
	 * getCurrentSingulationAlgorithmParameters<P>
	 * This method returns MTI LLCS command 0x35
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getCurrentSingulationAlgorithmParameters( byte algorithm ) {
    	if ((algorithm >= 0) && (algorithm <= 1)) {
    		CmdTagAccess.RFID_18K6CGetCurrentSingulationAlgorithmParameters mtiCmd = new CmdTagAccess.RFID_18K6CGetCurrentSingulationAlgorithmParameters();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(algorithm);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * setTagAccessPassword<P>
	 * This method returns MTI LLCS command 0x36
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setTagAccessPassword( int password ) {
		CmdTagAccess.RFID_18K6CSetTagAccessPassword mtiCmd = new CmdTagAccess.RFID_18K6CSetTagAccessPassword();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(password);
    	return (serialCmd);
    }

	/** 
	 * getTagAccessPassword<P>
	 * This method returns MTI LLCS command 0x37
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getTagAccessPassword( ) {
		CmdTagAccess.RFID_18K6CGetTagAccessPassword mtiCmd = new CmdTagAccess.RFID_18K6CGetTagAccessPassword();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * setTagWriteDataBuffer<P>
	 * This method returns MTI LLCS command 0x38
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setTagWriteDataBuffer( byte bufferIndex, short bufferData ) {
		CmdTagAccess.RFID_18K6CSetTagWriteDataBuffer mtiCmd = new CmdTagAccess.RFID_18K6CSetTagWriteDataBuffer();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(bufferIndex, bufferData);
    	return (serialCmd);
    }

	/** 
	 * getTagWriteDataBuffer<P>
	 * This method returns MTI LLCS command 0x39
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getTagWriteDataBuffer( byte bufferIndex ) {
		CmdTagAccess.RFID_18K6CGetTagWriteDataBuffer mtiCmd = new CmdTagAccess.RFID_18K6CGetTagWriteDataBuffer();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(bufferIndex);
    	return (serialCmd);
    }

	/** 
	 * getGuardBufferTagNum<P>
	 * This method returns MTI LLCS command 0x3A
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getGuardBufferTagNum( ) {
		CmdTagAccess.RFID_18K6CGetGuardBufferTagNum mtiCmd = new CmdTagAccess.RFID_18K6CGetGuardBufferTagNum();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * getGuardBufferTagInfo<P>
	 * This method returns MTI LLCS command 0x3B
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getGuardBufferTagInfo( byte bufferIndex ) {
		CmdTagAccess.RFID_18K6CGetGuardBufferTagInfo mtiCmd = new CmdTagAccess.RFID_18K6CGetGuardBufferTagInfo();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(bufferIndex);
    	return (serialCmd);
    }

	/** 
	 * tagInventory<P>
	 * This method returns MTI LLCS command 0x40
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagInventory( byte select, byte postMatch, byte guardMode ) {
    	if ((select >= 0) && (select <= 1) &&
    		(postMatch >= 0) && (postMatch <= 1) &&
    		(guardMode >= 0) && (guardMode <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagInventory mtiCmd = new CmdTagProtocol.RFID_18K6CTagInventory();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(select, postMatch, guardMode);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * tagRead<P>
	 * This method returns MTI LLCS command 0x41
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagRead( byte bank, short offset, byte count, byte retryCount, byte select, byte postMatch ) {
    	if ((bank >= 0) && (bank <= 3) && (count >= 1) && (count <= 253) && (retryCount >= 0) && (retryCount <= 7) &&
    		(select >= 0) && (select <= 1) && (postMatch >= 0) && (postMatch <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagRead mtiCmd = new CmdTagProtocol.RFID_18K6CTagRead();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(bank, offset, count, retryCount, select, postMatch);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * tagWrite<P>
	 * This method returns MTI LLCS command 0x42
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagWrite( byte bank, short offset, short data, byte retryCount, byte select, byte postMatch ) {
    	if ((bank >= 0) && (bank <= 3) && (retryCount >= 0) && (retryCount <= 7) &&
        	(select >= 0) && (select <= 1) && (postMatch >= 0) && (postMatch <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagWrite mtiCmd = new CmdTagProtocol.RFID_18K6CTagWrite();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(bank, offset, data, retryCount, select, postMatch);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * tagKill<P>
	 * This method returns MTI LLCS command 0x43
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagKill( int killPwd, byte retryCount, byte select, byte postMatch ) {
    	if ((retryCount >= 0) && (retryCount <= 7) &&
            (select >= 0) && (select <= 1) && (postMatch >= 0) && (postMatch <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagKill mtiCmd = new CmdTagProtocol.RFID_18K6CTagKill();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(killPwd, retryCount, select, postMatch);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * tagLock<P>
	 * This method returns MTI LLCS command 0x44
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagLock( byte killPerm, byte accPerm, byte epcPerm, byte tidPerm,
						   byte userPerm, byte retryCount, byte select, byte postMatch ) {
    	if ((killPerm >= 0) && (killPerm <= 4) && (accPerm >= 0) && (accPerm <= 4) &&
    		(epcPerm >= 0) && (epcPerm <= 4) && (tidPerm >= 0) && (tidPerm <= 4) &&
    		(retryCount >= 0) && (retryCount <= 7) && (select >= 0) && (select <= 1) && (postMatch >= 0) && (postMatch <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagLock mtiCmd = new CmdTagProtocol.RFID_18K6CTagLock();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(killPerm, accPerm, epcPerm, tidPerm, userPerm, retryCount, select, postMatch );
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * tagMultipleWrite<P>
	 * This method returns MTI LLCS command 0x45
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagMultipleWrite( byte bank, short offset, byte dataLength, byte retryCount, byte select, byte postMatch ) {
    	if ((bank >= 0) && (bank <= 3) && (dataLength >= 1) && (dataLength <= 32) &&
    		(retryCount >= 0) && (retryCount <= 7) && (select >= 0) && (select <= 1) && (postMatch >= 0) && (postMatch <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagMultipleWrite mtiCmd = new CmdTagProtocol.RFID_18K6CTagMultipleWrite();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(bank, offset, dataLength, retryCount, select, postMatch);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * tagBlockWrite<P>
	 * This method returns MTI LLCS command 0x46
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagBlockWrite( byte bank, short offset, byte dataLength, byte retryCount, byte select, byte postMatch ) {
    	if ((bank >= 0) && (bank <= 3) && (retryCount >= 0) && (retryCount <= 7) && 
        	(select >= 0) && (select <= 1) && (postMatch >= 0) && (postMatch <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagBlockWrite mtiCmd = new CmdTagProtocol.RFID_18K6CTagBlockWrite();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(bank, offset, dataLength, retryCount, select, postMatch);
        	return (serialCmd);
       	} else {
    		return null;
    	}
    }

	/** 
	 * tagBlockErase<P>
	 * This method returns MTI LLCS command 0x47
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] tagBlockErase( byte bank, short offset, byte dataLength, byte retryCount, byte select, byte postMatch ) {
    	if ((bank >= 0) && (bank <= 3) && (retryCount >= 0) && (retryCount <= 7) && 
            (select >= 0) && (select <= 1) && (postMatch >= 0) && (postMatch <= 1)) {
    		CmdTagProtocol.RFID_18K6CTagBlockErase mtiCmd = new CmdTagProtocol.RFID_18K6CTagBlockErase();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(bank, offset, dataLength, retryCount, select, postMatch);
        	return (serialCmd);
       	} else {
    		return null;
    	}
    }

	/** 
	 * controlCancel<P>
	 * This method returns MTI LLCS command 0x50
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] controlCancel( ) {
		CmdReaderModuleControl.RFID_ControlCancel mtiCmd = new CmdReaderModuleControl.RFID_ControlCancel();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * controlPause<P>
	 * This method returns MTI LLCS command 0x52
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] controlPause( ) {
		CmdReaderModuleControl.RFID_ControlPause mtiCmd = new CmdReaderModuleControl.RFID_ControlPause();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * controlResume<P>
	 * This method returns MTI LLCS command 0x53
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] controlResume( ) {
		CmdReaderModuleControl.RFID_ControlResume mtiCmd = new CmdReaderModuleControl.RFID_ControlResume();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * controlSoftReset<P>
	 * This method returns MTI LLCS command 0x54
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] controlSoftReset( ) {
		CmdReaderModuleControl.RFID_ControlSoftReset mtiCmd = new CmdReaderModuleControl.RFID_ControlSoftReset();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * controlResetToBootloader<P>
	 * This method returns MTI LLCS command 0x55
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] controlResetToBootloader( ) {
		CmdReaderModuleControl.RFID_ControlResetToBootloader mtiCmd = new CmdReaderModuleControl.RFID_ControlResetToBootloader();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * controlSetPowerState<P>
	 * This method returns MTI LLCS command 0x56
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] controlSetPowerState( byte powerState ) {
    	if ((powerState >= 0) && (powerState <= 1)) {
    		CmdReaderModuleControl.RFID_ControlSetPowerState mtiCmd = new CmdReaderModuleControl.RFID_ControlSetPowerState();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(powerState);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * controlGetPowerState<P>
	 * This method returns MTI LLCS command 0x57
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] controlGetPowerState( ) {
		CmdReaderModuleControl.RFID_ControlGetPowerState mtiCmd = new CmdReaderModuleControl.RFID_ControlGetPowerState();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * getFirmwareVersion<P>
	 * This method returns MTI LLCS command 0x60
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getFirmwareVersion( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacGetFirmwareVersion mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacGetFirmwareVersion();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * getDebug<P>
	 * This method returns MTI LLCS command 0x61
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getDebug( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacGetDebug mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacGetDebug();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * clearError<P>
	 * This method returns MTI LLCS command 0x62
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] clearError( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacClearError mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacClearError();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * getError<P>
	 * This method returns MTI LLCS command 0x63
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getError( byte errorType ) {
    	if ((errorType >= 0) && (errorType <= 1)) {
        	CmdReaderModuleFirmwareAccess.RFID_MacGetError mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacGetError();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(errorType);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * getBootloaderVersion<P>
	 * This method returns MTI LLCS command 0x64
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getBootloaderVersion( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacGetBootloaderVersion mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacGetBootloaderVersion();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * writeOemData<P>
	 * This method returns MTI LLCS command 0x66
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] writeOemData( short address, int data ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacWriteOemData mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacWriteOemData();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(address, data);
    	return (serialCmd);
    }

	/** 
	 * readOemData<P>
	 * This method returns MTI LLCS command 0x67
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] readOemData( short address ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacReadOemData mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacReadOemData();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(address);
    	return (serialCmd);
    }

	/** 
	 * bypassWriteRegister<P>
	 * This method returns MTI LLCS command 0x68
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] bypassWriteRegister( short address, short data ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacBypassWriteRegister mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacBypassWriteRegister();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(address, data);
    	return (serialCmd);
    }

	/** 
	 * bypassReadRegister<P>
	 * This method returns MTI LLCS command 0x69
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] bypassReadRegister( short address ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacBypassReadRegister mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacBypassReadRegister();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(address);
    	return (serialCmd);
    }

	/** 
	 * setRegion<P>
	 * This method returns MTI LLCS command 0x6A
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setRegion( byte regionOperation ) {
    	if ((regionOperation >= 0) && (regionOperation <= 11)) {
        	CmdReaderModuleFirmwareAccess.RFID_MacSetRegion mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacSetRegion();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(regionOperation);
        	return (serialCmd);
     	} else {
    		return null;
    	}
	}

	/** 
	 * getRegion<P>
	 * This method returns MTI LLCS command 0x6B
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getRegion( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacGetRegion mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacGetRegion();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * getOEMCfgVersion<P>
	 * This method returns MTI LLCS command 0x6C
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getOEMCfgVersion( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacGetOEMCfgVersion mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacGetOEMCfgVersion();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * getOEMCfgUpdateNumber<P>
	 * This method returns MTI LLCS command 0x6D
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getOEMCfgUpdateNumber( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacGetOEMCfgUpdateNumber mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacGetOEMCfgUpdateNumber();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * setUartBaudRate<P>
	 * This method is a macro to configure the HP SiP to use the
	 * specified baud rate by sending the RFID_MacWriteOemData command
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setUartBaudRate( int baudRate ) {
    	if ((baudRate == 115200) || (baudRate == 230400) || (baudRate == 460800) || (baudRate == 921600)) {
        	CmdReaderModuleFirmwareAccess.RFID_MacWriteOemData mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacWriteOemData();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(MtiCmd.BAUD_RATE_ADDR, baudRate);
        	return (serialCmd);
     	} else {
    		return null;
    	}
    }

	/** 
	 * getUartBaudRate<P>
	 * This method is a macro to configure the HP SiP to use the
	 * specified baud rate by sending the RFID_MacReadOemData command
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getUartBaudRate( ) {
    	CmdReaderModuleFirmwareAccess.RFID_MacReadOemData mtiCmd = new CmdReaderModuleFirmwareAccess.RFID_MacReadOemData();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(MtiCmd.BAUD_RATE_ADDR);
    	return (serialCmd);
    }

	/** 
	 * setGpioPinsConfiguration<P>
	 * This method returns MTI LLCS command 0x70
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] setGpioPinsConfiguration( byte mask, byte configuration ) {
    	if ((mask >= 0) && (mask <= 15) && (configuration >= 0) && (configuration <= 15)) {
        	CmdReaderModuleGpioPinAccess.RFID_RadioSetGpioPinsConfiguration mtiCmd = new CmdReaderModuleGpioPinAccess.RFID_RadioSetGpioPinsConfiguration();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(mask, configuration);
        	return (serialCmd);
     	} else {
    		return null;
    	}
    }

	/** 
	 * getGpioPinsConfiguration<P>
	 * This method returns MTI LLCS command 0x71
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] getGpioPinsConfiguration( ) {
    	CmdReaderModuleGpioPinAccess.RFID_RadioGetGpioPinsConfiguration mtiCmd = new CmdReaderModuleGpioPinAccess.RFID_RadioGetGpioPinsConfiguration();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * writeGpioPins<P>
	 * This method returns MTI LLCS command 0x72
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] writeGpioPins( byte mask, byte value ) {
    	if ((mask >= 0) && (mask <= 15) && (value >= 0) && (value <= 15)) {
        	CmdReaderModuleGpioPinAccess.RFID_RadioWriteGpioPins mtiCmd = new CmdReaderModuleGpioPinAccess.RFID_RadioWriteGpioPins();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(mask, value);
        	return (serialCmd);
     	} else {
    		return null;
    	}
    }

	/** 
	 * readGpioPins<P>
	 * This method returns MTI LLCS command 0x73
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] readGpioPins( byte mask ) {
    	if ((mask >= 0) && (mask <= 15)) {
        	CmdReaderModuleGpioPinAccess.RFID_RadioReadGpioPins mtiCmd = new CmdReaderModuleGpioPinAccess.RFID_RadioReadGpioPins();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(mask);
        	return (serialCmd);
     	} else {
    		return null;
    	}
    }

	/** 
	 * testSetAntennaPortConfiguration<P>
	 * This method returns MTI LLCS command 0x80
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testSetAntennaPortConfiguration( byte physicalPort, short powerLevel ) {
    	if ((physicalPort >= 0) && (physicalPort <= 1) && (powerLevel >= 0) && (powerLevel <= 330)) {
    		CmdReaderModuleTestSupport.RFID_TestSetAntennaPortConfiguration mtiCmd = new CmdReaderModuleTestSupport.RFID_TestSetAntennaPortConfiguration();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(physicalPort, powerLevel);
        	return (serialCmd);
     	} else {
    		return null;
    	}
    }

	/** 
	 * testGetAntennaPortConfiguration<P>
	 * This method returns MTI LLCS command 0x81
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testGetAntennaPortConfiguration( ) {
		CmdReaderModuleTestSupport.RFID_TestGetAntennaPortConfiguration mtiCmd = new CmdReaderModuleTestSupport.RFID_TestGetAntennaPortConfiguration();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * testSetFrequencyConfiguration<P>
	 * This method returns MTI LLCS command 0x82
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testSetFrequencyConfiguration( byte channelFlag, int exactFrequency ) {
    	if ((channelFlag >= 0) && (channelFlag <= 1) && (exactFrequency >= 865000) && (exactFrequency <= 928000)) {
    		CmdReaderModuleTestSupport.RFID_TestSetFrequencyConfiguration mtiCmd = new CmdReaderModuleTestSupport.RFID_TestSetFrequencyConfiguration();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(channelFlag, exactFrequency);
        	return (serialCmd);
     	} else {
    		return null;
    	}
    }

	/** 
	 * testGetFrequencyConfiguration<P>
	 * This method returns MTI LLCS command 0x83
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testGetFrequencyConfiguration( ) {
		CmdReaderModuleTestSupport.RFID_TestGetFrequencyConfiguration mtiCmd = new CmdReaderModuleTestSupport.RFID_TestGetFrequencyConfiguration();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * testSetRandomDataPulseTime<P>
	 * This method returns MTI LLCS command 0x84
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testSetRandomDataPulseTime( short onTime, short offTime ) {
		CmdReaderModuleTestSupport.RFID_TestSetRandomDataPulseTime mtiCmd = new CmdReaderModuleTestSupport.RFID_TestSetRandomDataPulseTime();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(onTime, offTime);
    	return (serialCmd);
    }

	/** 
	 * testGetRandomDataPulseTime<P>
	 * This method returns MTI LLCS command 0x85
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testGetRandomDataPulseTime( ) {
		CmdReaderModuleTestSupport.RFID_TestGetRandomDataPulseTime mtiCmd = new CmdReaderModuleTestSupport.RFID_TestGetRandomDataPulseTime();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * testSetInventoryConfiguration<P>
	 * This method returns MTI LLCS command 0x86
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testSetInventoryConfiguration( byte continuousOperation ) {
    	if ((continuousOperation >= 0) && (continuousOperation <= 1)) {
    		CmdReaderModuleTestSupport.RFID_TestSetInventoryConfiguration mtiCmd = new CmdReaderModuleTestSupport.RFID_TestSetInventoryConfiguration();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(continuousOperation);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * testGetInventoryConfiguration<P>
	 * This method returns MTI LLCS command 0x87
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testGetInventoryConfiguration( ) {
		CmdReaderModuleTestSupport.RFID_TestGetInventoryConfiguration mtiCmd = new CmdReaderModuleTestSupport.RFID_TestGetInventoryConfiguration();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * testTurnOnCarrierWave<P>
	 * This method returns MTI LLCS command 0x88
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testTurnOnCarrierWave( ) {
		CmdReaderModuleTestSupport.RFID_TestTurnOnCarrierWave mtiCmd = new CmdReaderModuleTestSupport.RFID_TestTurnOnCarrierWave();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * testTurnOffCarrierWave<P>
	 * This method returns MTI LLCS command 0x89
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testTurnOffCarrierWave( ) {
		CmdReaderModuleTestSupport.RFID_TestTurnOffCarrierWave mtiCmd = new CmdReaderModuleTestSupport.RFID_TestTurnOffCarrierWave();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd( );
    	return (serialCmd);
    }

	/** 
	 * testInjectRandomData<P>
	 * This method returns MTI LLCS command 0x8A
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testInjectRandomData( int count ) {
		CmdReaderModuleTestSupport.RFID_TestInjectRandomData mtiCmd = new CmdReaderModuleTestSupport.RFID_TestInjectRandomData();
		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
		serialCmd = mtiCmd.setCmd(count);
    	return (serialCmd);
    }

	/** 
	 * testTransmitRandomData<P>
	 * This method returns MTI LLCS command 0x8B
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testTransmitRandomData( byte control, int duration, byte randomType ) {
    	if ((control >= 0) && (control <= 1) && (randomType >= 0) && (randomType <= 1)) {
    		CmdReaderModuleTestSupport.RFID_TestTransmitRandomData mtiCmd = new CmdReaderModuleTestSupport.RFID_TestTransmitRandomData();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(control, duration, randomType);
        	return (serialCmd);    		
    	} else {
    		return null;
    	}
    }

	/** 
	 * testGetTemperature<P>
	 * This method returns MTI LLCS command 0x91
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testGetTemperature( byte source ) {
    	if ((source >= 0) && (source <= 1)) {
    		CmdReaderModuleTestSupport.RFID_EngGetTemperature mtiCmd = new CmdReaderModuleTestSupport.RFID_EngGetTemperature();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(source);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }

	/** 
	 * testGetRFPower<P>
	 * This method returns MTI LLCS command 0x93
	 * @return A byte[] containing the bit-packed message
	 */
    public byte[] testGetRFPower( byte source ) {
    	if ((source >= 0) && (source <= 1)) {
    		CmdReaderModuleTestSupport.RFID_EngGetRFPower mtiCmd = new CmdReaderModuleTestSupport.RFID_EngGetRFPower();
    		byte[] serialCmd = new byte[LENGTH_OF_MTIC];
    		serialCmd = mtiCmd.setCmd(source);
        	return (serialCmd);
    	} else {
    		return null;
    	}
    }
}
