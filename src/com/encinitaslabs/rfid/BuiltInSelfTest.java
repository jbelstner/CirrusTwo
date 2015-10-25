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
import java.io.InputStreamReader;
import java.util.Date;

import org.json.simple.JSONObject;

public class BuiltInSelfTest {

	private RfModuleCommHealth rfModuleCommHealth = RfModuleCommHealth.Good;
	private Integer rfModulePacketCount = 0;
	private Integer ambientTemperature = null;
	private Integer rfModuleTemperature = null;
	private Integer lastMtiStatusCode = 0;
	private Integer mtiStatusCode = 0;
	private Boolean rfModuleWatchDog = true;
	private Boolean pingRfModule = false;
	private Long startEpoch_ms = null;
	private Long totalMemInfoBytes = null;
	private Long totalMemUsedBytes = null;
	private Long totalMemFreeBytes = null;
	private Long totalMemCachBytes = null;
	private Long totalMemUsedPercent = null;
	private Integer totalCpuUsedPercent = null;
	private Long processMemUsedBytes = null;
	private Long processMemUsedPercent = null;		
	private Long processCpuUsedPercent = null;
	private int user, nice, system, idle, iowait, irq, softirq, steal;
	private int prev_user, prev_nice, prev_system, prev_idle, prev_iowait, prev_irq, prev_softirq, prev_steal;
	private Boolean previousValuesStored = false;
	private Log logObject = null;
	// Threshold settings
/*
	private Integer ambientTempThresholdHi = null;
	private Integer ambientTempThresholdLo = null;
	private Integer rfModuleTempThresholdHi = null;
	private Integer rfModuleTempThresholdLo = null;
	private Long totalCpuPercentThresholdHi = null;
	private Long totalMemPercentThresholdHi = null;
	private Long procCpuPercentThresholdHi = null;
	private Long procMemPercentThresholdHi = null;
*/
	// Alarm States
	private Boolean bRfModuleCommHealth = false;
	private Boolean bRfModuleFwError = false;
	private Boolean bAmbientTempThresholdHi = false;
	private Boolean bAmbientTempThresholdLo = false;
	private Boolean bRfModuleTempThresholdHi = false;
	private Boolean bRfModuleTempThresholdLo = false;
	private Boolean bTotalCpuPercentThresholdHi = false;
	private Boolean bTotalMemPercentThresholdHi = false;
	private Boolean bProcCpuPercentThresholdHi = false;
	private Boolean bProcMemPercentThresholdHi = false;
	private Boolean bPositionChanged = false;

	public enum RfModuleCommHealth {
		Bad((boolean)false),
		Good((boolean)true);
		
		private boolean bRfModuleCommHealth;
		
		RfModuleCommHealth(boolean bRfModuleCommHealth) {
			this.bRfModuleCommHealth = bRfModuleCommHealth;
		}

		public boolean getValue() {
			return bRfModuleCommHealth;
		}
	}

	/** 
	 * SelfTest<P>
	 * Class Constructor
	 */
	public BuiltInSelfTest( Log logObject_ ) {
		// Save the Log object
		logObject = logObject_;

		// Record the start date/time
		startEpoch_ms = new Date().getTime();		
	}

	/** 
	 * shouldPingRfModule<P>
	 * This method returns a Boolean indicating whether or
	 * not it is time to ping the RF module.
	 * @return A Boolean.
	 */
	public Boolean shouldPingRfModule() {
		return pingRfModule;
	}

	/** 
	 * getRfModuleCommHealth<P>
	 * This method returns the current health of the serial
	 * link to the embedded RFID module.
	 * A value of null indicates an uninitialized value.
	 * @return A String.
	 */
	public String getRfModuleCommHealth() {
		return rfModuleCommHealth.toString();
	}

	/** 
	 * getRfModuleWatchDog<P>
	 * This method returns the current RF Module WatcgDog state.
	 * A value of null indicates an uninitialized value.
	 * @return A Boolean.
	 */
	public Boolean getRfModuleWatchDog() {
		return rfModuleWatchDog;
	}

	/** 
	 * setRfModuleWatchDog<P>
	 * This method sets the current RF Module WatcgDog state.
	 * @param rfModuleWatchDog_ A Boolean.
	 */
	public void setRfModuleWatchDog(Boolean rfModuleWatchDog_) {
		if (rfModuleWatchDog_ != null) {
			rfModuleWatchDog = rfModuleWatchDog_;
		}
	}

