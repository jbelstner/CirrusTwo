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

public class PostMatchCriteria {
	
	public final int MASK_LENGTH = 8;
	private CmdTagSelect.Match matchState = CmdTagSelect.Match.Exclude;
	private Short offset = 0;
	private Short count = 0;
	private ArrayList<Integer> mask = null;

	/** 
	 * PostMatchCriteria<P>
	 * Class Constructor
	 */
	public PostMatchCriteria( ) {
		mask = new ArrayList<Integer>();
		for (int i = 0; i < MASK_LENGTH; i++) {
			mask.add(i, 0);
		}
	}

	/** 
	 * getOffset<P>
	 * This method returns the offset in bits from the start
	 * of bank that the specified mask will be matched against.
	 * The values range from 0 to 511.
	 * @return The bit offset.
	 */
	public Short getOffset() {
		return offset;
	}

	/** 
	 * setOffset<P>
	 * This method sets the offset in bits from the start
	 * of bank that the specified mask will be matched against.
	 * The values range from 0 to 511.
	 * @param offset_ The bit offset.
	 */
	public void setOffset(Short offset_) {
		offset = offset_;
	}

	/** 
	 * setOffset<P>
	 * This method sets the offset in bits from the start
	 * of bank that the specified mask will be matched against.
	 * The values range from 0 to 511.
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
	 * The values range from 0 to 496.
	 * @return The tag mask length.
	 */
	public Short getCount() {
		return count;
	}

	/** 
	 * setCount<P>
	 * This method sets the number of bits in the tag mask.
	 * The values range from 0 to 496.
	 * @param count_ The tag mask length.
	 */
	public void setCount(Short count_) {
		count = count_;
	}

	/** 
	 * setCount<P>
	 * This method sets the number of bits in the tag mask.
	 * The values range from 0 to 496.
	 * @param count_ The tag mask length.
	 */
	public void setCount(Number count_) {
		if (count_ != null) {
			count = count_.shortValue();
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
			mask.add(index_, value_.intValue());
		}
	}

	/** 
	 * getMatchState<P>
	 * This method returns the Match parameter for this criteria.
	 * The values are Include and Exclude.
	 * @return A CmdTagProtocol.Match enumeration.
	 */
	public CmdTagSelect.Match getMatchState() {
		return matchState;
	}

	/** 
	 * setMatchState<P>
	 * This method sets the Match parameter for this criteria.
	 * The values are Include and Exclude.
	 * @param matchState_ A CmdTagProtocol.Match enumeration.
	 */
	public void setMatchState(CmdTagSelect.Match matchState_) {
		matchState = matchState_;
	}

	/** 
	 * setMatchState<P>
	 * This method sets the Match parameter for this criteria.
	 * The values are Include and Exclude.
	 * @param matchState_ A string representation of the Match parameter.
	 */
	public void setMatchState(String matchState_) {
		if (matchState_ != null) {
			try {
				matchState = CmdTagSelect.Match.valueOf(matchState_);			
	        } catch(IllegalArgumentException iae) {
	        	matchState = CmdTagSelect.Match.Exclude;
	        }
		}
	}
}
