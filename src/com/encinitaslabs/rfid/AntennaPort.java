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

import com.encinitaslabs.rfid.cmd.CmdAntennaPortConf;

public class AntennaPort {

	// Antenna Configuration Parameters
	private CmdAntennaPortConf.State portState = CmdAntennaPortConf.State.Disabled;
	private Float powerLevel = (float) 30;
	private Integer numPhysicalPorts = 1;
	private Integer dwellTime = 2000;
	private Integer invCycles = 0;
	private Integer physicalPort = 0;

	/** 
	 * AntennaPort<P>
	 * Class Constructor
	 */
	public AntennaPort( Integer numPhysicalPorts_ ) {
		if (numPhysicalPorts_ != null) {
			numPhysicalPorts = numPhysicalPorts_;
		}
	}

	/** 
	 * getPortState<P>
	 * This method returns the enabled state for this port. 
	 * @return A CmdAntennaPortConf.State enumeration.
	 */
	public CmdAntennaPortConf.State getPortState() {
		return portState;
	}

	/** 
	 * setPortState<P>
	 * This method sets the enabled state for this port. 
	 * @param portState_ A CmdAntennaPortConf.State enumeration.
	 */
	public void setPortState(CmdAntennaPortConf.State portState_) {
		portState = portState_;
	}

	/** 
	 * setPortState<P>
	 * This method sets the enabled state for this port. 
	 * @param portState_ A String.
	 */
	public void setPortState(String portState_) {
		if (portState_ != null) {
			try {
				portState = CmdAntennaPortConf.State.valueOf(portState_);			
	        } catch(IllegalArgumentException iae) {
	        	portState = CmdAntennaPortConf.State.Disabled;
	        }
		}
	}

	/** 
	 * getPowerLevel<P>
	 * This method returns the power level used for this 
	 * The values can range from 0 to 30.
	 * @return The power level in units of dBm.
	 */
	public Float getPowerLevel() {
		return powerLevel;
	}

	/** 
	 * setPowerLevel<P>
	 * This method sets the power level used for this 
	 * The values can range from 0 to 30.
	 * @param powerLevel_ The power level in units of dBm.
	 */
	public void setPowerLevel(float powerLevel_) {
		powerLevel = powerLevel_;
	}

	/** 
	 * setPowerLevel<P>
	 * This method sets the power level used for this 
	 * The values can range from 0 to 30.
	 * @param powerLevel_ The power level in units of dBm.
	 */
	public void setPowerLevel(Number powerLevel_) {
		if (powerLevel_ != null) {
			powerLevel = powerLevel_.floatValue();
		}
	}

	/** 
	 * getDwellTime<P>
	 * This method returns the DwellTime used for this 
	 * This value is the maximum amount of time in milliseconds that may be
	 * spent on the logical antenna port during a tag-protocol-operation
	 * cycle before switching to the next enabled antenna port. A value of
	 * zero indicates that there is no maximum dwell time for this antenna
	 * port. If this parameter is zero, then NumberInventoryCycles may not
	 * be zero.
	 * @return The Dwell Time in ms.
	 */
	public Integer getDwellTime() {
		return dwellTime;
	}

	/** 
	 * setDwellTime<P>
	 * This method sets the DwellTime used for this 
	 * This value is the maximum amount of time in milliseconds that may be
	 * spent on the logical antenna port during a tag-protocol-operation
	 * cycle before switching to the next enabled antenna port. A value of
	 * zero indicates that there is no maximum dwell time for this antenna
	 * port. If this parameter is zero, then NumberInventoryCycles may not
	 * be zero.
	 * @param dwellTime_ The Dwell Time in ms.
	 */
	public void setDwellTime(int dwellTime_) {
		dwellTime = dwellTime_;
	}