	/** 
	 * setRfModuleTemp<P>
	 * This method sets the RF Module Temp to some value in C. 
	 * @param rfModuleTemp_ A String.
	 */
	public void setRfModuleTemp(String rfModuleTemp_) {
		if (rfModuleTemp_ != null) {
			rfModuleTemperature = Integer.decode(rfModuleTemp_);
		}
	}

	/** 
	 * setRfModuleTemp<P>
	 * This method sets the RF Module Temp to some value in C. 
	 * @param rfModuleTemp_ A Byte.
	 */
	public void setRfModuleTemp(Byte rfModuleTemp_) {
		if (rfModuleTemp_ != null) {
			rfModuleTemperature = rfModuleTemp_.intValue();
		}
	}

	/** 
	 * getAmbientTemp<P>
	 * This method returns the current Ambient Temp in C.
	 * A value of null indicates an uninitialized value.
	 * @return A Long.
	 */
	public String getAmbientTemp() {
		if (ambientTemperature == null) {
			return null;
		} else {
			return ambientTemperature.toString();
		}
	}

	/** 
	 * setAmbientTemp<P>
	 * This method sets the Ambient Temp to some value in C. 
	 * @param ambientTemp_ A String.
	 */
	public void setAmbientTemp(String ambientTemp_) {
		if (ambientTemp_ != null) {
			ambientTemperature = Integer.decode(ambientTemp_);
		}
	}

	/** 
	 * setAmbientTemp<P>
	 * This method sets the Ambient Temp to some value in C. 
	 * @param ambientTemp_ A Byte.
	 */
	public void setAmbientTemp(Byte ambientTemp_) {
		if (ambientTemp_ != null) {
			ambientTemperature = ambientTemp_.intValue();
		}
	}

	/** 
	 * setMtiStatusCode<P>
	 * This method sets the Firmware Error code returned from the RFID Module. 
	 * @param mtiStatusCode_ A String.
	 */
	public void setMtiStatusCode(String mtiStatusCode_) {
		if (mtiStatusCode_ != null) {
			mtiStatusCode = Integer.decode(mtiStatusCode_);
		}
	}

	/** 
	 * setMtiStatusCode<P>
	 * This method sets the Firmware Status code returned from the RFID Module. 
	 * @param mtiStatusCode_ An Integer.
	 */
	public void setMtiStatusCode(Integer mtiStatusCode_) {
		if (mtiStatusCode_ != null) {
			mtiStatusCode = mtiStatusCode_.intValue();
		}
	}

	/** 
	 * getMtiStatusCode<P>
	 * This method returns the Firmware Status code reported by the RFID Module.
	 * If the code is non-zero, it stored it in the lastMtiStatusCode and clears
	 * the mtiStatusCode register.
	 * @return An Integer.
	 */
	public Integer getMtiStatusCode( ) {
		Integer code = mtiStatusCode;
		if (mtiStatusCode != 0) {
			lastMtiStatusCode = mtiStatusCode;
			mtiStatusCode = 0;
		}
		return code;
	}

	/** 
	 * getLastMtiErrorCode<P>
	 * This method returns the last Firmware Error code from the RFID Module.
	 * @return An Integer.
	 */
	public Integer getLastMtiStatusCode( ) {
		return lastMtiStatusCode;
	}

	/** 
	 * getUpTime<P>
	 * This method returns the "up time" of this process.
	 * A value of zero indicates an uninitialized value.
	 * @return A String.
	 */
	public Long getUpTime() {
		return ((new Date().getTime()) - startEpoch_ms);
	}

	/** 
	 * getTotalCpuUsedInPercent<P>
	 * @return A String.
	 */
	public String getTotalCpuUsedInPercent() {
		if (totalCpuUsedPercent == null) {
			return null;
		} else {
			return totalCpuUsedPercent.toString();
		}
	}

	/** 
	 * getProcessCpuUsedInPercent<P>
	 * @return A String.
	 */
	public String getProcessCpuUsedInPercent() {
		if (processCpuUsedPercent == null) {
			return null;
		} else {
			return processCpuUsedPercent.toString();
		}
	}

	/** 
	 * getTotalMemoryUsedInBytes<P>
	 * @return A String.
	 */
	public String getTotalMemoryUsedInBytes() {
		if (totalMemUsedBytes == null) {
			return null;
		} else {
			return totalMemUsedBytes.toString();
		}
	}

	/** 
	 * getTotalMemoryUsedInPercent<P>
	 * @return A String.
	 */
	public String getTotalMemoryUsedInPercent() {
		if (totalMemUsedPercent == null) {
			return null;
		} else {
			return totalMemUsedPercent.toString();
		}
	}

