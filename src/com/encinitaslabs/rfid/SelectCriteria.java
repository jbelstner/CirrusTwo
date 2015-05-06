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
package com.encinitaslabs.rfid;

import java.util.ArrayList;

import com.encinitaslabs.rfid.cmd.CmdTagSelect;

public class SelectCriteria {
	
	public final int MASK_LENGTH = 8;
	private CmdTagSelect.ActiveState activeState = CmdTagSelect.ActiveState.Disabled;
	private CmdTagSelect.Bank bank = CmdTagSelect.Bank.EPC;
	private Short offset = 0;
	private Byte count = 0;
	private CmdTagSelect.Target targetFlag = CmdTagSelect.Target.S0;
	private Byte action = 0;
	private CmdTagSelect.Truncation truncation = CmdTagSelect.Truncation.Disable;
	private ArrayList<Integer> mask = null;

	/** 
	 * SelectCriteria<P>
	 * Class Constructor
	 */
	public SelectCriteria( ) {
		mask = new ArrayList<Integer>();
		for (int i = 0; i < MASK_LENGTH; i++) {
			mask.add(i, 0);
		}
	}
	
	/** 
	 * getBank<P>
	 * This method returns the memory bank to be compared against
	 * for this select criteria.
	 * The values "Reserved", "EPC", "TID" and "User".
	 * @return A CmdTagSelect.Bank enumeration.
	 */
	public CmdTagSelect.Bank getBank() {
		return bank;
	}

	/** 
	 * setBank<P>
	 * This method sets the memory bank to be compared against
	 * for this select criteria.
	 * The values "Reserved", "EPC", "TID" and "User".
	 * @param bank_ A CmdTagProtocol.Bank enumeration.
	 */
	public void setBank(CmdTagSelect.Bank bank_) {
		bank = bank_;
	}

	/** 
	 * setBank<P>
	 * This method sets the memory bank to be compared against
	 * for this select criteria.
	 * The values "Reserved", "EPC", "TID" and "User".
	 * @param bank_ A String.
	 */
	public void setBank(String bank_) {
		if (bank_ != null) {
			try {
				bank = CmdTagSelect.Bank.valueOf(bank_);			
	        } catch(IllegalArgumentException iae) {
				bank = CmdTagSelect.Bank.EPC;			
	        }
		}
	}

	/** 
	 * getOffset<P>
	 * This method returns the offset in bits from the start
	 * of bank that the specified mask will be matched against.
	 * The values range from 0x0000 to 0xFFFF.
	 * @return The bit offset.
	 */
	public Short getOffset() {
		return offset;
	}

	/** 
	 * setOffset<P>
	 * This method sets the offset in bits from the start
	 * of bank that the specified mask will be matched against.
	 * The values range from 0x0000 to 0xFFFF.
	 * @param offset_ The bit offset.
	 */
	public void setOffset(Short offset_) {
		offset = offset_;
	}

	/** 
	 * setOffset<P>
	 * This method sets the offset in bits from the start
	 * of bank that the specified mask will be matched against.
	 * The values range from 0x0000 to 0xFFFF.
	 * @param offset_ The bit offset.
	 */
	public void setOffset(Number offset_) {
		if (offset_ != null) {
			offset = offset_.shortValue();
		}
	}

	/** 
	 * getCount<P>
	 * This method returns the number of bits in the tag mask.
	 * The values range from 0x00 to 0xFF.
	 * @return The tag mask length.
	 */
	public Byte getCount() {
		return count;
	}

	/** 
	 * setCount<P>
	 * This method sets the number of bits in the tag mask.
	 * The values range from 0x00 to 0xFF.
	 * @param count_ The tag mask length.
	 */
	public void setCount(Byte count_) {
		count = count_;
	}

	/** 
	 * setCount<P>
	 * This method sets the number of bits in the tag mask.
	 * The values range from 0x00 to 0xFF.
	 * @param count_ The tag mask length.
	 */
	public void setCount(Number count_) {
		if (count_ != null) {
			count = count_.byteValue();
		}
	}

	/** 
	 * getMask<P>
	 * This method returns the portion of the mask
	 * specified by index_.
	 * The values range from 0 to 7.
	 * @param index_ An index into the array of Integers.
	 * @return The byte[] needed for a serial message.
	 */
	public byte[] getMask(int index_) {
		if ((index_ >= (byte)0x00) && (index_ <= (byte)0x07)) {
			byte[] mask_ = new byte[4];
			int mask32bit = mask.get(index_);
			mask_[0] = (byte)((mask32bit & 0x000000FF) >>  0);
			mask_[1] = (byte)((mask32bit & 0x0000FF00) >>  8);
			mask_[2] = (byte)((mask32bit & 0x00FF0000) >> 16);
			mask_[3] = (byte)((mask32bit & 0xFF000000) >> 24);
			return mask_;
		} else {
			return null;
		}
	}

	/** 
	 * setMask<P>
	 * This method sets the portion of the mask
	 * specified by index_.
	 * The values range from 0 to 7.
	 * @param index_ An index into the array of Integers
	 * @param value_ The mask value to write
	 */
	public void setMask(int index_, int value_) {
		if ((index_ >= (byte)0x00) && (index_ <= (byte)0x07)) {
			mask.add(index_, value_);
		}
	}

