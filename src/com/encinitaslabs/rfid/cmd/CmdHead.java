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

public enum CmdHead {
	// RFID Reader/Module Configuration
	RFID_RadioSetDeviceID((byte)0x00),
	RFID_RadioGetDeviceID((byte)0x01),
	RFID_RadioSetOperationMode((byte)0x02),
	RFID_RadioGetOperationMode((byte)0x03),
	RFID_RadioSetCurrentLinkProfile((byte)0x04),
	RFID_RadioGetCurrentLinkProfile((byte)0x05),
	RFID_RadioWriteRegister((byte)0x06),
	RFID_RadioReadRegister((byte)0x07),
	RFID_RadioWriteBankedRegister((byte)0x08),
	RFID_RadioReadBankedRegister((byte)0x09),
	RFID_RadioReadRegisterInfo((byte)0x0A),
	reserved_0x0B((byte)0x0B),
	reserved_0x0C((byte)0x0C),
	reserved_0x0D((byte)0x0D),
	RFID_RadioSetInventoryPacketFormat((byte)0x0E),
	RFID_RadioGetInventoryPacketFormat((byte)0x0F),
	// Antenna Port Configuration
	RFID_AntennaPortSetState((byte)0x10),
	RFID_AntennaPortGetState((byte)0x11),
	RFID_AntennaPortSetConfiguration((byte)0x12),
	RFID_AntennaPortGetConfiguration((byte)0x13),
	RFID_AntennaPortSetSenseThreshold((byte)0x14),
	RFID_AntennaPortGetSenseThreshold((byte)0x15),
	reserved_0x16((byte)0x16),
	reserved_0x17((byte)0x17),
	reserved_0x18((byte)0x18),
	reserved_0x19((byte)0x19),
	reserved_0x1A((byte)0x1A),
	reserved_0x1B((byte)0x1B),
	reserved_0x1C((byte)0x1C),
	reserved_0x1D((byte)0x1D),
	reserved_0x1E((byte)0x1E),
	reserved_0x1F((byte)0x1F),
	// ISO 18000-6C Tag Select Operation
	RFID_18K6CSetActiveSelectCriteria((byte)0x20),
	RFID_18K6CGetActiveSelectCriteria((byte)0x21),
	RFID_18K6CSetSelectCriteria((byte)0x22),
	RFID_18K6CGetSelectCriteria((byte)0x23),
	RFID_18K6CSetSelectMaskData((byte)0x24),
	RFID_18K6CGetSelectMaskData((byte)0x25),
	RFID_18K6CSetPostMatchCriteria((byte)0x26),
	RFID_18K6CGetPostMatchCriteria((byte)0x27),
	RFID_18K6CSetPostMatchMaskData((byte)0x28),
	RFID_18K6CGetPostMatchMaskData((byte)0x29),
	reserved_0x2A((byte)0x2A),
	reserved_0x2B((byte)0x2B),
	reserved_0x2C((byte)0x2C),
	reserved_0x2D((byte)0x2D),
	reserved_0x2E((byte)0x2E),
	reserved_0x2F((byte)0x2F),
	// ISO 18000-6C Tag Access Parameters
	RFID_18K6CSetQueryTagGroup((byte)0x30),
	RFID_18K6CGetQueryTagGroup((byte)0x31),
	RFID_18K6CSetCurrentSingulationAlgorithm((byte)0x32),
	RFID_18K6CGetCurrentSingulationAlgorithm((byte)0x33),
	RFID_18K6CSetCurrentSingulationAlgorithmParameters((byte)0x34),
	RFID_18K6CGetCurrentSingulationAlgorithmParameters((byte)0x35),
	RFID_18K6CSetTagAccessPassword((byte)0x36),
	RFID_18K6CGetTagAccessPassword((byte)0x37),
	RFID_18K6CSetTagWriteDataBuffer((byte)0x38),
	RFID_18K6CGetTagWriteDataBuffer((byte)0x39),
	RFID_18K6CGetGuardBufferTagNum((byte)0x3A),
	RFID_18K6CGetGuardBufferTagInfo((byte)0x3B),
	reserved_0x3C((byte)0x3C),
	reserved_0x3D((byte)0x3D),
	reserved_0x3E((byte)0x3E),
	reserved_0x3F((byte)0x3F),
	// ISO 18000-6C Tag Protocol Operation
	RFID_18K6CTagInventory((byte)0x40),
	RFID_18K6CTagRead((byte)0x41),
	RFID_18K6CTagWrite((byte)0x42),
	RFID_18K6CTagKill((byte)0x43),
	RFID_18K6CTagLock((byte)0x44),
	RFID_18K6CTagMultipleWrite((byte)0x45),
	RFID_18K6CTagBlockWrite((byte)0x46),
	RFID_18K6CTagBlockErase((byte)0x47),
	reserved_0x48((byte)0x48),
	reserved_0x49((byte)0x49),
	reserved_0x4A((byte)0x4A),
	reserved_0x4B((byte)0x4B),
	reserved_0x4C((byte)0x4C),
	reserved_0x4D((byte)0x4D),
	reserved_0x4E((byte)0x4E),
	reserved_0x4F((byte)0x4F),
	// RFID Reader/Module Control Operation
	RFID_ControlCancel((byte)0x50),
	reserved_0x51((byte)0x51),
	RFID_ControlPause((byte)0x52),
	RFID_ControlResume((byte)0x53),
	RFID_ControlSoftReset((byte)0x54),
	RFID_ControlResetToBootloader((byte)0x55),
	RFID_ControlSetPowerState((byte)0x56),
	RFID_ControlGetPowerState((byte)0x57),
	reserved_0x58((byte)0x58),
	reserved_0x59((byte)0x59),
	reserved_0x5A((byte)0x5A),
	reserved_0x5B((byte)0x5B),
	reserved_0x5C((byte)0x5C),
	reserved_0x5D((byte)0x5D),
	reserved_0x5E((byte)0x5E),
	reserved_0x5F((byte)0x5F),
	// RFID Reader/Module Firmware Access
	RFID_MacGetFirmwareVersion((byte)0x60),
	RFID_MacGetDebug((byte)0x61),
	RFID_MacClearError((byte)0x62),
	RFID_MacGetError((byte)0x63),
	RFID_MacGetBootloaderVersion((byte)0x64),
	reserved_0x65((byte)0x65),
	RFID_MacWriteOemData((byte)0x66),
	RFID_MacReadOemData((byte)0x67),
	RFID_MacBypassWriteRegister((byte)0x68),
	RFID_MacBypassReadRegister((byte)0x69),
	RFID_MacSetRegion((byte)0x6A),
	RFID_MacGetRegion((byte)0x6B),
	RFID_MacGetOEMCfgVersion((byte)0x6C),
	RFID_MacGetOEMCfgUpdateNumber((byte)0x6D),
	reserved_0x6E((byte)0x6E),
	reserved_0x6F((byte)0x6F),
	// RFID Reader/Module GPIO Pin Access
	RFID_RadioSetGpioPinsConfiguration((byte)0x70),
	RFID_RadioGetGpioPinsConfiguration((byte)0x71),
	RFID_RadioWriteGpioPins((byte)0x72),
	RFID_RadioReadGpioPins((byte)0x73),
	reserved_0x74((byte)0x74),
	reserved_0x75((byte)0x75),
	reserved_0x76((byte)0x76),
	reserved_0x77((byte)0x77),
	reserved_0x78((byte)0x78),
	reserved_0x79((byte)0x79),
	reserved_0x7A((byte)0x7A),
	reserved_0x7B((byte)0x7B),
	reserved_0x7C((byte)0x7C),
	reserved_0x7D((byte)0x7D),
	reserved_0x7E((byte)0x7E),
	reserved_0x7F((byte)0x7F),
	// RFID Reader/Module Region Test Support
	RFID_TestSetAntennaPortConfiguration((byte)0x80),
	RFID_TestGetAntennaPortConfiguration((byte)0x81),
	RFID_TestSetFrequencyConfiguration((byte)0x82),
	RFID_TestGetFrequencyConfiguration((byte)0x83),
	RFID_TestSetRandomDataPulseTime((byte)0x84),
	RFID_TestGetRandomDataPulseTime((byte)0x85),
	RFID_TestSetInventoryConfiguration((byte)0x86),
	RFID_TestGetInventoryConfiguration((byte)0x87),
	RFID_TestTurnOnCarrierWave((byte)0x88),
	RFID_TestTurnOffCarrierWave((byte)0x89),
	RFID_TestInjectRandomData((byte)0x8A),
	RFID_TestTransmitRandomData((byte)0x8B),
	reserved_0x8C((byte)0x8C),
	reserved_0x8D((byte)0x8D),
	reserved_0x8E((byte)0x8E),
	reserved_0x8F((byte)0x8F),
	reserved_0x90((byte)0x90),
	RFID_EngGetTemperature((byte)0x91),
	reserved_0x92((byte)0x92),
	RFID_EngGetRFPower((byte)0x93);

	private byte byte1st;
	
	CmdHead(byte byte1st) {
		this.byte1st = byte1st;
	}
	
	public byte get1stCmd() {
		return this.byte1st;
	}

	public byte getValue() {
		return byte1st;
	}
}