	/** 
	 * getProcessMemoryUsedInBytes<P>
	 * @return A String.
	 */
	public String getProcessMemoryUsedInBytes() {
		if (processMemUsedBytes == null) {
			return null;
		} else {
			return processMemUsedBytes.toString();
		}
	}

	/** 
	 * getProcessMemoryUsedInPercent<P>
	 * @return A String.
	 */
	public String getProcessMemoryUsedInPercent() {
		if (processMemUsedPercent == null) {
			return null;
		} else {
			return processMemUsedPercent.toString();
		}
	}

	/** 
	 * performSelfTests<P>
	 * @param queue_ A queue to store a bit alarm
	 * @return True means that at least one test failed.
	 */
	public Boolean performSelfTests() {
		// Update the statistics
/*
		updateCpuStats();
		updateMemStats();
		bPositionChanged = checkAccelerometer();
		processMemUsedBytes = totalMemUsedBytes * processMemUsedPercent.intValue() / 100;
		totalMemUsedPercent = totalMemUsedBytes * 100 / totalMemInfoBytes;
*/		
		// Check for serial activity since the last self test
		if (rfModuleWatchDog && (rfModulePacketCount == 0)) {
			if (pingRfModule == false) {
				// Try to ping the RF module this time
				pingRfModule = true;
			} else {
				// Otherwise we have a fault
				rfModuleCommHealth = RfModuleCommHealth.Bad;
			}
		}
		rfModulePacketCount = 0;
		bRfModuleFwError = (getMtiStatusCode() != 0);
		
		// Perform other checks against set thresholds
		if (rfModuleCommHealth != null) {
			bRfModuleCommHealth = (rfModuleCommHealth == RfModuleCommHealth.Bad);			
		}
/*
		if ((ambientTemperature != null) && (ambientTempThresholdHi != null)) {
			bAmbientTempThresholdHi = (ambientTemperature >= ambientTempThresholdHi);			
		}
		if ((ambientTemperature != null) && (ambientTempThresholdLo != null)) {
			bAmbientTempThresholdLo = (ambientTemperature <= ambientTempThresholdLo);
		}
		if ((rfModuleTemperature != null) && (rfModuleTempThresholdHi != null)) {
			bRfModuleTempThresholdHi = (rfModuleTemperature >= rfModuleTempThresholdHi);
		}
		if ((rfModuleTemperature != null) && (rfModuleTempThresholdLo != null)) {
			bRfModuleTempThresholdLo = (rfModuleTemperature <= rfModuleTempThresholdLo);
		}
		if ((totalCpuUsedPercent != null) && (totalCpuPercentThresholdHi != null)) {
			bTotalCpuPercentThresholdHi = (totalCpuUsedPercent >= totalCpuPercentThresholdHi);
		}
		if ((totalMemUsedPercent != null) && (totalMemPercentThresholdHi != null)) {
			bTotalMemPercentThresholdHi = (totalMemUsedPercent >= totalMemPercentThresholdHi);
		}
		if ((processCpuUsedPercent != null) && (procCpuPercentThresholdHi != null)) {
			bProcCpuPercentThresholdHi = (processCpuUsedPercent >= procCpuPercentThresholdHi);
		}
		if ((processMemUsedPercent != null) && (procMemPercentThresholdHi != null)) {
			bProcMemPercentThresholdHi = (processMemUsedPercent >= procMemPercentThresholdHi);
		}
*/
		// A failure is any one of these parameters being true
		return (bRfModuleCommHealth || bRfModuleFwError || bPositionChanged ||
				bAmbientTempThresholdHi || bAmbientTempThresholdLo ||
				bRfModuleTempThresholdHi || bRfModuleTempThresholdLo ||
				bTotalCpuPercentThresholdHi || bTotalMemPercentThresholdHi ||
				bProcCpuPercentThresholdHi || bProcMemPercentThresholdHi);
	}

	/** 
	 * rfModuleCommActivity<P>
	 * 
	 */
	public void rfModuleCommActivity() {
		rfModulePacketCount++;
		pingRfModule = false;
		rfModuleCommHealth = RfModuleCommHealth.Good;
	}