	/** 
	 * setMask<P>
	 * This method sets the portion of the mask
	 * specified by index_.
	 * The values range from 0 to 7.
	 * @param index_ An index into the array of Integers
	 * @param value_ The mask value to write
	 */
	public void setMask(int index_, Number value_) {
		if ((index_ >= (byte)0x00) && (index_ <= (byte)0x07)) {
			if (value_ != null) {
				mask.add(index_, value_.intValue());				
			}
		}
	}

	/** 
	 * getTargetFlag<P>
	 * This method returns the targeted session flag
	 * for this select criteria.
	 * The values S0, S1, S2, S3 and SL.
	 * @return A CmdTagSelect.Target enumeration.
	 */
	public CmdTagSelect.Target getTargetFlag() {
		return targetFlag;
	}

	/** 
	 * setTargetFlag<P>
	 * This method sets the targeted session flag
	 * for this select criteria.
	 * The values S0, S1, S2, S3 and SL.
	 * @param target_ A CmdTagSelect.Target enumeration.
	 */
	public void setTargetFlag(CmdTagSelect.Target targetFlag_) {
		targetFlag = targetFlag_;
	}

	/** 
	 * setTargetFlag<P>
	 * This method sets the targeted session flag
	 * for this select criteria.
	 * The values "S0", "S1", "S2", "S3" and "Selected".
	 * @param target_ A String.
	 */
	public void setTargetFlag(String targetFlag_) {
		if (targetFlag_ != null) {
			try {
				targetFlag = CmdTagSelect.Target.valueOf(targetFlag_);			
	        } catch(IllegalArgumentException iae) {
	        	targetFlag = CmdTagSelect.Target.S1;		
	        }
		}
	}

	/** 
	 * getAction<P>
	 * This method returns the specified action to be taken
	 * for this select criteria.
	 * The values Action_0 - Action_7.
	 * @return A CmdTagProtocol.Action enumeration.
	 */
	public Byte getAction() {
		return action;
	}

	/** 
	 * setAction<P>
	 * This method sets the specified action to be taken
	 * for this select criteria.
	 * The values are 0 - 7.
	 * @param bank_ A CmdTagProtocol.Action enumeration.
	 */
	public void setAction(Byte action_) {
		action = action_;
	}

	/** 
	 * setAction<P>
	 * This method sets the specified action to be taken
	 * for this select criteria.
	 * The values are 0 - 7.
	 * @param action_ A String.
	 */
	public void setAction(String action_) {
		if (action_ != null) {
			try {
				action = Byte.valueOf(action_);			
	        } catch(IllegalArgumentException iae) {
	        	action = 0;
	        }
		}
	}

	/** 
	 * getTruncation<P>
	 * This method returns the truncation value used
	 * for this select criteria.
	 * The values are "Disable" and "Enable".
	 * @return A CmdTagSelect.Truncation enumeration.
	 */
	public CmdTagSelect.Truncation getTruncation() {
		return truncation;
	}

	/** 
	 * setTruncation<P>
	 * This method sets the truncation value used
	 * for this select criteria.
	 * The values are "Disable" and "Enable".
	 * @param truncation_ A CmdTagSelect.Truncation enumeration.
	 */
	public void setTruncation(CmdTagSelect.Truncation truncation_) {
		truncation = truncation_;			
	}

	/** 
	 * setTruncation<P>
	 * This method sets the truncation value used
	 * for this select criteria.
	 * The values are "Disable" and "Enable".
	 * @param truncation_ The truncation value.
	 */
	public void setTruncation(String truncation_) {
		if (truncation_ != null) {
			try {
				truncation = CmdTagSelect.Truncation.valueOf(truncation_);			
	        } catch(IllegalArgumentException iae) {
	        	truncation = CmdTagSelect.Truncation.Disable;			
	        }
		}
	}
	
	/** 
	 * getActiveState<P>
	 * This method returns the ActiveState parameter for this criteria.
	 * The values are Enabled and Disable.
	 * @return A CmdTagProtocol.ActiveState enumeration.
	 */
	public CmdTagSelect.ActiveState getActiveState() {
		return activeState;
	}

	/** 
	 * setActiveState<P>
	 * This method sets the ActiveState parameter for this criteria.
	 * The values are Enabled and Disable.
	 * @param activeState_ A CmdTagProtocol.ActiveState enumeration.
	 */
	public void setActiveState(CmdTagSelect.ActiveState activeState_) {
		activeState = activeState_;
	}

	/** 
	 * setActiveState<P>
	 * This method sets the ActiveState parameter for this criteria.
	 * The values are Enabled and Disable.
	 * @param activeState_ A string representation of the ActiveState parameter.
	 */
	public void setActiveState(String activeState_) {
		if (activeState_ != null) {
			try {
				activeState = CmdTagSelect.ActiveState.valueOf(activeState_);			
	        } catch(IllegalArgumentException iae) {
	        	activeState = CmdTagSelect.ActiveState.Disabled;
	        }
		}
	}
}
