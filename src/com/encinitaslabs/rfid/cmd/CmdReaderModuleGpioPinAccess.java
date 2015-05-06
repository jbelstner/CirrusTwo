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
 * CmdReaderModuleGpioPinAccess Object
 * 
 * <P>This class contains a set of nested classes that extend the MtiCmd parent
 * and implement the RFID Reader/Module GPIO Pin Access commands (section 4.7)
 * as defined in the MTI RU-824/861 RFID Low Level Command set.
 *  
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CmdReaderModuleGpioPinAccess {

	public enum Mask {
		GPIO0_Select((byte)0x01),
		GPIO1_Select((byte)0x02),
		GPIO2_Select((byte)0x04),
		GPIO3_Select((byte)0x08);
		
		private byte bMask;
		
		Mask(byte bMask) {
			this.bMask = bMask;
		}

		public byte getValue() {
			return bMask;
		}
	}

	public enum Configuration {
		GPIO0_AsOutput((byte)0x01),
		GPIO1_AsOutput((byte)0x02),
		GPIO2_AsOutput((byte)0x04),
		GPIO3_AsOutput((byte)0x08);
		
		private byte bConfiguration;
		
		Configuration(byte bConfiguration) {
			this.bConfiguration = bConfiguration;
		}

		public byte getValue() {
			return bConfiguration;
		}
	}

	public enum Value {
		GPIO0_HighState((byte)0x01),
		GPIO1_HighState((byte)0x02),
		GPIO2_HighState((byte)0x04),
		GPIO3_HighState((byte)0x08);
		
		private byte bValue;
		
		Value(byte bValue) {
			this.bValue = bValue;
		}

		public byte getValue() {
			return bValue;
		}
	}

	
	/**
	 * RFID_RadioSetGpioPinsConfiguration Object
	 * 
	 * <P>This class implements the RFID_RadioSetGpioPinsConfiguration command as defined
	 * in section 4.7 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioSetGpioPinsConfiguration extends MtiCmd {
		public RFID_RadioSetGpioPinsConfiguration(){
			mCmdHead = CmdHead.RFID_RadioSetGpioPinsConfiguration;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param mask
		 * @param configuration (in/out)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Mask mask, Configuration configuration) {
			mParam.clear();
			mParam.add(mask.bMask);
			mParam.add(configuration.bConfiguration);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param mask
		 * @param configuration (in/out)
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte mask, byte configuration) {
			mParam.clear();
			mParam.add(mask);
			mParam.add(configuration);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioGetGpioPinsConfiguration Object
	 * 
	 * <P>This class implements the RFID_RadioGetGpioPinsConfiguration command as defined
	 * in section 4.7 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioGetGpioPinsConfiguration extends MtiCmd {
		public RFID_RadioGetGpioPinsConfiguration(){
			mCmdHead = CmdHead.RFID_RadioGetGpioPinsConfiguration;
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
	 * RFID_RadioWriteGpioPins Object
	 * 
	 * <P>This class implements the RFID_RadioWriteGpioPins command as defined
	 * in section 4.7 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioWriteGpioPins extends MtiCmd {
		public RFID_RadioWriteGpioPins(){
			mCmdHead = CmdHead.RFID_RadioWriteGpioPins;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param mask
		 * @param value
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Mask mask, Value value) {
			mParam.clear();
			mParam.add(mask.bMask);
			mParam.add(value.bValue);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param mask
		 * @param value
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte mask, byte value) {
			mParam.clear();
			mParam.add(mask);
			mParam.add(value);
			return composeCmd();
		}
	}

	
	/**
	 * RFID_RadioReadGpioPins Object
	 * 
	 * <P>This class implements the RFID_RadioReadGpioPins command as defined
	 * in section 4.7 of the MTI RU-824/861 RFID Low Level Command set.
	 *  
	 *  
	 * @author Encinitas Laboratories, Inc.
	 * @version 0.1
	 */
	static final class RFID_RadioReadGpioPins extends MtiCmd {
		public RFID_RadioReadGpioPins(){
			mCmdHead = CmdHead.RFID_RadioReadGpioPins;
		};

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param mask
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(Mask mask) {
			mParam.clear();
			mParam.add(mask.bMask);
			return composeCmd();
		}

		/** 
		 * setCmd
		 * 
		 * This method builds the specified RFID command.
		 * 
		 * @param mask
		 * 
		 * @return The packed RU-824/861 command
		 */
		public byte[] setCmd(byte mask) {
			mParam.clear();
			mParam.add(mask);
			return composeCmd();
		}
	}
}