	/** 
	 * getBitResponseJsonObject<P>
	 * This method builds the BIT JSON RPC result object.
	 */
	public String getBitResponseJsonObject( ) {
		updateCpuStats();
		updateMemStats();
		StringBuilder result = new StringBuilder("{");
		// UART Health
		result.append("\"uart_comm_health\":\"" + rfModuleCommHealth.toString() + "\",");
		// Temperatures
		result.append("\"ambient_temp\":" + ambientTemperature + ",");
		result.append("\"rf_module_temp\":" + rfModuleTemperature + ",");
		// CPU resource related parameters
		result.append("\"time_alive\":" + getUpTime() + ",");
		result.append("\"cpu_usage\":" + totalCpuUsedPercent + ",");
		result.append("\"mem_used_bytes\":" + totalMemUsedBytes + ",");
		result.append("\"mem_used_percent\":" + totalMemUsedPercent + ",");
		result.append("\"proc_cpu\":" + processCpuUsedPercent + ",");
		result.append("\"proc_mem_bytes\":" + processMemUsedBytes + ",");
		result.append("\"proc_mem_percent\":" + processMemUsedPercent + ",");
		// Motion Detection
		result.append("\"position_changed\":\"" + bPositionChanged + "\"}");
		// Return the string
		return result.toString();
	}
	
	/** 
	 * sendBitResponseToCli<P>
	 * This method prints the BIT data to the console.
	 */
	public void sendBitResponseToCli( ) {
		updateCpuStats();
		updateMemStats();
		// UART Health
		System.out.println("RFID Module Comms Staus = " + rfModuleCommHealth.toString());
		System.out.println("RFID Module Status Code = " + mtiStatusCode.toString());
		// Temperatures
		System.out.println("Ambient Temparature     = " + ambientTemperature + "C");
		System.out.println("RF Module Temperature   = " + rfModuleTemperature + "C");
		// CPU resource related parameters
		Long upTime  = getUpTime();
		Long days    = upTime/86400000;
		Long hours   = (upTime - (days * 86400000))/3600000;
		Long minutes = (upTime - (days * 86400000) - (hours * 3600000))/60000;
		Long seconds = (upTime - (days * 86400000) - (hours * 3600000) - (minutes * 60000))/1000;
		System.out.println("Cirrus-IIP Up Time      = " + days + " days, " + hours + " hrs, " + minutes + " min, " + seconds + " sec");
		System.out.println("Total CPU Utilization   = " + totalCpuUsedPercent + "%");
		System.out.println("Total Memory Used       = " + totalMemUsedBytes + " bytes");
		System.out.println("Total Memory Cached     = " + totalMemCachBytes + " bytes");
		System.out.println("Process CPU Utilization = " + processCpuUsedPercent + "%");
		System.out.println("Process Memory Used     = " + processMemUsedBytes + " bytes");
		System.out.println("Process Memory Used %   = " + processMemUsedPercent + "%");
		// Somebody bumped the reader
		System.out.println("Position Changed        = " + bPositionChanged + "\n");
	}

	/** 
	 * setBitAlarmThresholds<P>
	 * This method set the thresholds for triggering a BIT alarm.
	 */
	public Boolean setBitAlarmThresholds( JSONObject command ) {
		// Make sure we have a command
		if (command == null) { return false; }
/*
		// Make sure we have parameters
		JSONObject params = (JSONObject) command.get("params");
		if (params == null) { return false; }

		// Extract the values and save them to the profile object
		Number number = null;
		number = (Number)params.get("ambient_temp_hi");
		if (number != null) {
			ambientTempThresholdHi = number.intValue();
		}
		number = (Number)params.get("ambient_temp_lo");
		if (number != null) {
			ambientTempThresholdLo = number.intValue();
		}
		number = (Number)params.get("rf_module_temp_hi");
		if (number != null) {
			rfModuleTempThresholdHi = number.intValue();
		}
		number = (Number)params.get("rf_module_temp_lo");
		if (number != null) {
			rfModuleTempThresholdLo = number.intValue();
		}
		number = (Number)params.get("cpu_usage_hi");
		if (number != null) {
			rfModuleTempThresholdLo = number.intValue();
		}
		number = (Number)params.get("mem_used_percent_hi");
		if (number != null) {
			rfModuleTempThresholdLo = number.intValue();
		}
		number = (Number)params.get("proc_cpu_hi");
		if (number != null) {
			rfModuleTempThresholdLo = number.intValue();
		}
		number = (Number)params.get("proc_mem_percent_hi");
		if (number != null) {
			rfModuleTempThresholdLo = number.intValue();
		}
*/
		return true;
	}

