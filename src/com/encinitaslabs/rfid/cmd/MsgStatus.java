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

public enum MsgStatus {

	RFID_STATUS_OK((byte)0x00),
	
	RFID_ERROR_CMD_INVALID_DATA_LENGTH((byte)0x0E),
	RFID_ERROR_CMD_INVALID_PARAMETER((byte)0x0F),
	
	RFID_ERROR_SYS_CHANNEL_TIMEOUT((byte)0x0A),
	RFID_ERROR_SYS_SECURITY_FAILURE((byte)0xFE),
	RFID_ERROR_SYS_MODULE_FAILURE((byte)0xFF),
	
	RFID_ERROR_HWOPT_READONLY_ADDRESS((byte)0xA0),
	RFID_ERROR_HWOPT_UNSUPPORTED_REGION((byte)0xA1),
	
	RFID_ERROR_18K6C_REQRN((byte)0x01),
	RFID_ERROR_18K6C_ACCESS((byte)0x02),
	RFID_ERROR_18K6C_KILL((byte)0x03),
	RFID_ERROR_18K6C_NOREPLY((byte)0x04),
	RFID_ERROR_18K6C_LOCK((byte)0x05),
	RFID_ERROR_18K6C_BLOCKWRITE((byte)0x06),
	RFID_ERROR_18K6C_BLOCKERASE((byte)0x07),
	RFID_ERROR_18K6C_READ((byte)0x08),
	RFID_ERROR_18K6C_SELECT((byte)0x09),
	RFID_ERROR_18K6C_EASCODE((byte)0x20),
	
	RFID_ERROR_18K6B_INVALID_CRC((byte)0x11),
	RFID_ERROR_18K6B_RFICREG_FIFO((byte)0x12),
	RFID_ERROR_18K6B_NO_RESPONSE((byte)0x13),
	RFID_ERROR_18K6B_NO_ACKNOWLEDGE((byte)0x14),
	RFID_ERROR_18K6B_PREAMBLE((byte)0x15),
	
	RFID_ERROR_6CTAG_OTHER_ERROR((byte)0x80),
	RFID_ERROR_6CTAG_MEMORY_OVERRUN((byte)0x83),
	RFID_ERROR_6CTAG_MEMORY_LOCKED((byte)0x84),
	RFID_ERROR_6CTAG_INSUFFICIENT_POWER((byte)0x8B),
	RFID_ERROR_6CTAG_NONSPECIFIC_ERROR((byte)0x8F);
	
	private byte rtnStatus;
	
	MsgStatus(byte rtnStatus) {
		this.rtnStatus = rtnStatus;
	}
	
	public byte getRtnStatus() {
		return this.rtnStatus;
	}
	
	public byte getValue() {
		return rtnStatus;
	}
}
