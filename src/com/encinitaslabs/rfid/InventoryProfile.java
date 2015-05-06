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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.encinitaslabs.rfid.cmd.CmdReaderModuleConfig;
import com.encinitaslabs.rfid.cmd.CmdTagAccess;
import com.encinitaslabs.rfid.cmd.CmdTagProtocol;

/**
 * ReaderInventoryProfile Object
 * <P>Attributes and functionality corresponding to a Reader Profile.
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class InventoryProfile {
	private String profileFilename = "Default.conf";
	// RF Configuration Parameters
	private CmdReaderModuleConfig.OperationMode operationMode = CmdReaderModuleConfig.OperationMode.NonContinuous;
	private Integer linkProfile = 1;

	// Antenna Configuration Parameters
	private Integer numVirtualPorts = 1;
	private Float defaultPowerLevel = (float) 30;
	private Integer defaultDwellTime = 2000;
	private Integer defaultInvCycles = 0;

	// Tag Query Configuration Parameters
	private CmdTagAccess.Selected selectedState = CmdTagAccess.Selected.Any;
	private CmdTagAccess.Session sessionFlag = CmdTagAccess.Session.S1;
	private CmdTagAccess.Target targetState = CmdTagAccess.Target.A;

	// Q Algorithm Parameters
	private CmdTagAccess.Algorithm algorithm = CmdTagAccess.Algorithm.FixedQ;
	private Integer fixedQValue = 7;
	private Integer startQValue = 3;
	private Integer minQValue = 3;
	private Integer maxQValue = 15;
	private Integer retryCount = 0;
	private CmdTagAccess.ToggleTarget toggleTargetFlag = CmdTagAccess.ToggleTarget.Yes;
	private CmdTagAccess.RepeatUntilNoTags repeatUntilNoTags = CmdTagAccess.RepeatUntilNoTags.No;
	private Integer thresholdMultiplier = 0;
	
	// Tag Reporting Format Parameters
	private CmdTagProtocol.PerformGuardMode performGuardMode = CmdTagProtocol.PerformGuardMode.RealtimeMode;
		
	// Tag Filtering Parameters
	private CmdTagProtocol.PerformSelect performSelect = CmdTagProtocol.PerformSelect.No;
	private CmdTagProtocol.PerformPostMatch performPostMatch = CmdTagProtocol.PerformPostMatch.No;
		
	/** 
	 * InventoryProfile<P>
	 * @param filename Inventory Profile Config file
	 * Class Constructor
	 * @throws IOException 
	 */
	public InventoryProfile(String filename) throws IOException {
		if (filename != null) {
			profileFilename = filename;
			BufferedReader br = null;
			String currentLine;
			br = new BufferedReader(new FileReader(filename));

			while ((currentLine = br.readLine()) != null) {
				String st[] = currentLine.split(" ");
				if (currentLine.startsWith("#") || (st.length == 1)) {
					// Do nothing as this is a comment or no parameter
				} else if (currentLine.startsWith("OPERATION_MODE") && (st.length == 2)) {
					this.operationMode = CmdReaderModuleConfig.OperationMode.valueOf(st[1]);
					if (this.operationMode == null) {
						this.operationMode = CmdReaderModuleConfig.OperationMode.NonContinuous;
					}
				} else if (currentLine.startsWith("LINK_PROFILE") && (st.length == 2)) {
					this.linkProfile = Integer.parseInt(st[1]);
					if ((this.linkProfile < 0) || (this.linkProfile > 3)) {
						this.linkProfile = 1;
					}
				} else if (currentLine.startsWith("NUM_VIRTUAL_PORTS") && (st.length == 2)) {
					this.numVirtualPorts = Integer.parseInt(st[1]);
					if ((this.numVirtualPorts < 0) || (this.numVirtualPorts > 1)) {
						this.numVirtualPorts = 1;
					}
				} else if (currentLine.startsWith("POWER_LEVEL") && (st.length == 2)) {
					this.defaultPowerLevel = Float.parseFloat(st[1]);
					if ((this.defaultPowerLevel < 0) || (this.defaultPowerLevel > 33)) {
						this.defaultPowerLevel = (float)30;
					}
				} else if (currentLine.startsWith("DWELL_TIME") && (st.length == 2)) {
					this.defaultDwellTime = Integer.parseInt(st[1]);
					if ((this.defaultDwellTime < 0) || (this.defaultDwellTime > 65535)) {
						this.defaultDwellTime = 2000;
					}
				} else if (currentLine.startsWith("INV_CYCLES") && (st.length == 2)) {
					this.defaultInvCycles = Integer.parseInt(st[1]);
					if ((this.defaultInvCycles < 0) || (this.defaultInvCycles > 65535)) {
						this.defaultInvCycles = 0;
					}
					if ((this.defaultInvCycles == 0) && (this.defaultDwellTime == 0)) {
						// Both cannot be zero so set numberInventoryCycles = 1
						this.defaultInvCycles = 1;
					}
				} else if (currentLine.startsWith("SELECTED_STATE") && (st.length == 2)) {
					this.selectedState = CmdTagAccess.Selected.valueOf(st[1]);
					if (this.selectedState == null) {
						this.selectedState = CmdTagAccess.Selected.Any;
					}
				} else if (currentLine.startsWith("SESSION_FLAG") && (st.length == 2)) {
					this.sessionFlag = CmdTagAccess.Session.valueOf(st[1]);
					if (this.sessionFlag == null) {
						this.sessionFlag = CmdTagAccess.Session.S1;
					}
				} else if (currentLine.startsWith("TARGET_STATE") && (st.length == 2)) {
					this.targetState = CmdTagAccess.Target.valueOf(st[1]);
					if (this.targetState == null) {
						this.targetState = CmdTagAccess.Target.A;
					}
				} else if (currentLine.startsWith("ALGORITHM") && (st.length == 2)) {
					this.algorithm = CmdTagAccess.Algorithm.valueOf(st[1]);
					if (this.algorithm == null) {
						this.algorithm = CmdTagAccess.Algorithm.FixedQ;
					}
				} else if (currentLine.startsWith("FIXED_Q_VALUE") && (st.length == 2)) {
					this.fixedQValue = Integer.parseInt(st[1]);
					if ((this.fixedQValue < 0) || (this.fixedQValue > 15)) {
						this.fixedQValue = 7;
					}
				} else if (currentLine.startsWith("START_Q_VALUE") && (st.length == 2)) {
					this.startQValue = Integer.parseInt(st[1]);
					if ((this.startQValue < 0) || (this.startQValue > 15)) {
						this.startQValue = 3;
					}
				} else if (currentLine.startsWith("MIN_Q_VALUE") && (st.length == 2)) {
					this.minQValue = Integer.parseInt(st[1]);
					if ((this.minQValue < 0) || (this.minQValue > 15)) {
						this.minQValue = 3;
					}
				} else if (currentLine.startsWith("MAX_Q_VALUE") && (st.length == 2)) {
					this.maxQValue = Integer.parseInt(st[1]);
					if ((this.maxQValue < 0) || (this.maxQValue > 15)) {
						this.maxQValue = 15;
					}
				} else if (currentLine.startsWith("RETRY_COUNT") && (st.length == 2)) {
					this.retryCount = Integer.parseInt(st[1]);
					if ((this.retryCount < 0) || (this.retryCount > 255)) {
						this.retryCount = 0;
					}
				} else if (currentLine.startsWith("TOGGLE_TARGET_FLAG") && (st.length == 2)) {
					this.toggleTargetFlag = CmdTagAccess.ToggleTarget.valueOf(st[1]);
					if (this.toggleTargetFlag == null) {
						this.toggleTargetFlag = CmdTagAccess.ToggleTarget.Yes;
					}
				} else if (currentLine.startsWith("REPEAT_UNTIL_NO_TAGS") && (st.length == 2)) {
					this.repeatUntilNoTags = CmdTagAccess.RepeatUntilNoTags.valueOf(st[1]);
					if (this.repeatUntilNoTags == null) {
						this.repeatUntilNoTags = CmdTagAccess.RepeatUntilNoTags.No;
					}
				} else if (currentLine.startsWith("THRESHOLD_MULTIPLIER") && (st.length == 2)) {
					this.thresholdMultiplier = Integer.parseInt(st[1]);
					if ((this.thresholdMultiplier < 0) || (this.thresholdMultiplier > 255)) {
						this.thresholdMultiplier = 0;
					}
				} else if (currentLine.startsWith("PERFORM_GUARD_MODE") && (st.length == 2)) {
					this.performGuardMode = CmdTagProtocol.PerformGuardMode.valueOf(st[1]);
					if (this.performGuardMode == null) {
						this.performGuardMode = CmdTagProtocol.PerformGuardMode.RealtimeMode;
					}
				}
			}
			br.close();
		}
	}
	
	/** 
	 * getProfileFilename<P>
	 * This method returns the current profile filename 
	 * @return The Profile Filename.
	 */
	public String getProfileFilename() {
		return profileFilename;
	}

	/** 
	 * getNumVirtualPorts<P>
	 * This method returns the number of antenna port
	 * currently configured on this device.
	 * The values can range from 1 to 16.
	 * @return The number of virtual antenna ports.
	 */
	public Integer getNumVirtualPorts() {
		return numVirtualPorts;
	}

	/** 
	 * setNumVirtualPorts<P>
	 * This method returns the number of antenna port
	 * currently configured on this device.
	 * The values can range from 1 to 16.
	 * @param numVirtualPorts_ The number of virtual antenna ports.
	 */
	public void setNumVirtualPorts(int numVirtualPorts_) {
		numVirtualPorts = numVirtualPorts_;
	}

	/** 
	 * setNumVirtualPorts<P>
	 * This method returns the number of antenna port
	 * currently configured on this device.
	 * The values can range from 1 to 16.
	 * @param numVirtualPorts_ The number of virtual antenna ports.
	 */
	public void setNumVirtualPorts(Number numVirtualPorts_) {
		if (numVirtualPorts_ != null) {
			numVirtualPorts = numVirtualPorts_.intValue();
		}
	}

	/** 
	 * getDefaultPowerLevel<P>
	 * This method returns the power level used for this 
	 * The values can range from 0 to 30.
	 * @return The power level in units of dBm.
	 */
	public Float getDefaultPowerLevel() {
		return defaultPowerLevel;
	}

	/** 
	 * setDefaultPowerLevel<P>
	 * This method sets the power level used for this 
	 * The values can range from 0 to 30.
	 * @param powerLevel_ The power level in units of dBm.
	 */
	public void setDefaultPowerLevel(float powerLevel_) {
		defaultPowerLevel = powerLevel_;
	}

	/** 
	 * setDefaultPowerLevel<P>
	 * This method sets the power level used for this 
	 * The values can range from 0 to 30.
	 * @param powerLevel_ The power level in units of dBm.
	 */
	public void setDefaultPowerLevel(Number powerLevel_) {
		if (powerLevel_ != null) {
			defaultPowerLevel = powerLevel_.floatValue();
		}
	}

	/** 
	 * getOperationMode<P>
	 * This method returns the Operation Mode for this Profile.
	 * The values are Continuous and NonContinuous.
	 * @return A CmdReaderModuleConfig.OperationMode enumeration.
	 */
	public CmdReaderModuleConfig.OperationMode getOperationMode() {
		return operationMode;
	}

	/** 
	 * setOperationMode<P>
	 * This method sets the Operation Mode for this Profile.
	 * The values are Continuous and NonContinuous.
	 * @param operationMode_ A CmdReaderModuleConfig.OperationMode enumeration.
	 */
	public void setOperationMode(CmdReaderModuleConfig.OperationMode operationMode_) {
		operationMode = operationMode_;
	}
	
	/** 
	 * setOperationMode<P>
	 * This method sets the Operation Mode for this Profile.
	 * The values are Continuous and NonContinuous.
	 * @param operationMode_ A string representation of the Operation Mode.
	 */
	public void setOperationMode(String operationMode_) {
		if (operationMode_ != null) {
			try {
				operationMode = CmdReaderModuleConfig.OperationMode.valueOf(operationMode_);
	        } catch(IllegalArgumentException iae) {
				operationMode = CmdReaderModuleConfig.OperationMode.NonContinuous;
	        }
		}
	}

	/** 
	 * getAlgorithm<P>
	 * This method returns the Singulation Algorithm for this Profile.
	 * The values are Fixed and Dynamic.
	 * @return A CmdTagAccess.Algorithm enumeration.
	 */
	public CmdTagAccess.Algorithm getAlgorithm() {
		return algorithm;
	}

	/** 
	 * setAlgorithm<P>
	 * This method sets the Singulation Algorithm for this Profile.
	 * The values are Fixed and Dynamic.
	 * @param algorithm_ A CmdTagAccess.Algorithm enumeration.
	 */
	public void setAlgorithm(CmdTagAccess.Algorithm algorithm_) {
		algorithm = algorithm_;
	}

	/** 
	 * setAlgorithm<P>
	 * This method sets the Singulation Algorithm for this Profile.
	 * The values are Fixed and Dynamic.
	 * @param algorithm_ A string representation of the Singulation Algorithm.
	 */
	public void setAlgorithm(String algorithm_) {
		if (algorithm_ != null) {
			try {
				algorithm = CmdTagAccess.Algorithm.valueOf(algorithm_);
	        } catch(IllegalArgumentException iae) {
				algorithm = CmdTagAccess.Algorithm.FixedQ;
	        }
		}
	}

	/** 
	 * getRepeatUntilNoTags<P>
	 * This method returns the RepeatUntilNoTags parameter for this Profile.
	 * The values are No and Yes.
	 * @return A CmdTagAccess.RepeatUntilNoTags enumeration.
	 */
	public CmdTagAccess.RepeatUntilNoTags getRepeatUntilNoTags() {
		return repeatUntilNoTags;
	}

	/** 
	 * setRepeatUntilNoTags<P>
	 * This method sets the RepeatUntilNoTags parameter for this Profile.
	 * The values are No and Yes.
	 * @param repeatUntilNoTags_ A CmdTagAccess.RepeatUntilNoTags enumeration.
	 */
	public void setRepeatUntilNoTags(CmdTagAccess.RepeatUntilNoTags repeatUntilNoTags_) {
		repeatUntilNoTags = repeatUntilNoTags_;
	}

	/** 
	 * setRepeatUntilNoTags<P>
	 * This method sets the RepeatUntilNoTags parameter for this Profile.
	 * The values are No and Yes.
	 * @param repeatUntilNoTags_ A string representation of the RepeatUntilNoTags parameter.
	 */
	public void setRepeatUntilNoTags(String repeatUntilNoTags_) {
		if (repeatUntilNoTags_ != null) {
			try {
				repeatUntilNoTags = CmdTagAccess.RepeatUntilNoTags.valueOf(repeatUntilNoTags_);
	        } catch(IllegalArgumentException iae) {
				repeatUntilNoTags = CmdTagAccess.RepeatUntilNoTags.No;
	        }
		}
	}

	/** 
	 * getSelectedState<P>
	 * This method returns the Selected parameter for this Profile.
	 * The values are All, Deasserted and Asserted.
	 * @return A CmdTagAccess.Selected enumeration.
	 */
	public CmdTagAccess.Selected getSelectedState() {
		return selectedState;
	}

	/** 
	 * setSelectedState<P>
	 * This method sets the Selected parameter for this Profile.
	 * The values are All, Deasserted and Asserted.
	 * @param selectedState_ A CmdTagAccess.Selected enumeration.
	 */
	public void setSelectedState(CmdTagAccess.Selected selectedState_) {
		selectedState = selectedState_;
	}

	/** 
	 * setSelectedState<P>
	 * This method sets the Selected parameter for this Profile.
	 * The values are All, Deasserted and Asserted.
	 * @param selectedState_ A string representation of the Selected parameter.
	 */
	public void setSelectedState(String selectedState_) {
		if (selectedState_ != null) {
			try {
				selectedState = CmdTagAccess.Selected.valueOf(selectedState_);
	        } catch(IllegalArgumentException iae) {
				selectedState = CmdTagAccess.Selected.Any;
	        }
		}
	}

	/** 
	 * getSession<P>
	 * This method returns the Session parameter for this Profile.
	 * The values are S0, S1, S2 and S3.
	 * @return A CmdTagAccess.Session enumeration.
	 */
	public CmdTagAccess.Session getSessionFlag() {
		return sessionFlag;
	}

	/** 
	 * setSession<P>
	 * This method sets the Session parameter for this Profile.
	 * The values are S0, S1, S2 and S3.
	 * @param sessionFlag_ A CmdTagAccess.Session enumeration.
	 */
	public void setSessionFlag(CmdTagAccess.Session sessionFlag_) {
		sessionFlag = sessionFlag_;
	}

	/** 
	 * setSession<P>
	 * This method sets the Session parameter for this Profile.
	 * The values are S0, S1, S2 and S3.
	 * @param sessionFlag_ A string representation of the Session parameter.
	 */
	public void setSessionFlag(String sessionFlag_) {
		if (sessionFlag_ != null) {
			try {
				sessionFlag = CmdTagAccess.Session.valueOf(sessionFlag_);
	        } catch(IllegalArgumentException iae) {
				sessionFlag = CmdTagAccess.Session.S1;
	        }
		}
	}

	/** 
	 * getTarget<P>
	 * This method returns the Target parameter for this Profile.
	 * The values are A and B.
	 * @return A CmdTagAccess.Target enumeration.
	 */
	public CmdTagAccess.Target getTargetState() {
		return targetState;
	}

	/** 
	 * setTarget<P>
	 * This method sets the Target parameter for this Profile.
	 * The values are A and B.
	 * @param targetState_ A CmdTagAccess.Target enumeration.
	 */
	public void setTargetState(CmdTagAccess.Target targetState_) {
		targetState = targetState_;
	}

	/** 
	 * setTarget<P>
	 * This method sets the Target parameter for this Profile.
	 * The values are A and B.
	 * @param targetState_ A string representation of the Target parameter.
	 */
	public void setTargetState(String targetState_) {
		if (targetState_ != null) {
			try {
				targetState = CmdTagAccess.Target.valueOf(targetState_);
	        } catch(IllegalArgumentException iae) {
				targetState = CmdTagAccess.Target.A;
	        }
		}
	}

	/** 
	 * getToggleTarget<P>
	 * This method returns the ToggleTarget parameter for this Profile.
	 * The values are NotToggled and Toggled.
	 * @return A CmdTagAccess.ToggleTarget enumeration.
	 */
	public CmdTagAccess.ToggleTarget getToggleTargetFlag() {
		return toggleTargetFlag;
	}

	/** 
	 * setToggleTarget<P>
	 * This method sets the ToggleTarget parameter for this Profile.
	 * The values are NotToggled and Toggled.
	 * @param toggleTargetFlag_ A CmdTagAccess.ToggleTarget enumeration.
	 */
	public void setToggleTargetFlag(CmdTagAccess.ToggleTarget toggleTargetFlag_) {
		toggleTargetFlag = toggleTargetFlag_;
	}

	/** 
	 * setToggleTarget<P>
	 * This method sets the ToggleTarget parameter for this Profile.
	 * The values are NotToggled and Toggled.
	 * @param toggleTargetFlag_ A string representation of the ToggleTarget parameter.
	 */
	public void setToggleTargetFlag(String toggleTargetFlag_) {
		if (toggleTargetFlag_ != null) {
			try {
				toggleTargetFlag = CmdTagAccess.ToggleTarget.valueOf(toggleTargetFlag_);			
	        } catch(IllegalArgumentException iae) {
				toggleTargetFlag = CmdTagAccess.ToggleTarget.Yes;
	        }
		}
	}

	/** 
	 * getPerformGuardMode<P>
	 * This method returns the PerformGuardMode parameter for this Profile.
	 * The values are RealtimeMode, ScreeningMode, NoScreeningDisCmdWorkMode,
	 * ScreeningDisCmdWorkMode, NoScreeningEnCmdWorkMode and ScreeningEnCmdWorkMode.
	 * @return A CmdTagProtocol.PerformGuardMode enumeration.
	 */
	public CmdTagProtocol.PerformGuardMode getPerformGuardMode() {
		return performGuardMode;
	}

	/** 
	 * setPerformGuardMode<P>
	 * This method sets the PerformGuardMode parameter for this Profile.
	 * The values are RealtimeMode, ScreeningMode, NoScreeningDisCmdWorkMode,
	 * ScreeningDisCmdWorkMode, NoScreeningEnCmdWorkMode and ScreeningEnCmdWorkMode.
	 * @param performGuardMode_ A CmdTagProtocol.PerformGuardMode enumeration.
	 */
	public void setPerformGuardMode(CmdTagProtocol.PerformGuardMode performGuardMode_) {
		performGuardMode = performGuardMode_;
	}

	/** 
	 * setPerformGuardMode<P>
	 * This method sets the PerformGuardMode parameter for this Profile.
	 * The values are RealtimeMode, ScreeningMode, NoScreeningDisCmdWorkMode,
	 * ScreeningDisCmdWorkMode, NoScreeningEnCmdWorkMode and ScreeningEnCmdWorkMode.
	 * @param performGuardMode_ A string representation of the PerformGuardMode parameter.
	 */
	public void setPerformGuardMode(String performGuardMode_) {
		if (performGuardMode_ != null) {
			try {
				performGuardMode = CmdTagProtocol.PerformGuardMode.valueOf(performGuardMode_);			
	        } catch(IllegalArgumentException iae) {
				performGuardMode = CmdTagProtocol.PerformGuardMode.RealtimeMode;
	        }
		}
	}

	/** 
	 * getPerformSelect<P>
	 * This method returns the PerformSelect parameter for this Profile.
	 * The values are No and Yes.
	 * @return A CmdTagProtocol.PerformSelect enumeration.
	 */
	public CmdTagProtocol.PerformSelect getPerformSelect() {
		return performSelect;
	}

	/** 
	 * setPerformSelect<P>
	 * This method returns the PerformSelect parameter for this Profile.
	 * The values are No and Yes.
	 * @param performSelect_ A CmdTagProtocol.PerformSelect enumeration.
	 */
	public void setPerformSelect(CmdTagProtocol.PerformSelect performSelect_) {
		performSelect = performSelect_;
	}

	/** 
	 * setPerformSelect<P>
	 * This method returns the PerformSelect parameter for this Profile.
	 * The values are No and Yes.
	 * @param performSelect_ A string representation of the PerformSelect parameter.
	 */
	public void setPerformSelect(String performSelect_) {
		if (performSelect_ != null) {
			try {
				performSelect = CmdTagProtocol.PerformSelect.valueOf(performSelect_);			
	        } catch(IllegalArgumentException iae) {
	        	performSelect = CmdTagProtocol.PerformSelect.No;
	        }
		}
	}

	/** 
	 * getPerformPostMatch<P>
	 * This method returns the PerformPostMatch parameter for this Profile.
	 * The values are No and Yes.
	 * @return A CmdTagProtocol.PerformPostMatch enumeration.
	 */
	public CmdTagProtocol.PerformPostMatch getPerformPostMatch() {
		return performPostMatch;
	}

	/** 
	 * setPerformPostMatch<P>
	 * This method returns the PerformPostMatch parameter for this Profile.
	 * The values are No and Yes.
	 * @param performPostMatch_ A CmdTagProtocol.PerformPostMatch enumeration.
	 */
	public void setPerformPostMatch(CmdTagProtocol.PerformPostMatch performPostMatch_) {
		performPostMatch = performPostMatch_;
	}

	/** 
	 * setPerformPostMatch<P>
	 * This method returns the PerformPostMatch parameter for this Profile.
	 * The values are No and Yes.
	 * @param performPostMatch_ A string representation of the PerformPostMatch parameter.
	 */
	public void setPerformPostMatch(String performPostMatch_) {
		if (performPostMatch_ != null) {
			try {
				performPostMatch = CmdTagProtocol.PerformPostMatch.valueOf(performPostMatch_);			
	        } catch(IllegalArgumentException iae) {
	        	performPostMatch = CmdTagProtocol.PerformPostMatch.No;
	        }
		}
	}

	/** 
	 * getLinkProfile<P>
	 * This method returns the current Link Profile.
	 * The values range from 0 to 3.
	 * @return The Link Profile.
	 */
	public Integer getLinkProfile() {
		return linkProfile;
	}

	/** 
	 * setLinkProfile<P>
	 * This method sets the current Link Profile.
	 * The values range from 0 to 3.
	 * @param linkProfile_ The Link Profile.
	 */
	public void setLinkProfile(int linkProfile_) {
		linkProfile = linkProfile_;
	}

	/** 
	 * setLinkProfile<P>
	 * This method sets the current Link Profile.
	 * The values range from 0 to 3.
	 * @param linkProfile_ The Link Profile.
	 */
	public void setLinkProfile(Number linkProfile_) {
		if (linkProfile_ != null) {
			linkProfile = linkProfile_.intValue();
		}
	}

	/** 
	 * getFixedQValue<P>
	 * This method returns the QValue when fixed Q is used.
	 * The values range from 0 to 15.
	 * @return The fixed Q value.
	 */
	public Integer getFixedQValue() {
		return fixedQValue;
	}

	/** 
	 * setFixedQValue<P>
	 * This method sets the QValue when fixed Q is used.
	 * The values range from 0 to 15.
	 * @param fixedQValue_ The fixed Q value.
	 */
	public void setFixedQValue(int fixedQValue_) {
		fixedQValue = fixedQValue_;
	}

	/** 
	 * setFixedQValue<P>
	 * This method sets the QValue when fixed Q is used.
	 * The values range from 0 to 15.
	 * @param fixedQValue_ The fixed Q value.
	 */
	public void setFixedQValue(Number fixedQValue_) {
		if (fixedQValue_ != null) {
			fixedQValue = fixedQValue_.intValue();
		}
	}

	/** 
	 * getStartQValue<P>
	 * This method returns the StartQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @return The start Q value.
	 */
	public Integer getStartQValue() {
		return startQValue;
	}

	/** 
	 * setStartQValue<P>
	 * This method sets the StartQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @param startQValue_ The start Q value.
	 */
	public void setStartQValue(int startQValue_) {
		startQValue = startQValue_;
	}

	/** 
	 * setStartQValue<P>
	 * This method sets the StartQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @param startQValue_ The start Q value.
	 */
	public void setStartQValue(Number startQValue_) {
		if (startQValue_ != null) {
			startQValue = startQValue_.intValue();
		}
	}

	/** 
	 * getMinQValue<P>
	 * This method returns the MinQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @return The minimum Q value.
	 */
	public Integer getMinQValue() {
		return minQValue;
	}

	/** 
	 * setMinQValue<P>
	 * This method sets the MinQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @param minQValue_ The minimum Q value.
	 */
	public void setMinQValue(int minQValue_) {
		minQValue = minQValue_;
	}

	/** 
	 * setMinQValue<P>
	 * This method sets the MinQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @param minQValue_ The minimum Q value.
	 */
	public void setMinQValue(Number minQValue_) {
		if (minQValue_ != null) {
			minQValue = minQValue_.intValue();
		}
	}

	/** 
	 * getMaxQValue<P>
	 * This method returns the MaxQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @return The maximum Q value.
	 */
	public Integer getMaxQValue() {
		return maxQValue;
	}

	/** 
	 * setMaxQValue<P>
	 * This method sets the MaxQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @param maxQValue_ The maximum Q value.
	 */
	public void setMaxQValue(int maxQValue_) {
		maxQValue = maxQValue_;
	}

	/** 
	 * setMaxQValue<P>
	 * This method sets the MaxQValue when dynamic Q is used.
	 * The values range from 0 to 15.
	 * @param maxQValue_ The maximum Q value.
	 */
	public void setMaxQValue(Number maxQValue_) {
		if (maxQValue_ != null) {
			maxQValue = maxQValue_.intValue();
		}
	}

	/** 
	 * getRetryCount<P>
	 * This method returns the RetryCount used for this 
	 * Specifies the number of times to try another execution of the
	 * singulation algorithm for the specified session/target before either
	 * toggling the target (if ToggleTarget is non-zero) or terminating the
	 * inventory/tag access operation.  The values can range from 0 to 255.
	 * @return The RetryCount value.
	 */
	public Integer getRetryCount() {
		return retryCount;
	}

	/** 
	 * setRetryCount<P>
	 * This method sets the RetryCount used for this 
	 * Specifies the number of times to try another execution of the
	 * singulation algorithm for the specified session/target before either
	 * toggling the target (if ToggleTarget is non-zero) or terminating the
	 * inventory/tag access operation.  The values can range from 0 to 255.
	 * @param retryCount_ The RetryCount value.
	 */
	public void setRetryCount(int retryCount_) {
		retryCount = retryCount_;
	}

	/** 
	 * setRetryCount<P>
	 * This method sets the RetryCount used for this 
	 * Specifies the number of times to try another execution of the
	 * singulation algorithm for the specified session/target before either
	 * toggling the target (if ToggleTarget is non-zero) or terminating the
	 * inventory/tag access operation.  The values can range from 0 to 255.
	 * @param retryCount_ The RetryCount value.
	 */
	public void setRetryCount(Number retryCount_) {
		if (retryCount_ != null) {
			retryCount = retryCount_.intValue();
		}
	}

	/** 
	 * getThresholdMultiplier<P>
	 * This method returns the ThresholdMultiplier when dynamic Q is used.
	 * The multiplier, specified in units of fourths (i.e. 0.25), that will
	 * be applied to the Q-adjustment threshold as part of the dynamic-Q 
	 * algorithm.  The values can range from 0 to 255.
	 * @return The ThresholdMultiplier value.
	 */
	public Integer getThresholdMultiplier() {
		return thresholdMultiplier;
	}

	/** 
	 * setThresholdMultiplier<P>
	 * This method sets the ThresholdMultiplier when dynamic Q is used.
	 * The multiplier, specified in units of fourths (i.e. 0.25), that will
	 * be applied to the Q-adjustment threshold as part of the dynamic-Q 
	 * algorithm.  The values can range from 0 to 255.
	 * @param thresholdMultiplier_ The ThresholdMultiplier value.
	 */
	public void setThresholdMultiplier(int thresholdMultiplier_) {
		thresholdMultiplier = thresholdMultiplier_;
	}

	/** 
	 * setThresholdMultiplier<P>
	 * This method sets the ThresholdMultiplier when dynamic Q is used.
	 * The multiplier, specified in units of fourths (i.e. 0.25), that will
	 * be applied to the Q-adjustment threshold as part of the dynamic-Q 
	 * algorithm.  The values can range from 0 to 255.
	 * @param thresholdMultiplier_ The ThresholdMultiplier value.
	 */
	public void setThresholdMultiplier(Number thresholdMultiplier_) {
		if (thresholdMultiplier_ != null) {
			thresholdMultiplier = thresholdMultiplier_.intValue();
		}
	}

	/** 
	 * getDefaultDwellTime<P>
	 * This method returns the DwellTime used for this 
	 * This value is the maximum amount of time in milliseconds that may be
	 * spent on the logical antenna port during a tag-protocol-operation
	 * cycle before switching to the next enabled antenna port. A value of
	 * zero indicates that there is no maximum dwell time for this antenna
	 * port. If this parameter is zero, then NumberInventoryCycles may not
	 * be zero.
	 * @return The Dwell Time in ms.
	 */
	public Integer getDefaultDwellTime() {
		return defaultDwellTime;
	}

	/** 
	 * setDefaultDwellTime<P>
	 * This method sets the DwellTime used for this 
	 * This value is the maximum amount of time in milliseconds that may be
	 * spent on the logical antenna port during a tag-protocol-operation
	 * cycle before switching to the next enabled antenna port. A value of
	 * zero indicates that there is no maximum dwell time for this antenna
	 * port. If this parameter is zero, then NumberInventoryCycles may not
	 * be zero.
	 * @param dwellTime_ The Dwell Time in ms.
	 */
	public void setDefaultDwellTime(int dwellTime_) {
		defaultDwellTime = dwellTime_;
	}

	/** 
	 * setDefaultDwellTime<P>
	 * This method sets the DwellTime used for this 
	 * This value is the maximum amount of time in milliseconds that may be
	 * spent on the logical antenna port during a tag-protocol-operation
	 * cycle before switching to the next enabled antenna port. A value of
	 * zero indicates that there is no maximum dwell time for this antenna
	 * port. If this parameter is zero, then NumberInventoryCycles may not
	 * be zero.
	 * @param dwellTime_ The Dwell Time in ms.
	 */
	public void setDefaultDwellTime(Number dwellTime_) {
		if (dwellTime_ != null) {
			defaultDwellTime = dwellTime_.intValue();
		}
	}

	/** 
	 * getDefaultInvCycles<P>
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
	public Integer getDefaultInvCycles() {
		return defaultInvCycles;
	}

	/** 
	 * setDefaultInvCycles<P>
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
	public void setDefaultInvCycles(int invCycles_) {
		defaultInvCycles = invCycles_;
	}

	/** 
	 * setDefaultInvCycles<P>
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
	public void setDefaultInvCycles(Number invCycles_) {
		if (invCycles_ != null) {
			defaultInvCycles = invCycles_.intValue();
		}
	}

	/** 
	 * getProfile<P>
	 * This method displays all the configuration settings.
	 * @param jsonFormat True if the result string is in JSON format
	 * @return The result string
	 */
	public String getProfile(boolean jsonFormat) {
		// Return value depends on whether this command came from the gateway or not
		if (jsonFormat) {
			StringBuilder result = new StringBuilder("{");
			result.append( "\"profile_name\":\"" + profileFilename + "\",");
			result.append( "\"operation_mode\":\"" + getOperationMode().toString() + "\",");
			result.append( "\"link_profile\":" + Integer.toString(getLinkProfile()) + ",");
			result.append( "\"num_virtual_ports\":" + Integer.toString(getNumVirtualPorts()) + ",");
			result.append( "\"def_power_level\":" + Float.toString(getDefaultPowerLevel()) + ",");
			result.append( "\"def_dwell_time\":" + Integer.toString(getDefaultDwellTime()) + ",");
			result.append( "\"def_inv_cycles\":" + Integer.toString(getDefaultInvCycles()) );
			result.append( "\"selected_state\":\"" + getSelectedState().toString() + "\",");
			result.append( "\"session_flag\":\"" + getSessionFlag().toString() + "\",");
			result.append( "\"target_state\":\"" + getTargetState().toString() + "\",");
			result.append( "\"algorithm\":\"" + getAlgorithm().toString() + "\",");
			result.append( "\"fixed_q_value\":" + Integer.toString(getFixedQValue()) + ",");
			result.append( "\"start_q_value\":" + Integer.toString(getStartQValue()) + ",");
			result.append( "\"min_q_value\":" + Integer.toString(getMinQValue()) + ",");
			result.append( "\"max_q_value\":" + Integer.toString(getMaxQValue()) + ",");
			result.append( "\"retry_count\":" + Integer.toString(getRetryCount()) + ",");
			result.append( "\"toggle_target_flag\":\"" + getToggleTargetFlag().toString() + "\",");
			result.append( "\"repeat_until_no_tags\":\"" + getRepeatUntilNoTags().toString() + "\",");
			result.append( "\"threshold_multiplier\":" + Integer.toString(getThresholdMultiplier()) + ",");
			result.append( "\"perform_guard_mode\":\"" + getPerformGuardMode().toString() + "\",");
			result.append( "}" );
			return result.toString();
		} else {
			System.out.println("\n\n");
			System.out.println( "Current Inventory Profile = " + getProfileFilename() );
			System.out.println( "Operation Mode            = " + getOperationMode().toString() );
			System.out.println( "Link Profile              = " + Integer.toString(getLinkProfile()) );
			System.out.println( "Number Virtual Ports      = " + Integer.toString(getNumVirtualPorts()) );
			System.out.println( "Default Power Level (dBm) = " + Float.toString(getDefaultPowerLevel()) );
			System.out.println( "Default Dwell Time (ms)   = " + Integer.toString(getDefaultDwellTime()) );
			System.out.println( "Default Inventory Cycles  = " + Integer.toString(getDefaultInvCycles()) );
			System.out.println( "Selected State            = " + getSelectedState().toString() );
			System.out.println( "Session Flag              = " + getSessionFlag().toString() );
			System.out.println( "Target State              = " + getTargetState().toString() );
			System.out.println( "Algorithm                 = " + getAlgorithm().toString() );
			System.out.println( "Fixed Q Value             = " + Integer.toString(getFixedQValue()) );
			System.out.println( "Start Q Value             = " + Integer.toString(getStartQValue()) );
			System.out.println( "Min Q Value               = " + Integer.toString(getMinQValue()) );
			System.out.println( "Max Q Value               = " + Integer.toString(getMaxQValue()) );
			System.out.println( "Retry Count               = " + Integer.toString(getRetryCount()) );
			System.out.println( "Toggle Target Flag        = " + getToggleTargetFlag().toString() );
			System.out.println( "Repeat Until No Tags      = " + getRepeatUntilNoTags().toString() );
			System.out.println( "Threshold Multiplier      = " + Integer.toString(getThresholdMultiplier()) );
			System.out.println( "Perform Guard Mode        = " + getPerformGuardMode().toString() );
			System.out.println( "\n\n" );
			return null;
		}
	}	

	/** 
	 * setProfile<P>
	 * This method saves verbose profile settings.
	 * @param jsonProfile_ The verbose profile parameters in json format
	 * @return True if the profile was saved successfully
	 */
	public boolean setProfile(String jsonProfile_) {
		boolean success = true;
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonProfile_);
			// Save the elements of the inventory profile
			setOperationMode((String) jsonObject.get("operation_mode"));
			setLinkProfile((Number) jsonObject.get("link_profile"));
			setNumVirtualPorts((Number) jsonObject.get("num_virtual_ports"));
			setDefaultPowerLevel((Number) jsonObject.get("def_power_level"));
			setDefaultDwellTime((Number) jsonObject.get("def_dwell_time"));
			setDefaultInvCycles((Number) jsonObject.get("def_inv_cycles"));
			setSelectedState((String) jsonObject.get("selected_state"));
			setSessionFlag((String) jsonObject.get("session_flag"));
			setTargetState((String) jsonObject.get("target_state"));
			setAlgorithm((String) jsonObject.get("algorithm"));
			setFixedQValue((Number) jsonObject.get("fixed_q_value"));
			setStartQValue((Number) jsonObject.get("start_q_value"));
			setMinQValue((Number) jsonObject.get("min_q_value"));
			setMaxQValue((Number) jsonObject.get("max_q_value"));
			setRetryCount((Number) jsonObject.get("retry_count"));
			setToggleTargetFlag((String) jsonObject.get("toggle_target_flag"));
			setRepeatUntilNoTags((String) jsonObject.get("repeat_until_no_tags"));
			setThresholdMultiplier((Number) jsonObject.get("threshold_multiplier"));
			setPerformGuardMode((String) jsonObject.get("perform_guard_mode"));
		} catch (ParseException e) {
			success = false;
		}
		return success;
	}	
}