	/** 
	 * setDwellTime<P>
	 * This method sets the DwellTime used for this 
	 * This value is the maximum amount of time in milliseconds that may be
	 * spent on the logical antenna port during a tag-protocol-operation
	 * cycle before switching to the next enabled antenna port. A value of
	 * zero indicates that there is no maximum dwell time for this antenna
	 * port. If this parameter is zero, then NumberInventoryCycles may not
	 * be zero.
	 * @param dwellTime_ The Dwell Time in ms.
	 */
	public void setDwellTime(Number dwellTime_) {
		if (dwellTime_ != null) {
			dwellTime = dwellTime_.intValue();
		}
	}

	/** 
	 * getInvCycles<P>
	 * This method returns the NumberInventoryCycles used for this 
	 * Specifies the maximum number of inventory cycles to attempt on the
	 * antenna port during a tag-protocol-operation cycle before switching
	 * to the next enabled antenna port. An inventory cycle consists of one
	 * or more executions of the singulation algorithm for a particular
	 * inventory-session target (i.e. A or B). If the singulation algorithm
	 * parameters are configured to toggle the inventory-session, executing
	 * the singulation algorithm for inventory session A and inventory
	 * session B counts as two inventory cycles. A value of zero indicates
	 * that there is no maximum number of inventory cycles for this antenna
	 * port. If this parameter is zero, then DwellTime may not be zero.
	 * @return The NumberInventoryCycles value.
	 */
	public Integer getInvCycles() {
		return invCycles;
	}

	/** 
	 * setInvCycles<P>
	 * This method sets the NumberInventoryCycles used for this 
	 * Specifies the maximum number of inventory cycles to attempt on the
	 * antenna port during a tag-protocol-operation cycle before switching
	 * to the next enabled antenna port. An inventory cycle consists of one
	 * or more executions of the singulation algorithm for a particular
	 * inventory-session target (i.e. A or B). If the singulation algorithm
	 * parameters are configured to toggle the inventory-session, executing
	 * the singulation algorithm for inventory session A and inventory
	 * session B counts as two inventory cycles. A value of zero indicates
	 * that there is no maximum number of inventory cycles for this antenna
	 * port. If this parameter is zero, then DwellTime may not be zero.
	 * @param numberInventoryCycles_ The NumberInventoryCycles value.
	 */
	public void setInvCycles(int invCycles_) {
		invCycles = invCycles_;
	}

	/** 
	 * setInvCycles<P>
	 * This method sets the NumberInventoryCycles used for this 
	 * Specifies the maximum number of inventory cycles to attempt on the
	 * antenna port during a tag-protocol-operation cycle before switching
	 * to the next enabled antenna port. An inventory cycle consists of one
	 * or more executions of the singulation algorithm for a particular
	 * inventory-session target (i.e. A or B). If the singulation algorithm
	 * parameters are configured to toggle the inventory-session, executing
	 * the singulation algorithm for inventory session A and inventory
	 * session B counts as two inventory cycles. A value of zero indicates
	 * that there is no maximum number of inventory cycles for this antenna
	 * port. If this parameter is zero, then DwellTime may not be zero.
	 * @param numberInventoryCycles_ The NumberInventoryCycles value.
	 */
	public void setInvCycles(Number invCycles_) {
		if (invCycles_ != null) {
			invCycles = invCycles_.intValue();
		}
	}

	/** 
	 * getPhysicalPort<P>
	 * This method returns the antenna port used for this 
	 * The values can range from 0 to 1.
	 * @return The antenna port.
	 */
	public Integer getPhysicalPort() {
		return physicalPort;
	}

	/** 
	 * setPhysicalPort<P>
	 * This method sets the antenna port used for this 
	 * The values can range from 0 to 1.
	 * @param physicalPort The antenna port.
	 */
	public void setPhysicalPort(int physicalPort_) {
		if (physicalPort_ < numPhysicalPorts) {
			physicalPort = physicalPort_;			
		}
	}

	/** 
	 * setPhysicalPort<P>
	 * This method sets the antenna port used for this 
	 * The values can range from 0 to 1.
	 * @param physicalPort The antenna port.
	 */
	public void setPhysicalPort(Number physicalPort_) {
		if (physicalPort_ != null) {
			if (physicalPort_.intValue() < numPhysicalPorts) {
				physicalPort = physicalPort_.intValue();			
			}
		}
	}
}