	/** 
	 * getBitAlarmJsonObject<P>
	 * This method builds the BIT ALARM JSON RPC indication object.
	 */
	public String getBitAlarmJsonObject( String device_id ) {
		Date date = new Date();
		Long epoch_ms = date.getTime();
		StringBuilder result = new StringBuilder("{");
		// When and Who
		result.append("\"sent_on\":" + epoch_ms.toString() + ",");
		result.append("\"device_id\":\"" + device_id + "\",");
		// UART Health
		result.append("\"rf_module_comm\":\"" + bRfModuleCommHealth + "\",");
		// Temperatures
		result.append("\"ambient_temp_hi\":\"" + bAmbientTempThresholdHi + "\",");
		result.append("\"ambient_temp_lo\":\"" + bAmbientTempThresholdLo + "\",");
		result.append("\"rf_module_temp_hi\":\"" + bRfModuleTempThresholdHi + "\",");
		result.append("\"rf_module_temp_lo\":\"" + bRfModuleTempThresholdLo + "\",");
		// CPU resource related parameters
		result.append("\"cpu_usage_hi\":\"" + bTotalCpuPercentThresholdHi + "\",");
		result.append("\"mem_used_percent_hi\":\"" + bTotalMemPercentThresholdHi + "\",");
		result.append("\"proc_cpu_hi\":\"" + bProcCpuPercentThresholdHi + "\",");
		result.append("\"proc_mem_percent_hi\":\"" + bProcMemPercentThresholdHi + "\",");
		// Motion Detection
		result.append("\"position_changed\":\"" + bPositionChanged + "\"}");
		// Return the string
		return result.toString();
	}

	/** 
	 * updateCpuStats<P>
	 * This method updates the CPU utilization statistics.
	 * @return True if stats were successfully updated
	 */
	private Boolean updateCpuStats() {
		// Scrape the /proc/stat file
		totalCpuUsedPercent = 0;
		Process proc;
		String command = "cat /proc/stat";
		try {
			proc = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";
			while ((line = stdInput.readLine()) != null) {
				if (line.startsWith("cpu  ")) {
					// Scrape the individual values from the line
					String stat[] = line.split(" +");
					user = Integer.parseInt(stat[1]);
					nice = Integer.parseInt(stat[2]);
					system = Integer.parseInt(stat[3]);
					iowait = Integer.parseInt(stat[4]);
					irq = Integer.parseInt(stat[5]);
					softirq = Integer.parseInt(stat[6]);
					steal = Integer.parseInt(stat[7]);

					// Need previous values to calculate stats 
					if (previousValuesStored == true) {
						int PrevIdle = prev_idle + prev_iowait;
						int Idle = idle + iowait;
						int PrevNonIdle = prev_user + prev_nice + prev_system + prev_irq + prev_softirq + prev_steal;
						int NonIdle = user + nice + system + irq + softirq + steal;
						int PrevTotal = PrevIdle + PrevNonIdle;
						int Total = Idle + NonIdle;
						totalCpuUsedPercent = (((Total-PrevTotal)-(Idle-PrevIdle)) * 100) /(Total-PrevTotal);
					}
					// Save the new values away for next time
					prev_user = user;
					prev_nice = nice;
					prev_system = system;
					prev_iowait = iowait;
					prev_irq = irq;
					prev_softirq = softirq;
					prev_steal = steal;
					previousValuesStored = true;
				}
			}
			return true;
		} catch (Exception e) {
			log( "Unable to update CPU statistics!\n" + e.toString(), Log.Level.Error );
			return false;
		}
	}

	/** 
	 * updateMemStats<P>
	 * This method updates the Memory utilization statistics.
	 */
	private Boolean updateMemStats() {
		// Scrape the /proc/meminfo" file
		totalMemInfoBytes = (long)0;
		totalMemFreeBytes = (long)0;
		totalMemCachBytes = (long)0;
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("cat /proc/meminfo");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";
			while ((line = stdInput.readLine()) != null) {
				if (line.startsWith("MemTotal")) {
					// Scrape the individual values from the line
					String stat[] = line.split(" +");
					totalMemInfoBytes = Long.parseLong(stat[1]);
				}
				if (line.startsWith("MemFree")) {
					// Scrape the individual values from the line
					String stat[] = line.split(" +");
					totalMemFreeBytes = Long.parseLong(stat[1]);
				}
				if (line.startsWith("Cached")) {
					// Scrape the individual values from the line
					String stat[] = line.split(" +");
					totalMemCachBytes = Long.parseLong(stat[1]);
				}
			}
			totalMemUsedBytes = totalMemInfoBytes - totalMemFreeBytes;
			return true;
		} catch (Exception e) {
			log( "Unable to update memory statistics!\n" + e.toString(), Log.Level.Error );
			return false;
		}
	}

	/** 
	 * log<P>
	 * This method is used for making log entries.
	 */
	private void log(String entry, Log.Level logLevel) {
		if (logObject != null) {
			logObject.makeEntry(entry, logLevel);
		} else {
			System.out.println(entry);
		}
	}
}
