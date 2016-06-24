/*
 * Copyright (c) 2013 - 2016, Encinitas Laboratories, Inc. 
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
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.encinitaslabs.rfid.cmd.CmdAntennaPortConf;
import com.encinitaslabs.rfid.cmd.CmdHead;
import com.encinitaslabs.rfid.cmd.CmdReaderModuleConfig;
import com.encinitaslabs.rfid.cmd.CmdReaderModuleFirmwareAccess;
import com.encinitaslabs.rfid.cmd.CmdTagAccess;
import com.encinitaslabs.rfid.cmd.CmdTagProtocol;
import com.encinitaslabs.rfid.cmd.Llcs;
import com.encinitaslabs.rfid.cmd.MtiCmd;
import com.encinitaslabs.rfid.comms.SerialComms;
import com.encinitaslabs.rfid.utils.Crc16;
import com.encinitaslabs.rfid.utils.EmailUtil;

/**
 * CirrusII Object
 * <P>This class is the main class.
 * It abstracts the details of MTI Low Level Command Set into a set of basic
 * commands for control and use of the RFID module.
 *  
 * @author Encinitas Laboratories, Inc.
 * @version 0.1
 */
public class CirrusII {
	
	private static final String appVersionString = "C2P-0.9.24";
	private static final String configFile = "main.properties";
	private static CirrusII cirrusII;

	public enum RfidState {
		Idle,
		WaitingForResponse,
		WaitingForBegin,
		WaitingForEnd,
		WaitingForInventory,
		WaitingForAccess,
		WaitingForReset
	}
	
	public enum ErrorCode {
		Success,
		ParameterError,
		DeviceNotResponding,
		WrongState
	}
	
	// RFID parameters
	private InventoryProfile profile = null;
	private ArrayList<AntennaPort> antennaPorts = new ArrayList<AntennaPort>();;
	private Llcs llcs = null;
	private byte testModeCommandSelect = 0;
	private boolean testModeResponsePending = false;
	private int numPhysicalPorts = 2;
	// Serial Port parameters
	private String moduleType = "RU861";
	private LinkedBlockingQueue<byte[]> serialCmdQueue = new LinkedBlockingQueue<byte[]>();
	private LinkedBlockingQueue<byte[]> serialRspQueue = new LinkedBlockingQueue<byte[]>();
	private LinkedBlockingQueue<RfidState> nextRfidState = new LinkedBlockingQueue<RfidState>();
	private SerialComms serialComms = new SerialComms(serialRspQueue);;
	// Tag Data parameters
	private ConcurrentHashMap<String, TagData> tagEvents = new ConcurrentHashMap<String, TagData>();
	private ConcurrentHashMap<String, TagData> tagDatabase = new ConcurrentHashMap<String, TagData>();
	private boolean tagPresent = false;
	private boolean autoRepeat = false;
	// Fotaflo parameters
	private Fotaflo fotaflo = null;
	private final int MIN_LENGTH_FILENAME = 20;
	private final int FAILURE_THRESHOLD = 3;
	private LinkedBlockingQueue<String> pictureQueue = new LinkedBlockingQueue<String>();
	private Camera camera = new Camera(pictureQueue);
	private String username = null;
	private String password = null;
	private String photoUrl = null;
	private String deviceId = null;
	private String location = null;
	private String imageFormat = "SmallNormal";
	private String shotsPerTrigger = "1";
	private int epcFirst = 0;
	private int epcLast = 7;
	private int triggersPerEvent = 3;
	private int triggerInterval_sec = 5;
	private int eventTimeout_sec = 900;
	private int downloadFailures = 0;
	private int uploadFailures = 0;
	private static final String CAMERA_ERROR = "Unable to download images from camera";
	private static final String UPLOAD_ERROR = "Unable to upload images to the server";
	// Statistics
	private int numberOfTriggers = 0;
	private int numberOfUploads = 0;
	private int numberOfUnique = 0;
	// Local parameters
	private static final Logger log = Logger.getLogger(CirrusII.class);
	private RfidState rfidState =  RfidState.Idle;
	private boolean testMode = false;
	private String sipVersionString = " ";
	private LedControl led = null;
	private boolean useCLI = true;
	private int ticTimerCount = 0;
	private BuiltInSelfTest bist = null;
    private EmailUtil mail = new EmailUtil();
    private ArrayList<String> alertEmailList = new ArrayList<>();
//	private boolean motionFlag = false;
	private boolean errorFlag = false;
	// Timeout and retry parameters
	private Timer bistTimer = null;
	private Timer interScanTimer = null;
	private Timer powerOnTimer = null;
	private Timer uploadRetryTimer = null;
	private Timer tagEventAgeTimer = null;
	private Timer moduleResetTimer = null;
    private Timer emailHoldoffTimer = null;
    private boolean emailHoldoff = false;
	private int bistTimeInterval_ms = 15000;
	private int powerOnTimeInterval_ms = 7000;
	private int uploadRetryInterval_ms = 600000;
	private int tagDataTimeInterval_ms = 1000;
	private int moduleResetWait_ms = 10000;

	private double latitude = 0.0;
	private double longitude = 0.0;
	
	/** 
	 * main
	 * 
	 * This is the main executable.
	 */
	public static void main( String[] args ) throws InterruptedException , IOException {

		System.out.println( "Encinitas Laboratories, Inc.  Copyright 2014-2016" );
		System.out.println( "Cirrus-II Photo, version " + appVersionString);
			    
		if ((args.length > 0) && (Boolean.valueOf(args[0]) == true)) {
			System.out.println( "Command Line Interface Enabled\n\n");
			cirrusII = new CirrusII(true);	
		} else {
			System.out.println( "Command Line Interface Disabled\n\n");
			cirrusII = new CirrusII(false);	
		}

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
            	cirrusII.cleanup();
            }
        });
	}
	
	/** 
	 * SmartAntenna
	 * 
	 * Class Constructor
	 */
	public CirrusII( boolean useCLI_ ) {
		// Check if we are using command line input
		useCLI = useCLI_;
		
		try {
			Runtime.getRuntime().exec("/etc/init.d/ntp restart");
		} catch (IOException e) {
			System.out.println("Unable to restart ntp\n" + e.toString());
		}
		
		// Parse the config file (if there is one)
		try {
			parseConfigFile(configFile);
		} catch (IOException e1) {
			System.out.println("No config file found, using defaults\n" + e1.toString());
		}
		
		// Create the log object that we need to have
		log.info( "Cirrus-II Photo, version " + appVersionString);
		bist = new BuiltInSelfTest();
		
		// Fotaflo specific objects
		fotaflo = new Fotaflo(deviceId, location);
		fotaflo.setCredentials(username, password);
		fotaflo.setUploadUrl(photoUrl);
		// Initially turn the power off
		camera.setImageFormat(imageFormat);
		camera.setShotsPerTrigger(shotsPerTrigger);

		// SERIAL PORT INITIALIZATION
		try {
			serialComms.connect();
		} catch (Exception e) {
			log.error("Unable to open Comm Port " + e.toString());
		}

		// Turn on the Green LED
		try {
			led = new LedControl();
			led.set(LedControl.Color.Green, LedControl.BlinkState.Constant);
		} catch (IOException e1) {
			log.error("Unable set LED colors\n" + e1.toString());
		}			

		// RFID MODULE INITIALIZATION
		llcs = new Llcs();
		try {
			requestReaderInformation();
			profile = new InventoryProfile();
			AntennaPort defaultPort = new AntennaPort(numPhysicalPorts);
			defaultPort.setPowerLevel(profile.getDefaultPowerLevel());
			defaultPort.setDwellTime(profile.getDefaultDwellTime());
			defaultPort.setInvCycles(profile.getDefaultInvCycles());
			// Initialize the antennaPorts array
			for (int i = 0; i < profile.getNumVirtualPorts(); i++) {
				antennaPorts.add(defaultPort);
			}
			initializeRfidModuleSettings();
		} catch (Exception e) {
			log.error("Unable to load profile\n" + e.toString());
		}

		if (useCLI) {
			// LISTEN FOR INPUT FROM THE COMMAND LINE
			Thread cliWorker = new Thread () {
		        // Simple command line interface
		        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
				public void run() {
					while ( true ) {
						String command = "";
						try {
							command = sysin.readLine();
							processCommandLineInput(command);
						} catch (IOException e) {
							log.error("Command line read error\n" + e.toString());
						} catch (InterruptedException e) {
							log.error("Unable to queue Gateway Command\n" + e.toString());
						}
					}
				}
			};
			cliWorker.start();
		}
		
		// MANAGE THE PICTURE UPLOAD QUEUE
		Thread imageWorker = new Thread () {
			public void run() {
				while ( true ) {
					try {
						// This method blocks until a file is available
						String fileToUpload = pictureQueue.take();
						associateFileWithTagsAndUpload(fileToUpload);
					} catch (Exception e) {
						log.error("Error processing Command or Event\n" + e.toString());
					}
				}
			}
		};
		imageWorker.start();

		// MANAGE THE SERIAL PORT COMMAND QUEUE
		Thread serialcmdWorker = new Thread () {
			public void run() {
				while ( true ) {
					if (rfidState == RfidState.Idle) {
						try {
							byte[] serialCmd = serialCmdQueue.take();
							// Determine the command type
							String commandType = MtiCmd.getCommandType(serialCmd[MtiCmd.TYPE_INDEX]);
					    	log.debug("Sending " + commandType);
					    	// Change the serial state based on the command type
							if (commandType.contains("Command")) {
								setRfidState(RfidState.WaitingForResponse);
							}
							// Send the packet out the serial port
							serialComms.serialWrite(serialCmd, serialCmd.length);
						} catch (Exception e) {
							log.error("Error processing Serial Command\n" + e.toString());
						}
					} else {
						try {
							nextRfidState.take();
						} catch (Exception e) {
							log.error("Error processing Next RFID State\n" + e.toString());
						}
					}
				}
			}
		};
		serialcmdWorker.start();

		// MANAGE THE SERIAL PORT RESPONSE QUEUE
		Thread serialRspWorker = new Thread () {
			public void run() {
				while ( true ) {
					try {
						byte[] nextResponse = serialRspQueue.take();
						processSerialResponse(nextResponse);
					} catch (Exception e) {
						log.error("Error processing Serial Response\n" + e.toString());
					}
				}
			}
		};
		serialRspWorker.start();

		// Start recurring timers
		startBistTimer(bistTimeInterval_ms);
		startUploadRetryTimer(uploadRetryInterval_ms);
		startTagEventAgeTimer(tagDataTimeInterval_ms);
		startPowerOnTimer(powerOnTimeInterval_ms);
		// Upload any leftover pictures from last time
		camera.enablePower(true);
		queueLeftoverFiles();

		// Give the user some help
		if (useCLI) {
			showTestModeOffCommands();
		}		
	}

	/** 
	 * startBistTimer<P>
	 * This method sets the Built-In-Self-Test (BIST) time interval to the
	 * specified value. If the timer is already running, it first stops it
	 * and then starts it up again with the new interval.
	 * @param timeInterval_ms The recurring time interval in milliseconds
	 */
	private void startBistTimer( int timeInterval_ms ) {
		if (bistTimer != null) {
			bistTimer.cancel();
		}
		bistTimer = new Timer();
		bistTimer.schedule(new BistTimerTask(), timeInterval_ms, timeInterval_ms);
	}	
	
	/** 
	 * BistTimerTask<P>
	 * This recurring timer handles the execution of Built In Self Test (BIST).
	 */
	class BistTimerTask extends TimerTask {

		@Override
		public void run() {
			
			if (bist.performSelfTests()) {
				errorFlag = true;
			} else {
				errorFlag = false;
			}

			// See if we need to Ping the RFID Module
			if ((rfidState == RfidState.Idle) && bist.shouldPingRfModule()) {
				if (autoRepeat) {
					startInterScanTimer(profile.getDefaultDelayTime());
				} else {
					// or ping the module
					pingModule();
				}
			}

			// Handle the case where the HP-SiP goes off in the weeds
			if ((rfidState != RfidState.WaitingForReset) && ("Bad".equals(bist.getRfModuleCommHealth()))) {
				resetDevice();
			}

			// Update some runtime statistics
			try {
				updateStatistics();
			} catch (Exception e) {
				log.error( "Unable to update statistics\n" + e.toString() );
			}
		}
	}
	
	
	/** 
	 * startPowerOnTimer<P>
	 * @param timeInterval_ms The time interval in milliseconds
	 */
	private void startPowerOnTimer( int timeInterval_ms ) {
		if (powerOnTimer != null) {
			powerOnTimer.cancel();
		}
		powerOnTimer = new Timer();
		powerOnTimer.schedule(new PowerOnTimerTask(), timeInterval_ms);
	}	
	
	/** 
	 * PowerOnTimerTask<P>
	 */
	class PowerOnTimerTask extends TimerTask {

		@Override
		public void run() {
			camera.updateImageFormat();
			if (!useCLI) {
				autoRepeat = true;
				startInterScanTimer(profile.getDefaultDelayTime());
			}
		}
	}

	/** 
	 * startInterScanTimer<P>
	 * @param timeInterval_ms The time interval in milliseconds
	 */
	private void startInterScanTimer( int timeInterval_ms ) {
		if (interScanTimer != null) {
			interScanTimer.cancel();
		}
		interScanTimer = new Timer();
		interScanTimer.schedule(new InterScanTimerTask(), timeInterval_ms);
	}	
	
	/** 
	 * InterScanTimerTask<P>
	 */
	class InterScanTimerTask extends TimerTask {

		@Override
		public void run() {
			try {
				sendInventoryRequest();
			} catch (InterruptedException e) {
				log.error("Unable to queue serial command\n" + e.toString());
			}
		}
	}

	/** 
	 * startUploadRetryTimer<P>
	 * @param timeInterval_ms The recurring time interval in milliseconds
	 */
	private void startUploadRetryTimer( int timeInterval_ms ) {
		if (uploadRetryTimer != null) {
			uploadRetryTimer.cancel();
		}
		uploadRetryTimer = new Timer();
		uploadRetryTimer.schedule(new UploadRetryTimerTask(), timeInterval_ms, timeInterval_ms);
	}	
	
	/** 
	 * UploadRetryTimerTask<P>
	 */
	class UploadRetryTimerTask extends TimerTask {

		@Override
		public void run() {
			queueLeftoverFiles();
		}
	}

	/** 
	 * startTagEventAgeTimer<P>
	 * @param timeInterval_ms The recurring time interval in milliseconds
	 */
	private void startTagEventAgeTimer( int timeInterval_ms ) {
		if (tagEventAgeTimer != null) {
			tagEventAgeTimer.cancel();
		}
		tagEventAgeTimer = new Timer();
		tagEventAgeTimer.schedule(new TagEventAgeTimerTask(), timeInterval_ms, timeInterval_ms);
	}	
	
	/** 
	 * TagEventAgeTimerTask<P>
	 */
	class TagEventAgeTimerTask extends TimerTask {

		@Override
		public void run() {

			try {
				ageTagEvents();
			} catch (NullPointerException npe) {
				log.error("Unable to age tag database\n" + npe.toString());
			}
			updateVisualIndicator();
		}
	}

	/** 
	 * startModuleResetTimer<P>
	 * This method sets the delay time after a module reset to the
	 * specified value. If the timer is already running, it first stops it
	 * and then starts it up again with the new interval.
	 * @param timeInterval_ms The recurring time interval in milliseconds
	 */
	private void startModuleResetTimer( int timeInterval_ms ) {
		if (moduleResetTimer != null) {
			moduleResetTimer.cancel();
		}
		moduleResetTimer = new Timer();
		moduleResetTimer.schedule(new ModuleResetTimerTask(), timeInterval_ms);
	}	
	
	/** 
	 * ModuleResetTimerTask<P>
	 * This timer handles HP-SiP post reset activities.
	 */
	class ModuleResetTimerTask extends TimerTask {

		@Override
		public void run() {
			
			if (rfidState == RfidState.WaitingForReset) {
				setRfidState(RfidState.Idle);
				try {
					initializeRfidModuleSettings();
				} catch (InterruptedException e) {
					log.error("Unable to initialize RFID module settings!  " + e.toString());
				}
				// Pick up where we left off
				if (autoRepeat) {
					startInterScanTimer(profile.getDefaultDelayTime());
				}
			}
		}
	}

    /**
     * startEmailHoldoffTimer<P>
     */
    private void startEmailHoldoffTimer() {
        if (emailHoldoffTimer != null) {
            emailHoldoffTimer.cancel();
        }
        emailHoldoffTimer = new Timer();
        emailHoldoffTimer.schedule(new EmailHoldoffTimerTask(), 600000);
        emailHoldoff = true;
    }

    /**
     * EmailHoldoffTimerTask<P>
     */
    class EmailHoldoffTimerTask extends TimerTask {

        @Override
        public void run() {
            emailHoldoff = false;
        }
    }

    private void sendEmails(final String _subject, final String _body) {

        if (emailHoldoff) {
            return;
        }
        
        if ((_subject == null) || (_body == null)) {
        	log.warn("Email subject or body is null");
            return;
        }
        
        log.warn(_subject + ": " + _body);
        
        if (alertEmailList.isEmpty()) {
        	log.warn("Email List is empty");
            return;
        }
        
        startEmailHoldoffTimer();

        // Send the email(s) in the background
        Thread emailSender = new Thread() {
            public void run() {
                for (String to : alertEmailList) {
                    mail.sendTLS(to, _subject, _body, null);
                }
            }
        };
        emailSender.start();
    }

	/** 
	 * requestReaderInformation<P>
	 * This method requests Reader ID and FW version from the RFID module.
	 * @throws InterruptedException 
	 */
	private void requestReaderInformation() throws InterruptedException {
		// Request the FW version of the reader
		byte[] cmd = llcs.getFirmwareVersion();
		if (cmd != null) { serialCmdQueue.put(cmd); }
	}
	
	/** 
	 * pingModule<P>
	 * This method requests the Device ID from the RFID module.
	 * @throws InterruptedException 
	 */
	private void pingModule() {
		try {
			// Request the Ambient Temperature from the reader
			byte[] cmd = llcs.getDeviceID();
			if (cmd != null) { serialCmdQueue.put(cmd); }
		} catch (InterruptedException e) {
			log.error("Unable to send Ping to RFID module\n" + e.toString());
		}
	}
	
	/** 
	 * initializeRfidModuleSettings<P>
	 * This method sends all the configuration settings to the RFID module.
	 * @throws InterruptedException 
	 */
	private void initializeRfidModuleSettings() throws InterruptedException {

		// Set the Default Operation Mode
		byte[] cmd1 = llcs.setOperationMode(profile.getOperationMode().getValue());
		if (cmd1 != null) { serialCmdQueue.put(cmd1); }
		if (profile.getOperationMode() == CmdReaderModuleConfig.OperationMode.Continuous) {
			bist.setRfModuleWatchDog(false);
		}

		// Set the Default Link Profile
		byte[] cmd2 = llcs.setCurrentLinkProfile(profile.getLinkProfile().byteValue());
		if (cmd2 != null) { serialCmdQueue.put(cmd2); }

		// Set the Inventory Response Packet Format
		if (moduleType.startsWith("HPSIP")) {
			byte[] cmd3 = llcs.setInventoryPacketFormat((byte)0x01);
			if (cmd3 != null) { serialCmdQueue.put(cmd3); }
		}
		
		// Set the Default Antenna Port, Power Level, Scan Time and Number of Inventory Cycles
		// for all the configured virtual antenna ports
		for (int i = 0; i < antennaPorts.size(); i++) {
			byte[] cmd4a = llcs.antennaPortSetState((byte)i, CmdAntennaPortConf.State.Enabled.getValue());
			if (cmd4a != null) { serialCmdQueue.put(cmd4a); }

			byte[] cmd4b = llcs.antennaPortSetConfiguration((byte)i,
															(short)(Math.round(antennaPorts.get(i).getPowerLevel() * 10)),
															antennaPorts.get(i).getDwellTime().shortValue(),
															antennaPorts.get(i).getInvCycles().shortValue(),
															antennaPorts.get(i).getPhysicalPort().byteValue());
			if (cmd4b != null) { serialCmdQueue.put(cmd4b); }
		}

		// Set the Default Singulation Algorithm
		byte[] cmd5 = llcs.setCurrentSingulationAlgorithm(profile.getAlgorithm().getValue());
		if (cmd5 != null) { serialCmdQueue.put(cmd5); }

		// Set the Default Singulation Parameters
		if (profile.getAlgorithm() == CmdTagAccess.Algorithm.FixedQ) {
			byte[] cmd6 = llcs.setCurrentSingulationParameters( profile.getAlgorithm().getValue(),
																profile.getFixedQValue().byteValue(),
																profile.getRetryCount().byteValue(),
																profile.getToggleTargetFlag().getValue(),
																profile.getRepeatUntilNoTags().getValue() );
			if (cmd6 != null) { serialCmdQueue.put(cmd6); }
		} else {
			byte[] cmd6 = llcs.setCurrentSingulationParameters( profile.getAlgorithm().getValue(),
																profile.getStartQValue().byteValue(),
																profile.getMinQValue().byteValue(),
																profile.getMaxQValue().byteValue(),
																profile.getRetryCount().byteValue(),
																profile.getToggleTargetFlag().getValue(),
																profile.getThresholdMultiplier().byteValue() );
			if (cmd6 != null) { serialCmdQueue.put(cmd6); }
		}
	}

	/** 
	 * processSerialResponse<P>
	 * This method parses a complete command received off the serial port.
	 * @param dataBuffer A byte buffer containing the response.
	 */
	private void processSerialResponse( byte[] dataBuffer ) {
		String responseType = MtiCmd.getCommandType(dataBuffer[MtiCmd.TYPE_INDEX]);
		CmdHead response = MtiCmd.getCmdHead(dataBuffer);
		bist.rfModuleCommActivity();

		// Log what we received
		if (responseType.contains("Response")) {
	    	log.debug("Received " + response + " " + responseType);
		} else {
	    	log.debug("Received " + responseType);							
		}

		// Check to see if this packet is corrupted
		int packetLength = MtiCmd.getCommandLength(dataBuffer[MtiCmd.TYPE_INDEX]);
		if (!Crc16.check(dataBuffer, packetLength)) {
	    	log.warn("CRC failed!");
	    	log.warn( MtiCmd.byteArrayToString(dataBuffer, packetLength, true));
			return;
		}

		// Process the incoming Serial Message based on type
		if (responseType.contains("Command")) {
			// We should never get a "command" from the module
			
		} else if (responseType.contains("Response")) {
			// Check the status byte for an error
			Byte status = dataBuffer[MtiCmd.STATUS_POS];
			if (status != 0) {
				log.error("MTI Command Error Code: " + status.toString());
				sendClearError();
			}
			// Process the receipt of a Response packets
			if (rfidState != RfidState.WaitingForResponse) {
				log.warn( "Received Response packet in the wrong state!");
	    		setRfidState(RfidState.WaitingForResponse);
			}
			// Specific Response packet processing
			if (response.name().contains("RFID_MacGetFirmwareVersion") && !testModeResponsePending) {
				sipVersionString = CmdReaderModuleFirmwareAccess.RFID_MacGetFirmwareVersion.getVersion(dataBuffer);
	    		setRfidState(RfidState.Idle);
				
			} else if (response.name().contains("RFID_EngGetTemperature") && !testModeResponsePending) {
				Short temperature = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
				if (testModeCommandSelect == 0) {
					bist.setRfModuleTemp(Integer.toString(temperature));
				} else {
					bist.setAmbientTemp(Integer.toString(temperature));
				}
	    		setRfidState(RfidState.Idle);

			} else if (response.name().contains("RFID_MacGetError") && !testModeResponsePending) {
				int errorCode = CmdReaderModuleFirmwareAccess.RFID_MacGetError.parseResponse(dataBuffer);
				if (errorCode != 0) {
					log.error( "Last MTI MAC Firmware Error Code: 0x" + Integer.toHexString(errorCode));
				}
	    		setRfidState(RfidState.Idle);

			} else if (testModeResponsePending) {
				processTestModeResponses(response, dataBuffer);
	    		setRfidState(RfidState.Idle);
				
			} else {
				// A Response packet from anything else
	    		setRfidState(RfidState.Idle);
			}
			
		} else if (responseType.contains("Begin")) {
			// Do some packet processing here
    		setRfidState(RfidState.WaitingForEnd);			

		} else if (responseType.contains("Inventory")) {
			if (rfidState != RfidState.WaitingForEnd) {
	    		setRfidState(RfidState.WaitingForEnd);
			}
			// Process the tag data
			processInventoryResponse(dataBuffer);						
			
		} else if (responseType.contains("End")) {
			// Check the status word for an error
			int status = MtiCmd.getCmdEndStatus(dataBuffer);
			if (status != 0) {
				bist.setMtiStatusCode(status);
				log.error("MTI MAC Firmware Error Code: 0x" + Integer.toHexString(status));
				sendClearError();
			}
			// Return to idle state
    		setRfidState(RfidState.Idle);
			// See if we need to continue to read tags
			if (autoRepeat) {
				startInterScanTimer(profile.getDefaultDelayTime());
			}

		} else if (responseType.contains("Access")) {
			printTagAccessData(dataBuffer);

		} else if (responseType.contains("Work")) {
			//
			
		}		
	}

	/** 
	 * processInventoryResponse<P>
	 * This method parses the inventory response message.
	 * @param dataBuffer A byte buffer containing the command.
	 */
	private void processInventoryResponse( byte[] dataBuffer ) {
		// Extract the values of interest from the response buffer
		TagData tagData = new TagData();
		CmdTagProtocol.RFID_18K6CTagInventory.parseResponse(dataBuffer, false, tagData);
		if (tagData.crcValid) {
			tagPresent = true;
			if (testMode) {
				// Test mode processing of tag data
				System.out.println(tagData.epc);
				return;
			}
			TagData oldData = tagEvents.get(tagData.epc);
			boolean tryToTakePhoto = false;
			// Tags not in the event database cause a photo
			if (oldData == null) {
				tryToTakePhoto = true;
				oldData = tagData;
			}
			// Tags in the event database that have shots left cause a photo
			if ((oldData.shotCount < triggersPerEvent) && (oldData.triggerCountDown_sec == 0)) {
				tryToTakePhoto = true;
			}
			// If we should take photo, check if the camera is ready
			if (tryToTakePhoto && !camera.isBusy()) {
				// Trigger the camera
				if (camera.takePhoto(oldData.epc.substring(epcFirst, epcLast))) {
					numberOfTriggers++;
					// Update the event database
					oldData.shotCount++;				
					oldData.eventCountDown_sec = eventTimeout_sec;
					oldData.triggerCountDown_sec = triggerInterval_sec;
					tagEvents.put(oldData.epc, oldData);
					log.info(oldData.epc + " new trigger");
				}
			}
			// Update the all inclusive Tag Database
			tagDatabase.put(tagData.epc, tagData);
		}
	}
	
	/** 
	 * ageTagDatabase<P>
	 */
	private void ageTagEvents( ) throws NullPointerException {
		// Iterate through the entire ArrayList of tags
		Set<String> epcs = tagEvents.keySet();
		for (String epc: epcs) {
			TagData tagData = tagEvents.get(epc);
			if (tagData.triggerCountDown_sec > 0) {
				tagData.triggerCountDown_sec--;
				if (tagData.triggerCountDown_sec == 0 ) {
					log.debug(epc + " trigger holdoff expired");
				}
			}
			if (tagData.eventCountDown_sec > 0) {
				tagData.eventCountDown_sec--;
				if (tagData.eventCountDown_sec == 0 ) {
					log.debug(epc + " tagEvent expired");
					tagEvents.remove(epc);
				}
			}
		}
	}

	/** 
	 * uploadLeftoverPictures<P>
	 * This method attempts to upload any stored pictures.
	 */
	private boolean queueLeftoverFiles() {
		Integer oldFiles = 0;
		// Scrape the command line
		Process proc;
		try {
			proc = Runtime.getRuntime().exec("./get_leftovers.sh");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";
			while ((line = stdInput.readLine()) != null) {
				if ((line.length() > MIN_LENGTH_FILENAME) && line.endsWith(".jpg")) {
					pictureQueue.put(line);
					oldFiles++;
				}
			}
			if (oldFiles > 0) {
				log.info("Uploading " + oldFiles + " old files");
			}
			return true;
		} catch (Exception e) {
			log.warn( "Error reading directory!\n" + e.toString() );
			return false;
		}
	}

	/** 
	 * associateFileWithTagsAndUpload<P>
	 * This method associates all the currently read tags with this
	 * image file and uploads the record to the Fotaflo database.
	 */
	private void associateFileWithTagsAndUpload( String fileToUpload ) {
		// Check for a camera error
		if (fileToUpload.equalsIgnoreCase(camera.TIMEOUT)) {
			// flush the tagEvents to allow another photo to be taken
			tagEvents.clear();
			if (++downloadFailures > FAILURE_THRESHOLD) {
				downloadFailures = 0;
				sendEmails(location+"--"+deviceId, CAMERA_ERROR);
			}
		} else {
			downloadFailures = 0;
			// Get the one tag that triggered this photo
			String epcPlusTimestamp[] = fileToUpload.split("-");
			try {
				if (fotaflo.postImageToServer(fileToUpload, epcPlusTimestamp[0])) {
					numberOfUploads++;
					uploadFailures = 0;
				}
				else if (++uploadFailures > FAILURE_THRESHOLD) {
					uploadFailures = 0;
					sendEmails(location+"--"+deviceId, UPLOAD_ERROR);
				}

			} catch (Exception e) {
				log.error("Unable to upload image/tags\n" + e.toString());
				if (++uploadFailures > FAILURE_THRESHOLD) {
					uploadFailures = 0;
					sendEmails(location+"--"+deviceId, UPLOAD_ERROR);
				}
			}
		}
	}

	/** 
	 * showAllCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showTestModeOffCommands() {
		System.out.println( "\n\n" );
		System.out.println( "Cirrus-IIP Command Line Options" );
		System.out.println( "\n" );
		System.out.println( "test_mode_on  - Turns RFID Test Mode functionality ON." );
		System.out.println( "custom_macros - Displays the list of RFID Custom Macro commands." );
		System.out.println( "help          - Displays the list of Command Line Options." );
		System.out.println( "quit          - Shuts down the Cirrus-II application." );
		System.out.println( "\n" );
	}
	
	private void showTestModeOnCommands() {
		System.out.println( "\n\n" );
		System.out.println( "Cirrus-IIP Command Line Options - RFID Test Mode" );
		System.out.println( "\n" );
		System.out.println( "test_mode_off - Turns RFID Test Mode functionality OFF." );
		System.out.println( "config        - Displays the list of RFID Module Config commands." );
		System.out.println( "antenna       - Displays the list of RFID Antenna Config commands." );
		System.out.println( "tag_select    - Displays the list of RFID Tag Select commands." );
		System.out.println( "tag_access    - Displays the list of RFID Tag Access commands." );
		System.out.println( "tag_proto     - Displays the list of RFID Tag Protocol commands." );
		System.out.println( "control       - Displays the list of RFID Module Control commands." );
		System.out.println( "firmware      - Displays the list of RFID Firmware Access commands." );
		System.out.println( "gpio_ctrl     - Displays the list of RFID GPIO Control commands." );
		System.out.println( "test_support  - Displays the list of RFID Test Support commands." );
		System.out.println( "help          - Displays the list of Command Line Options." );
		System.out.println( "quit          - Shuts down the Cirrus-II application." );
		System.out.println( "\n" );
	}
	
	/** 
	 * showModuleConfigCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showModuleConfigCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Module Config Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.2" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "set_device_id <deviceId>" );
		System.out.println( "get_device_id" );
		System.out.println( "set_op_mode <operationMode>" );
		System.out.println( "get_op_mode" );
		System.out.println( "set_link_profile <linkProfile>" );
		System.out.println( "get_link_profile" );
		System.out.println( "write_reg <address> <value>" );
		System.out.println( "read_reg <address>" );
		System.out.println( "write_banked_reg <address> <bank> <value>" );
		System.out.println( "read_banked_reg <address> <bank>" );
		System.out.println( "reg_info <address>" );
		System.out.println( "set_inv_format <format>" );
		System.out.println( "get_inv_format" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showAntennaConfigCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showAntennaConfigCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Antenna Config Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.3" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "set_port_state <vPort> <state>" );
		System.out.println( "get_port_state <vPort>" );
		System.out.println( "set_port_config <vPort> <powerLevel> <dwellTime> <numInvCycles> <pPort>" );
		System.out.println( "get_port_config <vPort>" );
		System.out.println( "set_sense_thresh <threshold>" );
		System.out.println( "get_sense_thresh" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showTagSelectCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showTagSelectCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Tag Select Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.4" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "set_active_criteria <cIndex> <state>" );
		System.out.println( "get_active_criteria <cIndex>" );
		System.out.println( "set_criteria <cIndex> <bank> <offset> <count> <target> <action>" );
		System.out.println( "get_criteria <cIndex>" );
		System.out.println( "set_select_mask <cIndex> <mIndex> <mask0> <mask1> <mask2> <mask3>" );
		System.out.println( "get_select_mask <cIndex> <mIndex>" );
		System.out.println( "set_post_match <match> <offset> <count>" );
		System.out.println( "get_post_match" );
		System.out.println( "set_post_mask <mIndex> <mask0> <mask1> <mask2> <mask3>" );
		System.out.println( "get_post_mask <mIndex>" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showTagAccessCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showTagAccessCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Tag Access Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.5" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "set_tag_group <selected> <session> <target>" );
		System.out.println( "get_tag_group" );
		System.out.println( "set_sing_algo <algorithm>" );
		System.out.println( "get_sing_algo" );
		System.out.println( "set_sing_parms_fixed <qValue> <retries> <toggle> <repeat>" );
		System.out.println( "set_sing_parms_dynam <startQ> <minQ> <maxQ> <retries> <toggle> <threshMult>" );
		System.out.println( "get_sing_parms <algorithm>" );
		System.out.println( "set_tag_passwd <password>" );
		System.out.println( "get_tag_passwd" );
		System.out.println( "set_tag_buffer <bufferIndex> <data>" );
		System.out.println( "get_tag_buffer <bufferIndex>" );
		System.out.println( "get_guard_tag_num" );
		System.out.println( "get_guard_tag_info <index>" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showTagProtocolCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showTagProtocolCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Tag Protocol Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.6" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "tag_inventory <select> <postMatch> <guardMode>" );
		System.out.println( "tag_read <bank> <offset> <count> <retries> <select> <postMatch>" );
		System.out.println( "tag_write <bank> <offset> <data> <retries> <select> <postMatch>" );
		System.out.println( "tag_kill <killPwd> <retries> <select> <postMatch>" );
		System.out.println( "tag_lock <killPerm> <accPerm> <epcPerm> <tidPerm> <userPerm> <retries> <select> <postMatch>" );
		System.out.println( "tag_multi_write <bank> <offset> <length> <retries> <select> <postMatch>" );
		System.out.println( "tag_block_write <bank> <offset> <length> <retries> <select> <postMatch>" );
		System.out.println( "tag_block_erase <bank> <offset> <length> <retries> <select> <postMatch>" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showModuleControlCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showModuleControlCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Module Control Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.7" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "inv_cancel" );
		System.out.println( "inv_pause" );
		System.out.println( "inv_resume" );
		System.out.println( "soft_reset" );
		System.out.println( "boot_reset" );
		System.out.println( "set_pwr_state <powerState>" );
		System.out.println( "get_pwr_state" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showFirmwareAccessCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showFirmwareAccessCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Firmware Access Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.8" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "get_fw_version" );
		System.out.println( "get_error <errorType>" );
		System.out.println( "clear_error" );
		System.out.println( "get_boot_version" );
		System.out.println( "set_region <region>" );
		System.out.println( "get_region" );
		System.out.println( "set_uart_baud_rate <rate>" );
		System.out.println( "get_uart_baud_rate" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showGpioControlCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showGpioControlCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID GPIO Control Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.9" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "set_gpio_cfg <mask> <config>" );
		System.out.println( "get_gpio_cfg" );
		System.out.println( "write_gpio <mask> <value>" );
		System.out.println( "read_gpio <mask>" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showTestSupportCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showTestSupportCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Test Support Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.10" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values can be in either decimal or hex format." );
		System.out.println( "      Hexadecimal values must be preceded by 0x." );
		System.out.println( "\n" );
		System.out.println( "test_set_port_cfg <pPort> <pwrLevel>" );
		System.out.println( "test_get_port_cfg" );
		System.out.println( "test_set_freq_cfg <chanFlag> <freqKHz>" );
		System.out.println( "test_get_freq_cfg" );
		System.out.println( "test_set_rand_pulse_time <onTime> <offTime>" );
		System.out.println( "test_get_rand_pulse_time" );
		System.out.println( "test_set_inv_cfg <continuous>" );
		System.out.println( "test_get_inv_cfg" );
		System.out.println( "test_cw_on" );
		System.out.println( "test_cw_off" );
		System.out.println( "test_inject_rand_data <count>" );
		System.out.println( "test_tx_rand_data <control> <duration> <type>" );
		System.out.println( "test_get_temp <source>" );
		System.out.println( "test_get_rf_power <source>" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showGatewayCommands<P>
	 * This method displays a list of all the valid commands.
	 */
	private void showMacroCommands() {
		System.out.println( "\n\n" );
		System.out.println( "RFID Macro Commands Currently Supported" );
		System.out.println( "See Software User's Guide Section 5.1" );
		System.out.println( "for command and parameter definitions." );
		System.out.println( "NOTE: Commands and parameters are separated by a single space." );
		System.out.println( "      Parameter values are in decimal format only." );
		System.out.println( "\n" );
		System.out.println( "start" );
		System.out.println( "stop" );
		System.out.println( "beacon_on" );
		System.out.println( "beacon_off" );
		System.out.println( "manual_trigger" );
		System.out.println( "show_database" );
		System.out.println( "flush_database" );
		System.out.println( "show_version" );
		System.out.println( "log_level <level>" );
		System.out.println( "rfid_config" );
		System.out.println( "camera_config" );
		System.out.println( "run_bit" );
		System.out.println( "reset" );
		System.out.println( "help" );
		System.out.println( "quit" );
		System.out.println( "\n" );
	}
	
	/** 
	 * showCameraConfig<P>
	 * This method displays the current camera configuration.
	 */
	private void showFotafloConfig() {
		System.out.println("\n\n");
		System.out.println( "Camera Location    = " + location );
		System.out.println( "Camera Name        = " + deviceId );
		System.out.println( "Auth Username      = " + username );
		System.out.println( "Auth Password      = " + password );
		System.out.println( "Photo Server URL   = " + photoUrl );
		System.out.println( "Image Format       = " + imageFormat );
		System.out.println( "Shots Per Trigger  = " + shotsPerTrigger );
		System.out.println( "Trigger Interval   = " + triggerInterval_sec + " seconds" );
		System.out.println( "Triggers Per Event = " + triggersPerEvent );
		System.out.println( "Event Timeout      = " + eventTimeout_sec + " seconds" );
		System.out.println( "Camera Make        = " + camera.getManufacturer());
		System.out.println( "Camera Model       = " + camera.getModel());
		System.out.println( "Camera Version     = " + camera.getVersion());
		System.out.println( "Camera Serial#     = " + camera.getSerialNumber());
		System.out.println( "Latitude/Longitude = " + latitude + " / " + longitude);
		System.out.println( "\n\n" );
		
	}
	
	/** 
	 * updateVisualIndicator<P>
	 * This method updated the LED on the Smart Antenna.
	 * @return void
	 */
	private void updateVisualIndicator( ) {
		// No sense on going any further in this case
		if (led == null) { return; }
		// Do things based on combination of the current RFID state and flags
		if (errorFlag) {
			led.set(LedControl.Color.Red, LedControl.BlinkState.MediumBlink);
		} else {
			switch (rfidState) {
			case Idle:
				led.set(LedControl.Color.Green, LedControl.BlinkState.Constant);
				break;
			case WaitingForResponse:
			case WaitingForBegin:
			case WaitingForInventory:
			case WaitingForAccess:
				led.set(LedControl.Color.Blue, LedControl.BlinkState.Constant);
				break;
			case WaitingForEnd:
				if (!tagPresent) {
					led.set(LedControl.Color.Blue, LedControl.BlinkState.Constant);
				} else {
					led.set(LedControl.Color.Blue, LedControl.BlinkState.FastBlink);
				}
				break;
			case WaitingForReset:
				led.set(LedControl.Color.Red, LedControl.BlinkState.Constant);
				break;
			}
		}
	}
		
	/** 
	 * setRfidState<P>
	 * This method sets the RFID State.
	 * @param RfidState newRfidState
	 * @return void
	 */
	private void setRfidState( RfidState newRfidState ) {
		log.debug(newRfidState.toString());
		synchronized(rfidState) {
			rfidState = newRfidState;
		}
		try {
			nextRfidState.put(rfidState);
		} catch (InterruptedException e) {
			log.error("Unable to queue the Next RFID State\n" + e.toString());
		}
	}
		
	/** 
	 * processCommandLineInput<P>
	 * This method processes the incoming RFID command.
	 * @param command Command to parse
	 * @throws InterruptedException 
	 */
	private void processCommandLineInput( String command ) throws InterruptedException {
		// Parse the command line input
		String cmd[] = command.split(" ");
		String method = cmd[0];
		// Certain command line input we do not queue for processing
		if (method.equalsIgnoreCase("help")) {
			if (testMode) {
				showTestModeOnCommands();
			} else {
				showTestModeOffCommands();
			}
		} else if (method.equalsIgnoreCase("quit")) {
			cleanup();
			System.exit(0); 
		} else if (method.equalsIgnoreCase("test_mode_on")) {
			System.out.println("Test Mode = ON\n");
			testMode = true;
			showTestModeOnCommands();
		} else if (method.equalsIgnoreCase("test_mode_off")) {
			System.out.println("Test Mode = OFF\n");
			testMode = false;
			showTestModeOffCommands();
		} else if (method.equalsIgnoreCase("config")) {
			showModuleConfigCommands();
		} else if (method.equalsIgnoreCase("antenna")) {
			showAntennaConfigCommands();
		} else if (method.equalsIgnoreCase("tag_select")) {
			showTagSelectCommands();
		} else if (method.equalsIgnoreCase("tag_access")) {
			showTagAccessCommands();
		} else if (method.equalsIgnoreCase("tag_proto")) {
			showTagProtocolCommands();
		} else if (method.equalsIgnoreCase("control")) {
			showModuleControlCommands();
		} else if (method.equalsIgnoreCase("firmware")) {
			showFirmwareAccessCommands();
		} else if (method.equalsIgnoreCase("gpio_ctrl")) {
			showGpioControlCommands();
		} else if (method.equalsIgnoreCase("test_support")) {
			showTestSupportCommands();
		} else if (method.equalsIgnoreCase("custom_macros")) {
			showMacroCommands();

		} else if (method.equalsIgnoreCase("start")) {
			System.out.println("Starting autonomous photo capture\n");
			try {
				autoRepeat = true;
				sendInventoryRequest();
			} catch (InterruptedException e) {
				log.error("Unable to queue serial command\n" + e.toString());
			}				
		} else if (method.equalsIgnoreCase("stop")) {
			System.out.println("Stopping autonomous photo capture\n");
			try {
				autoRepeat = false;
				sendInventoryCancel();
			} catch (InterruptedException e) {
				log.error("Unable to queue serial command\n" + e.toString());
			}				
		} else if (method.equalsIgnoreCase("beacon_on")) {
			System.out.println("Visual beacon enabled\n");
			led.beacon("Enable");
		} else if (method.equalsIgnoreCase("beacon_off")) {
			System.out.println("Visual beacon disabled\n");
			led.beacon("Disable");
		} else if (method.equalsIgnoreCase("manual_trigger")) {
			System.out.println("Manually trigger a burst using tag 1234567\n");
			TagData tagData = new TagData();
			tagData.epc = "1234567";
			tagData.eventCountDown_sec = eventTimeout_sec;
			tagPresent = true;
			tagEvents.put(tagData.epc, tagData);
			tagDatabase.put(tagData.epc, tagData);
			camera.takePhoto(tagData.epc);
		} else if (method.equalsIgnoreCase("show_database")) {
			printTagDatabase();
		} else if (method.equalsIgnoreCase("flush_database")) {
			System.out.println("Flushing Cirrus-II tag database");
			System.out.println(tagDatabase.size() + " tags erased");
			System.out.println("\n");
			tagDatabase.clear();
		} else if (method.equalsIgnoreCase("show_version")) {
			System.out.println("Cirrus-II Application = " + appVersionString);
			System.out.println("RFID Module Firmware Rev. = " + sipVersionString);
			System.out.println("\n");
		} else if (method.equalsIgnoreCase("rfid_config")) {
			profile.getProfile(false);
		} else if (method.equalsIgnoreCase("camera_config")) {
			showFotafloConfig();
		} else if (method.equalsIgnoreCase("run_bit")) {
			bist.sendBitResponseToCli();
		} else if (	method.equalsIgnoreCase("reset") ) {
			resetDevice();
			
		} else if (testMode) {
			// Individual LLCS commands are handled here
			try {
				executeTestModeCommand(command);				
			} catch (InterruptedException ie) {
				System.out.println("Unable to send serial command!\n");
			} catch (NumberFormatException nfe) {
				System.out.println("Invalid Number Format!\n");
			}
		}
	}

	/** 
	 * sendInventoryRequest<P>
	 * This method sends an Inventory command to RFID module.
	 * @return True means means successful update to the RFID module.
	 * @throws InterruptedException 
	 */
	private Boolean sendInventoryRequest() throws InterruptedException {
		// Determine if we will be performing a select or post match
		byte[] cmd = llcs.tagInventory( profile.getPerformSelect().getValue(),
										profile.getPerformPostMatch().getValue(),
										(byte) 0x00 );

		if (cmd != null) { serialCmdQueue.put(cmd); } else { return false; }
		return true;
	}
	
	/** 
	 * sendInventoryCancel<P>
	 * This method sends an Inventory cancel command to RFID module.
	 * @return True means means successful update to the RFID module.
	 * @throws InterruptedException 
	 */
	private Boolean sendInventoryCancel() throws InterruptedException {
    	log.info("Sending RFID_ControlCancel");
		byte[] cmd = llcs.controlCancel();
		// Send the packet out the serial port immediately
		if (cmd != null) {
			serialComms.serialWrite(cmd, cmd.length);
			return true;
		} else {
			return false;
		}
	}
	
	/** 
	 * sendSoftReset<P>
	 * This method sends a Reset command to RFID module.
	 * @return True means means successful update to the RFID module.
	 * @throws InterruptedException 
	 */
	private Boolean sendSoftReset() throws InterruptedException {
		serialCmdQueue.clear();
		setRfidState(RfidState.WaitingForReset);
		byte[] cmd = llcs.controlSoftReset();
		
		startModuleResetTimer(moduleResetWait_ms);

		// Send the packet out the serial port immediately
		if (cmd != null) {
			serialComms.serialWrite(cmd, cmd.length);
			return true;
		} else {
			return false;
		}
	}
	
	/** 
	 * sendClearError<P>
	 * This method sends a command to the RFID module to retrieve and
	 * clear the Error Code from the last error (i.e. (byte)1)
	 * @return True is successful
	 */
	private Boolean sendClearError() {
		byte[] cmd1 = llcs.getError((byte)1);
		byte[] cmd2 = llcs.clearError( );
		if ((cmd1 != null) && (cmd2 != null)) {
			try {
				serialCmdQueue.put(cmd1);
				serialCmdQueue.put(cmd2);
			} catch (Exception e) {
				log.error("Unable to queue serial commands\n" + e.toString());
			}
		}
		return true;
	}
	
	/** 
	 * updateStatistics<P>
	 * This method writs statistics to a file.
	 * @throws Exception 
	 */
	private void updateStatistics() throws Exception {

		numberOfUnique = tagDatabase.size();

		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter("./statistics.txt", false));

		bw.write("upTimeInSeconds  = " + ticTimerCount + "\n");
		bw.write("numberOfUniques  = " + numberOfUnique + "\n");
		bw.write("numberOfUploads  = " + numberOfUploads + "\n");
		bw.write("numberOfTriggers = " + numberOfTriggers + "\n");
		bw.write("waitingForCamera = " + camera.isBusy() + "\n");
		bw.write("currentRfidState = " + rfidState.toString() + "\n");
		bw.close();
	}
	
	/** 
	 * parseConfigFile<P>
	 * This method parses the config file.
	 * @throws IOException 
	 */
	private void parseConfigFile(String filename) throws IOException {
		BufferedReader br = null;
		String currentLine;
		br = new BufferedReader(new FileReader(filename));

		try {
			while ((currentLine = br.readLine()) != null) {
				String st[] = currentLine.split(" ");
				if (currentLine.startsWith("#")) {
					// Do nothing as this is a comment
				} else if (currentLine.startsWith("PHOTO_URL") && (st.length == 2)) {
					this.photoUrl = st[1];
				} else if (currentLine.startsWith("USERNAME") && (st.length == 2)) {
					this.username = st[1];
				} else if (currentLine.startsWith("PASSWORD") && (st.length == 2)) {
					this.password = st[1];
				} else if (currentLine.startsWith("LOCATION") && (st.length >= 2)) {
					this.location = currentLine.substring(9); // everything after LOCATION
				} else if (currentLine.startsWith("DEVICE_ID") && (st.length >= 2)) {
					this.deviceId = currentLine.substring(10); // everything after DEVICE_ID
				} else if (currentLine.startsWith("EPC_FIRST") && (st.length == 2)) {
					this.epcFirst = Integer.parseInt(st[1]);
				} else if (currentLine.startsWith("EPC_LAST") && (st.length == 2)) {
					this.epcLast = Integer.parseInt(st[1]);
				} else if (currentLine.startsWith("IMAGE_FORMAT") && (st.length == 2)) {
					this.imageFormat = st[1];
				} else if (currentLine.startsWith("SHOTS_PER_TRIGGER") && (st.length == 2)) {
					this.shotsPerTrigger = st[1];
				} else if (currentLine.startsWith("TRIGGERS_PER_EVENT") && (st.length == 2)) {
					this.triggersPerEvent = Integer.parseInt(st[1]);
				} else if (currentLine.startsWith("TRIGGER_INTERVAL_SEC") && (st.length == 2)) {
					this.triggerInterval_sec = Integer.parseInt(st[1]);
				} else if (currentLine.startsWith("EVENT_TIMEOUT_SEC") && (st.length == 2)) {
					this.eventTimeout_sec = Integer.parseInt(st[1]);
                } else if (currentLine.startsWith("ALERT_EMAIL") && (st.length == 2)) {
                    alertEmailList.add(st[1]);
				}
			}
		} catch (IOException e) {
			System.out.println("Unable to open config file\n" + e.toString());
		}
		br.close();
	}

	/** 
	 * printTagDatabase<P>
	 * This method prints the Inventory Tag Database.
	 * @param tagList An Array of TagData objects.
	 */
	private void printTagDatabase( ) {
		// Iterate through the entire ConcurrentHashMap
		Set<String> epcs = tagDatabase.keySet();
		System.out.println( "Unique Tag Count = " + epcs.size() );
		for (String epc: epcs) {
			TagData tagData = tagDatabase.get(epc);
			System.out.println( "EPC = " + tagData.epc );
		}
	}
	
	/** 
	 * printTagAccessData<P>
	 * This method prints the tag access data.
	 * @param dataBuffer A byte buffer containing the command.
	 */
	private void printTagAccessData( byte[] dataBuffer ) {
		// Extract the values of interest from the response buffer
		System.out.println( "Tag Access Data" );
		System.out.println( CmdTagProtocol.RFID_18K6CTagRead.parseAccessPacket(dataBuffer, true) );
		System.out.println( "\n" );
	}
	
	/** 
	 * executeTestModeCommand<P>
	 * This method executes a command line test command.
	 * @param command The command line input
	 * @throws InterruptedException 
	 */
	private void executeTestModeCommand(String command) throws InterruptedException, NumberFormatException {
		// Stop any gateway controlled inventory cycle
		autoRepeat = false;
		testModeResponsePending = true;
		String cli[] = command.split(" ");

		// RFID Module Config Commands Currently Supported
		// See MTI Command Reference Manual Section 4.1
		if ((command.startsWith("set_device_id")) && (cli.length >= 2)) {
			deviceId = cli[1];
			byte[] cmd = llcs.setDeviceID((byte)deviceId.hashCode());
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_device_id")) {
			byte[] cmd = llcs.getDeviceID();
			if (cmd != null) { serialCmdQueue.put(cmd); }
		
		} else if ((command.startsWith("set_op_mode")) && (cli.length >= 2)) {
			byte operationMode = Byte.decode(cli[1]);
			byte[] cmd = llcs.setOperationMode(operationMode);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_op_mode")) {
			byte[] cmd = llcs.getOperationMode();
			if (cmd != null) { serialCmdQueue.put(cmd); }
			
		} else if ((command.startsWith("set_link_profile")) && (cli.length >= 2)) {
			byte linkProfile = Byte.decode(cli[1]);
			byte[] cmd = llcs.setCurrentLinkProfile(linkProfile);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_link_profile")) {
			byte[] cmd = llcs.getCurrentLinkProfile();
			if (cmd != null) { serialCmdQueue.put(cmd); }
			
		} else if ((command.startsWith("write_reg")) && (cli.length >= 3)) {
			short address = Short.decode(cli[1]);
			int value = Integer.decode(cli[2]);
			byte[] cmd = llcs.writeRegister(address, value);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("read_reg")) && (cli.length >= 2)) {
			short address = Short.decode(cli[1]);
			byte[] cmd = llcs.readRegister(address);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("write_banked_reg")) && (cli.length >= 4)) {
			short address = Short.decode(cli[1]);
			short bank = Short.decode(cli[2]);
			int value = Integer.decode(cli[3]);
			byte[] cmd = llcs.writeRegister(address, bank, value);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("read_banked_reg")) && (cli.length >= 3)) {
			short address = Short.decode(cli[1]);
			short bank = Short.decode(cli[2]);
			byte[] cmd = llcs.readRegister(address, bank);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("reg_info")) && (cli.length >= 2)) {
			short address = Short.decode(cli[1]);
			byte[] cmd = llcs.readRegisterInfo(address);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("set_inv_format")) && (cli.length >= 2) && moduleType.startsWith("HPSIP")) {
			byte packetFormat = Byte.decode(cli[1]);
			byte[] cmd = llcs.setInventoryPacketFormat(packetFormat);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_inv_format") && moduleType.startsWith("HPSIP")) {
			byte[] cmd = llcs.getInventoryPacketFormat();
			if (cmd != null) { serialCmdQueue.put(cmd); }
			
		// RFID Antenna Config Commands Currently Supported
		// See MTI Command Reference Manual Section 4.2
		} else if ((command.startsWith("set_port_state")) && (cli.length >= 3)) {
			byte antennaPort = Byte.decode(cli[1]);
			byte state = Byte.decode(cli[2]);
			byte[] cmd = llcs.antennaPortSetState(antennaPort, state);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("get_port_state")) && (cli.length >= 2)) {
			byte antennaPort = Byte.decode(cli[1]);
			byte[] cmd = llcs.antennaPortGetState(antennaPort);
			if (cmd != null) { serialCmdQueue.put(cmd); }
				
		} else if ((command.startsWith("set_port_config")) && (cli.length >= 6)) {
			byte antennaPort = Byte.decode(cli[1]);
			short powerLevel = Short.decode(cli[2]);
			short dwellTime = Short.decode(cli[3]);
			short numberInventoryCycles = Short.decode(cli[4]);
			byte physicalPort = Byte.decode(cli[5]);
			byte[] cmd = llcs.antennaPortSetConfiguration(antennaPort, powerLevel, dwellTime, numberInventoryCycles, physicalPort);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("get_port_config")) && (cli.length >= 2)) {
			byte antennaPort = Byte.decode(cli[1]);
			byte[] cmd = llcs.antennaPortGetConfiguration(antennaPort);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("set_sense_thresh")) && (cli.length >= 2)) {
			int antennaSenseThreshold = Integer.decode(cli[1]);
			byte[] cmd = llcs.antennaPortSetSenseThreshold(antennaSenseThreshold);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_sense_thresh")) {
			byte[] cmd = llcs.antennaPortGetSenseThreshold();
			if (cmd != null) { serialCmdQueue.put(cmd); }
			
		// RFID Tag Select Commands Currently Supported
		// See MTI Command Reference Manual Section 4.3
		} else if ((command.startsWith("set_active_criteria")) && (cli.length >= 3)) {
			byte criteriaIndex = Byte.decode(cli[1]);
			byte activeState = Byte.decode(cli[2]);
			byte[] cmd = llcs.setActiveSelectCriteria(criteriaIndex, activeState);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if (command.startsWith("get_active_criteria")) {
			byte criteriaIndex = Byte.decode(cli[1]);
			byte[] cmd = llcs.getActiveSelectCriteria(criteriaIndex);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
				
		} else if ((command.startsWith("set_criteria")) && (cli.length >= 7)) {
			byte criteriaIndex = Byte.decode(cli[1]);
			byte bank = Byte.decode(cli[2]);
			short offset = Short.decode(cli[3]);
			byte count = Byte.decode(cli[4]);
			byte target = Byte.decode(cli[5]);
			byte action = Byte.decode(cli[6]);
			byte truncation = (byte)0;
			byte[] cmd = llcs.setSelectCriteria(criteriaIndex, bank, offset, count, target, action, truncation);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if (command.startsWith("get_criteria")) {
			byte criteriaIndex = Byte.decode(cli[1]);
			byte[] cmd = llcs.getSelectCriteria(criteriaIndex);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
				
		} else if ((command.startsWith("set_select_mask")) && (cli.length >= 7)) {
			byte criteriaIndex = Byte.decode(cli[1]);
			byte maskIndex = Byte.decode(cli[2]);
			byte maskData0 = (byte)(Short.decode(cli[3]) & 0x00FF);
			byte maskData1 = (byte)(Short.decode(cli[4]) & 0x00FF);
			byte maskData2 = (byte)(Short.decode(cli[5]) & 0x00FF);
			byte maskData3 = (byte)(Short.decode(cli[6]) & 0x00FF);
			byte[] cmd = llcs.setSelectMaskData(criteriaIndex, maskIndex, maskData0, maskData1, maskData2, maskData3);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("get_select_mask")) && (cli.length >= 3)) {
			byte criteriaIndex = Byte.decode(cli[1]);
			byte maskIndex = Byte.decode(cli[2]);
			byte[] cmd = llcs.getSelectMaskData(criteriaIndex, maskIndex);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("set_post_match")) && (cli.length >= 4)) {
			byte match = Byte.decode(cli[1]);
			short offset = Short.decode(cli[2]);
			short count = Short.decode(cli[3]);
			byte[] cmd = llcs.setPostMatchCriteria(match, offset, count);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_post_match")) {
			byte[] cmd = llcs.getPostMatchCriteria();
			if (cmd != null) { serialCmdQueue.put(cmd); }
				
		} else if ((command.startsWith("set_post_mask")) && (cli.length >= 6)) {
			byte maskIndex = Byte.decode(cli[1]);
			byte maskData0 = (byte)(Short.decode(cli[2]) & 0x00FF);
			byte maskData1 = (byte)(Short.decode(cli[3]) & 0x00FF);
			byte maskData2 = (byte)(Short.decode(cli[4]) & 0x00FF);
			byte maskData3 = (byte)(Short.decode(cli[5]) & 0x00FF);
			byte[] cmd = llcs.setPostMatchMaskData(maskIndex, maskData0, maskData1, maskData2, maskData3);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("get_post_mask")) && (cli.length >= 2)) {
			byte maskIndex = Byte.decode(cli[1]);
			byte[] cmd = llcs.getPostMatchMaskData(maskIndex);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		// RFID Tag Access Commands Currently Supported
		// See MTI Command Reference Manual Section 4.3
		} else if ((command.startsWith("set_tag_group")) && (cli.length >= 4)) {
			byte selected = Byte.decode(cli[1]);
			byte session = Byte.decode(cli[2]);
			byte target = Byte.decode(cli[3]);
			byte[] cmd = llcs.setQueryTagGroup(selected, session, target);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_tag_group")) {
			byte[] cmd = llcs.getQueryTagGroup();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("set_sing_algo")) && (cli.length >= 2)) {
			byte algorithm = Byte.decode(cli[1]);
			byte[] cmd = llcs.setCurrentSingulationAlgorithm(algorithm);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_sing_algo")) {
			byte[] cmd = llcs.getCurrentSingulationAlgorithm();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("set_sing_parms_fixed")) && (cli.length >= 5)) {
			byte qValue = Byte.decode(cli[1]);
			byte retryCount = Byte.decode(cli[2]);
			byte toggleTarget = Byte.decode(cli[3]);
			byte repeatUntilNoTags = Byte.decode(cli[4]);
			byte[] cmd = llcs.setCurrentSingulationParameters((byte)0, qValue, retryCount, toggleTarget, repeatUntilNoTags);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("set_sing_parms_dynam")) && (cli.length >= 7)) {
			byte startQvalue = Byte.decode(cli[1]);
			byte minQvalue = Byte.decode(cli[2]);
			byte maxQvalue = Byte.decode(cli[3]);
			byte retryCount = (byte)(Short.decode(cli[4]) & 0xFF);
			byte toggleTarget = Byte.decode(cli[5]);
			byte thresholdMultiplier = (byte)(Short.decode(cli[6]) & 0xFF);
			byte[] cmd = llcs.setCurrentSingulationParameters((byte)1, startQvalue, minQvalue, maxQvalue, retryCount, toggleTarget, thresholdMultiplier);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("get_sing_parms")) && (cli.length >= 2)) {
			testModeCommandSelect = Byte.decode(cli[1]);
			byte[] cmd = llcs.getCurrentSingulationAlgorithmParameters(testModeCommandSelect);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("set_tag_passwd")) && (cli.length >= 2)) {
			int password = (int)(Long.decode(cli[1]) & 0xFFFFFFFF);
			byte[] cmd = llcs.setTagAccessPassword(password);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_tag_passwd")) {
			byte[] cmd = llcs.getTagAccessPassword();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("set_tag_buffer")) && (cli.length >= 3)) {
			byte bufferIndex = (byte)(Short.decode(cli[1]) & 0xFF);
			short bufferData = (short)(Integer.decode(cli[2]) & 0xFFFF);
			byte[] cmd = llcs.setTagWriteDataBuffer(bufferIndex, bufferData);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("get_tag_buffer")) && (cli.length >= 2)) {
			byte bufferIndex = Byte.decode(cli[1]);
			byte[] cmd = llcs.getTagWriteDataBuffer(bufferIndex);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_guard_tag_num")) {
			byte[] cmd = llcs.getGuardBufferTagNum();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("get_guard_tag_info")) && (cli.length >= 2)) {
			byte bufferIndex = Byte.decode(cli[1]);
			byte[] cmd = llcs.getGuardBufferTagInfo(bufferIndex);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		// RFID Tag Protocol Commands Currently Supported
		// See MTI Command Reference Manual Section 4.4
		} else if ((command.startsWith("tag_inventory")) && (cli.length >= 4)) {
			byte select = Byte.decode(cli[1]);
			byte postMatch = Byte.decode(cli[2]);
			byte guardMode = Byte.decode(cli[3]);
			byte[] cmd = llcs.tagInventory(select, postMatch, guardMode);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("tag_read")) && (cli.length >= 7)) {
			byte bank = Byte.decode(cli[1]);
			short offset = Short.decode(cli[2]);
			byte count = Byte.decode(cli[3]);
			byte retryCount = Byte.decode(cli[4]);
			byte select = Byte.decode(cli[5]);
			byte postMatch = Byte.decode(cli[6]);
			byte[] cmd = llcs.tagRead(bank, offset, count, retryCount, select, postMatch);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("tag_write")) && (cli.length >= 7)) {
			byte bank = Byte.decode(cli[1]);
			short offset = Short.decode(cli[2]);
			short data = Short.decode(cli[3]);
			byte retryCount = Byte.decode(cli[4]);
			byte select = Byte.decode(cli[5]);
			byte postMatch = Byte.decode(cli[6]);
			byte[] cmd = llcs.tagWrite(bank, offset, data, retryCount, select, postMatch);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("tag_kill")) && (cli.length >= 5)) {
			int killPwd = Integer.decode(cli[1]);
			byte retryCount = Byte.decode(cli[2]);
			byte select = Byte.decode(cli[3]);
			byte postMatch = Byte.decode(cli[4]);
			byte[] cmd = llcs.tagKill(killPwd, retryCount, select, postMatch);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("tag_lock")) && (cli.length >= 9)) {
			byte killPerm = Byte.decode(cli[1]);
			byte accPerm = Byte.decode(cli[2]);
			byte epcPerm = Byte.decode(cli[3]);
			byte tidPerm = Byte.decode(cli[4]);
			byte userPerm = Byte.decode(cli[5]);
			byte retryCount = Byte.decode(cli[6]);
			byte select = Byte.decode(cli[7]);
			byte postMatch = Byte.decode(cli[8]);
			byte[] cmd = llcs.tagLock(killPerm, accPerm, epcPerm, tidPerm, userPerm, retryCount, select, postMatch);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("tag_multi_write")) && (cli.length >= 7)) {
			byte bank = Byte.decode(cli[1]);
			short offset = Short.decode(cli[2]);
			byte dataLength = Byte.decode(cli[3]);
			byte retryCount = Byte.decode(cli[4]);
			byte select = Byte.decode(cli[5]);
			byte postMatch = Byte.decode(cli[6]);
			byte[] cmd = llcs.tagMultipleWrite(bank, offset, dataLength, retryCount, select, postMatch);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("tag_block_write")) && (cli.length >= 7)) {
			byte bank = Byte.decode(cli[1]);
			short offset = Short.decode(cli[2]);
			byte dataLength = Byte.decode(cli[3]);
			byte retryCount = Byte.decode(cli[4]);
			byte select = Byte.decode(cli[5]);
			byte postMatch = Byte.decode(cli[6]);
			byte[] cmd = llcs.tagBlockWrite(bank, offset, dataLength, retryCount, select, postMatch);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("tag_block_erase")) && (cli.length >= 7)) {
			byte bank = Byte.decode(cli[1]);
			short offset = Short.decode(cli[2]);
			byte dataLength = Byte.decode(cli[3]);
			byte retryCount = Byte.decode(cli[4]);
			byte select = Byte.decode(cli[5]);
			byte postMatch = Byte.decode(cli[6]);
			byte[] cmd = llcs.tagBlockErase(bank, offset, dataLength, retryCount, select, postMatch);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		// RFID Module Control Commands Currently Supported
		// See MTI Command Reference Manual Section 4.5
		} else if (command.startsWith("inv_cancel")) {
			byte[] cmd = llcs.controlCancel();
			// Send the packet out the serial port immediately
			if (cmd != null) { serialComms.serialWrite(cmd, cmd.length); }

		} else if (command.startsWith("inv_pause")) {
			byte[] cmd = llcs.controlPause();
			// Send the packet out the serial port immediately
			if (cmd != null) { serialComms.serialWrite(cmd, cmd.length); }

		} else if (command.startsWith("inv_resume")) {
			byte[] cmd = llcs.controlResume();
			// Send the packet out the serial port immediately
			if (cmd != null) { serialComms.serialWrite(cmd, cmd.length); }

		} else if (command.startsWith("soft_reset")) {
			sendSoftReset();

		} else if (command.startsWith("boot_reset")) {
			byte[] cmd = llcs.controlResetToBootloader();
			// Send the packet out the serial port immediately
			if (cmd != null) { serialComms.serialWrite(cmd, cmd.length); }

		} else if ((command.startsWith("set_pwr_state")) && (cli.length >= 2)) {
			byte powerState = Byte.decode(cli[1]);
			byte[] cmd = llcs.controlSetPowerState(powerState);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_pwr_state")) {
			byte[] cmd = llcs.controlGetPowerState();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		// RFID Firmware Access Commands Currently Supported
		// See MTI Command Reference Manual Section 4.6
		} else if (command.startsWith("get_fw_version")) {
			byte[] cmd = llcs.getFirmwareVersion();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if (command.startsWith("get_debug")) {
			byte[] cmd = llcs.getDebug();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if (command.startsWith("clear_error")) {
			byte[] cmd = llcs.clearError();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("get_error")) && (cli.length >= 2)) {
			byte errorType = Byte.decode(cli[1]);
			byte[] cmd = llcs.getError(errorType);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_boot_version")) {
			byte[] cmd = llcs.getBootloaderVersion();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("write_oem_data")) && (cli.length >= 3)) {
			short address = Short.decode(cli[1]);
			int data = Integer.decode(cli[2]);
			byte[] cmd = llcs.writeOemData(address, data);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("read_oem_data")) && (cli.length >= 2)) {
			short address = Short.decode(cli[1]);
			byte[] cmd = llcs.readOemData(address);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("write_bypass_reg")) && (cli.length >= 3)) {
			short address = Short.decode(cli[1]);
			short data = Short.decode(cli[2]);
			byte[] cmd = llcs.bypassWriteRegister(address, data);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("read_bypass_reg")) && (cli.length >= 2)) {
			short address = Short.decode(cli[1]);
			byte[] cmd = llcs.bypassReadRegister(address);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("set_region")) && (cli.length >= 2)) {
			byte regionOperation = Byte.decode(cli[1]);
			byte[] cmd = llcs.setRegion(regionOperation);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_region")) {
			byte[] cmd = llcs.getRegion();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if (command.startsWith("get_oem_cfg")) {
			byte[] cmd = llcs.getOEMCfgVersion();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if (command.startsWith("get_oem_cfg_update")) {
			byte[] cmd = llcs.getOEMCfgUpdateNumber();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("get_uart_baud_rate")) && moduleType.startsWith("HPSIP")) {
			byte[] cmd = llcs.getUartBaudRate();
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
						
		// RFID GPIO Control Commands Currently Supported
		// See MTI Command Reference Manual Section 4.7
		} else if ((command.startsWith("set_gpio_cfg")) && (cli.length >= 3)) {
			byte mask = Byte.decode(cli[1]);
			byte configuration = Byte.decode(cli[2]);
			byte[] cmd = llcs.setGpioPinsConfiguration(mask, configuration);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if (command.startsWith("get_gpio_cfg")) {
			byte[] cmd = llcs.getGpioPinsConfiguration();
			if (cmd != null) { serialCmdQueue.put(cmd); }

		} else if ((command.startsWith("write_gpio")) && (cli.length >= 3)) {
			byte mask = Byte.decode(cli[1]);
			byte value = Byte.decode(cli[2]);
			byte[] cmd = llcs.writeGpioPins(mask, value);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		} else if ((command.startsWith("read_gpio")) && (cli.length >= 2)) {
			byte mask = Byte.decode(cli[1]);
			byte[] cmd = llcs.readGpioPins(mask);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }

		// RFID Test Support Commands Currently Supported
		// See MTI Command Reference Manual Section 4.8
		} else if ((command.startsWith("test_set_port_cfg")) && (cli.length >= 3)) {
			byte physicalPort = Byte.decode(cli[1]);
			short powerLevel = Short.decode(cli[2]);
			byte[] cmd = llcs.testSetAntennaPortConfiguration(physicalPort, powerLevel);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if (command.startsWith("test_get_port_cfg")) {
			byte[] cmd = llcs.testGetAntennaPortConfiguration();
			if (cmd != null) { serialCmdQueue.put(cmd); }
				
		} else if ((command.startsWith("test_set_freq_cfg")) && (cli.length >= 3)) {
			byte channelFlag = Byte.decode(cli[1]);
			int exactFrequency = Integer.decode(cli[2]);
			byte[] cmd = llcs.testSetFrequencyConfiguration(channelFlag, exactFrequency);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if (command.startsWith("test_get_freq_cfg")) {
			byte[] cmd = llcs.testGetFrequencyConfiguration();
			if (cmd != null) { serialCmdQueue.put(cmd); }
				
		} else if ((command.startsWith("test_set_rand_pulse_time")) && (cli.length >= 3)) {
			short onTime = Short.decode(cli[1]);
			short offTime = Short.decode(cli[2]);
			byte[] cmd = llcs.testSetRandomDataPulseTime(onTime, offTime);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if (command.startsWith("test_get_rand_pulse_time")) {
			byte[] cmd = llcs.testGetRandomDataPulseTime();
			if (cmd != null) { serialCmdQueue.put(cmd); }
				
		} else if ((command.startsWith("test_set_inv_cfg")) && (cli.length >= 2)) {
			byte continuousOperation = Byte.decode(cli[1]);
			byte[] cmd = llcs.testSetInventoryConfiguration(continuousOperation);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if (command.startsWith("test_get_inv_cfg")) {
			byte[] cmd = llcs.testGetInventoryConfiguration();
			if (cmd != null) { serialCmdQueue.put(cmd); }
				
		} else if (command.startsWith("test_cw_on")) {
			byte[] cmd = llcs.testTurnOnCarrierWave();
			if (cmd != null) { serialCmdQueue.put(cmd); }
			
		} else if (command.startsWith("test_cw_off")) {
			byte[] cmd = llcs.testTurnOffCarrierWave();
			if (cmd != null) { serialCmdQueue.put(cmd); }
						
		} else if ((command.startsWith("test_inject_rand_data")) && (cli.length >= 2)) {
			int count = Integer.decode(cli[1]);
			byte[] cmd = llcs.testInjectRandomData(count);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
			
		} else if ((command.startsWith("test_tx_rand_data")) && (cli.length >= 4)) {
			byte control = Byte.decode(cli[1]);
			int duration = Integer.decode(cli[2]);
			byte randomType = Byte.decode(cli[3]);
			byte[] cmd = llcs.testTransmitRandomData(control, duration, randomType);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
						
		} else if ((command.startsWith("test_get_temp")) && (cli.length >= 2) && moduleType.startsWith("HPSIP")) {
			testModeCommandSelect = Byte.decode(cli[1]);
			byte[] cmd = llcs.testGetTemperature(testModeCommandSelect);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
						
		} else if ((command.startsWith("test_get_rf_power")) && (cli.length >= 2) && moduleType.startsWith("HPSIP")) {
			testModeCommandSelect = Byte.decode(cli[1]);
			byte[] cmd = llcs.testGetRFPower(testModeCommandSelect);
			if (cmd != null) { serialCmdQueue.put(cmd); }
			else { System.out.println("Invalid parameter or format for " + cli[0]); }
						
		} else {
			// No valid command
			testModeResponsePending = false;
		}
	}

	/** 
	 * processTestModeResponses<P>
	 * This method processes serial responses to test mode commands.
	 * @param response The command name in the response
	 * @param dataBuffer A byte buffer containing the response.
	 */
	private void processTestModeResponses(CmdHead response, byte[] dataBuffer) {
		testModeResponsePending = false;
		// Check for success
		Byte status = dataBuffer[MtiCmd.RESP_DATA_INDEX];
		if (status.byteValue() == 0x00) {
			System.out.println(response + " success");
		} else {
			System.out.println(response + " failed!");
			System.out.println("\n");
			return;
		}
		
		if (response.name().contains("RFID_RadioSetDeviceID")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioGetDeviceID")) {
			Byte deviceId = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Device ID = " + Byte.toString(deviceId));
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioSetOperationMode")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioGetOperationMode")) {
			Byte operationMode = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Operation Mode = " + Byte.toString(operationMode));
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioSetCurrentLinkProfile")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioGetCurrentLinkProfile")) {
			Byte profile = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Link Profile = " + Byte.toString(profile));
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioWriteRegister")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioReadRegisterInfo")) {
			Short selectorAddress = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 4);
			Short currentSelector = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 6);
			System.out.println("Register Type    = 0x" + MtiCmd.byteArrayToString(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1, 1, false));					
			System.out.println("Access Type      = 0x" + MtiCmd.byteArrayToString(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2, 1, false));					
			System.out.println("Bank Size        = 0x" + MtiCmd.byteArrayToString(dataBuffer, MtiCmd.RESP_DATA_INDEX + 3, 1, false));					
			System.out.println("Selector Address = 0x" + Integer.toHexString(selectorAddress));					
			System.out.println("Current Selector = 0x" + Integer.toHexString(currentSelector));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioReadRegister")) {
			Integer value = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Register Value = 0x" + Integer.toHexString(value));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioWriteBankedRegister")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioReadBankedRegister")) {
			Integer value = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Register Value = 0x" + Integer.toHexString(value));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioSetInventoryPacketFormat")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioGetInventoryPacketFormat")) {
			Byte format = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Inventory Packet Format = " + format.toString());
			System.out.println("\n");

		} else if (response.name().contains("RFID_AntennaPortSetState")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_AntennaPortGetState")) {
			Byte state = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Integer senseValue = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2);
			System.out.println("Active State        = " + state.toString());
			System.out.println("Antenna Sense Value = " + senseValue.toString());
			System.out.println("\n");

		} else if (response.name().contains("RFID_AntennaPortSetConfiguration")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_AntennaPortGetConfiguration")) {
			Short power = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			Short dwellTime = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 3);
			Short invCycles = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 5);
			Byte physical = dataBuffer[MtiCmd.RESP_DATA_INDEX + 7];
			System.out.println("Power Level dBm            = " + Float.toString(power.floatValue()/10));
			System.out.println("Dwell Time milliseconds    = " + dwellTime.toString());					
			System.out.println("Number of Inventory Cycles = " + invCycles.toString());					
			System.out.println("Physical Port              = " + physical.toString());					
			System.out.println("\n");

		} else if (response.name().contains("RFID_AntennaPortSetSenseThreshold")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_AntennaPortGetSenseThreshold")) {
			Integer thresh = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Global Antenna Threshold = " + Integer.toString(thresh));
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetActiveSelectCriteria")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetActiveSelectCriteria")) {
			Byte state = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Active State = " + Byte.toString(state));
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetSelectCriteria")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetSelectCriteria")) {
			Byte bank = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Short offset = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2);
			Byte count = dataBuffer[MtiCmd.RESP_DATA_INDEX + 4];
			Byte target = dataBuffer[MtiCmd.RESP_DATA_INDEX + 5];
			Byte action = dataBuffer[MtiCmd.RESP_DATA_INDEX + 6];
			Byte truncation = dataBuffer[MtiCmd.RESP_DATA_INDEX + 7];
			System.out.println("Memory Bank = " + Byte.toString(bank));
			System.out.println("Bit Offset  = " + Short.toString(offset));					
			System.out.println("Bit Count   = " + Byte.toString(count));					
			System.out.println("Target Flag = " + Byte.toString(target));					
			System.out.println("Action      = " + Byte.toString(action));					
			System.out.println("Truncation  = " + Byte.toString(truncation));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetSelectMaskData")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetSelectMaskData")) {
			Integer mask = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Mask Data [3:0] = 0x" + Integer.toHexString(mask));
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetPostMatchCriteria")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetPostMatchCriteria")) {
			Byte match = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Short offset = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2);
			Short count = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 4);
			System.out.println("Match State = " + Byte.toString(match));
			System.out.println("Bit Offset  = " + Short.toString(offset));					
			System.out.println("Bit Count   = " + Short.toString(count));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetPostMatchMaskData")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetPostMatchMaskData")) {
			Byte maskIndex = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Integer mask = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2);
			System.out.println("Mask Index      = " + Byte.toString(maskIndex));
			System.out.println("Mask Data [3:0] = 0x" + Integer.toHexString(mask));
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetQueryTagGroup")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetQueryTagGroup")) {
			Byte selected = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Byte session = dataBuffer[MtiCmd.RESP_DATA_INDEX + 2];
			Byte target = dataBuffer[MtiCmd.RESP_DATA_INDEX + 3];
			System.out.println("Selected = " + Byte.toString(selected));
			System.out.println("Session  = " + Byte.toString(session));
			System.out.println("Target   = " + Byte.toString(target));
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetCurrentSingulationAlgorithmParameters")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetCurrentSingulationAlgorithmParameters")) {
			if (testModeCommandSelect == 0) {
				Byte qValue = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
				Byte retries = dataBuffer[MtiCmd.RESP_DATA_INDEX + 2];
				Byte toggle = dataBuffer[MtiCmd.RESP_DATA_INDEX + 3];
				Byte repeat = dataBuffer[MtiCmd.RESP_DATA_INDEX + 4];
				System.out.println("Q Vlaue              = " + Byte.toString(qValue));
				System.out.println("Retry Count          = " + Byte.toString(retries));
				System.out.println("Toggle Target        = " + Byte.toString(toggle));
				System.out.println("Repeat Until No Tags = " + Byte.toString(repeat));
				System.out.println("\n");
			} else {
				Byte startQ = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
				Byte minQ = dataBuffer[MtiCmd.RESP_DATA_INDEX + 2];
				Byte maxQ = dataBuffer[MtiCmd.RESP_DATA_INDEX + 3];
				Byte retries = dataBuffer[MtiCmd.RESP_DATA_INDEX + 4];
				Byte toggle = dataBuffer[MtiCmd.RESP_DATA_INDEX + 5];
				Byte thresh = dataBuffer[MtiCmd.RESP_DATA_INDEX + 6];
				System.out.println("Starting Q           = " + Byte.toString(startQ));
				System.out.println("Minimum Q            = " + Byte.toString(minQ));
				System.out.println("Maximum Q            = " + Byte.toString(maxQ));
				System.out.println("Retry Count          = " + Byte.toString(retries));
				System.out.println("Toggle Target        = " + Byte.toString(toggle));
				System.out.println("Threshold Multiplier = " + Byte.toString(thresh));
				System.out.println("\n");
			}

		} else if (response.name().contains("RFID_18K6CSetCurrentSingulationAlgorithm")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetCurrentSingulationAlgorithm")) {
			Byte algorithm = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Algorithm = " + Byte.toString(algorithm));
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetTagAccessPassword")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetTagAccessPassword")) {
			Integer password = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Password = 0x" + Integer.toHexString(password));
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CSetTagWriteDataBuffer")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetTagWriteDataBuffer")) {
			Short value = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Buffer Value = 0x" + Integer.toHexString(value & 0xFFFF));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetGuardBufferTagNum")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CGetGuardBufferTagInfo")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagInventory")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagRead")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagWrite")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagKill")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagLock")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagMultipleWrite")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagBlockWrite")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_18K6CTagBlockErase")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_ControlCancel")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_ControlPause")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_ControlResume")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_ControlSoftReset")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_ControlResetToBootloader")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_ControlSetPowerState")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_ControlGetPowerState")) {
			Byte state = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("State = " + Byte.toString(state));
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacGetFirmwareVersion")) {
			Byte major = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Byte minor = dataBuffer[MtiCmd.RESP_DATA_INDEX + 2];
			Byte patch = dataBuffer[MtiCmd.RESP_DATA_INDEX + 3];
			System.out.println("Major = " + Byte.toString(major));
			System.out.println("Minor = " + Byte.toString(minor));
			System.out.println("Patch = " + Byte.toString(patch));
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacGetDebug")) {
			Integer data = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Debug Value = 0x" + Integer.toHexString(data));
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacClearError")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacGetError")) {
			Integer data = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Error Code = 0x" + Integer.toHexString(data));
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacGetBootloaderVersion")) {
			Byte major = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Byte minor = dataBuffer[MtiCmd.RESP_DATA_INDEX + 2];
			Byte patch = dataBuffer[MtiCmd.RESP_DATA_INDEX + 3];
			System.out.println("Major = " + Byte.toString(major));
			System.out.println("Minor = " + Byte.toString(minor));
			System.out.println("Patch = " + Byte.toString(patch));
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacWriteOemData")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacReadOemData")) {
			Integer data = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Baud Rate = " + data.toString());
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacBypassWriteRegister")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacBypassReadRegister")) {
			Short value = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			System.out.println("Register Value = 0x" + Integer.toHexString(value & 0xFFFF));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacSetRegion")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacGetRegion")) {
			Byte region = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Integer support = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2);
			System.out.println("Region = " + Byte.toString(region));
			System.out.println("Support = 0x" + Integer.toHexString(support));
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacGetOEMCfgVersion")) {
			Byte major = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Byte minor = dataBuffer[MtiCmd.RESP_DATA_INDEX + 2];
			Byte patch = dataBuffer[MtiCmd.RESP_DATA_INDEX + 3];
			System.out.println("Major = " + Byte.toString(major));
			System.out.println("Minor = " + Byte.toString(minor));
			System.out.println("Patch = " + Byte.toString(patch));
			System.out.println("\n");

		} else if (response.name().contains("RFID_MacGetOEMCfgUpdateNumber")) {
			Byte update1 = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Byte update2 = dataBuffer[MtiCmd.RESP_DATA_INDEX + 2];
			System.out.println("Value 1 = " + Byte.toString(update1));
			System.out.println("Value 2 = " + Byte.toString(update2));
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioSetGpioPinsConfiguration")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioGetGpioPinsConfiguration")) {
			Byte value = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Config = " + Byte.toString(value));
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioWriteGpioPins")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_RadioReadGpioPins")) {
			Byte value = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Value = " + Byte.toString(value));
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestSetAntennaPortConfiguration")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestGetAntennaPortConfiguration")) {
			Byte port = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Short powerLevel = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2);
			Float dBm = powerLevel.floatValue() / 10;
			System.out.println("Antenna Port = " + port.toString());					
			System.out.println("RF Power Out = " + dBm.toString());					
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestSetFrequencyConfiguration")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestGetFrequencyConfiguration")) {
			Byte channelFlag = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			Integer frequency = MtiCmd.getInt(dataBuffer, MtiCmd.RESP_DATA_INDEX + 2);
			System.out.println("Channel Flag = " + channelFlag.toString());					
			System.out.println("RF Frequency = " + frequency.toString());					
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestSetRandomDataPulseTime")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestGetRandomDataPulseTime")) {
			Short onTime = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			Short offTime = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 3);
			System.out.println("Data Pulse On Time  = " + Integer.toString(onTime));					
			System.out.println("Data Pulse Off Time = " + Integer.toString(offTime));					
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestSetInventoryConfiguration")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestGetInventoryConfiguration")) {
			Byte configuration = dataBuffer[MtiCmd.RESP_DATA_INDEX + 1];
			System.out.println("Inventory Configuration = " + configuration.toString());					
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestTurnOnCarrierWave")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestTurnOffCarrierWave")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestInjectRandomData")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_TestTransmitRandomData")) {
			System.out.println("\n");

		} else if (response.name().contains("RFID_EngGetTemperature")) {
			Short temperature = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			if (testModeCommandSelect == 0) {
				System.out.println("PA Temperature = " + Integer.toString(temperature) + " C");
				bist.setRfModuleTemp(Integer.toString(temperature));
			} else {
				System.out.println("Ambient Temperature = " + Integer.toString(temperature) + " C");
				bist.setAmbientTemp(Integer.toString(temperature));
			}
			System.out.println("\n");

		} else if (response.name().contains("RFID_EngGetRFPower")) {
			Short powerLevel = MtiCmd.getShort(dataBuffer, MtiCmd.RESP_DATA_INDEX + 1);
			Float dBm = powerLevel.floatValue() / 10;
			if (testModeCommandSelect == 0) {
				System.out.println("Forward Power = " + dBm.toString() + " dBm");					
			} else {
				System.out.println("Reverse Power = " + dBm.toString() + " dBm");					
			}
			System.out.println("\n");

		} else {
			System.out.println("\n");
		}
	}

	/** 
	 * resetDevice<P>
	 * This method performs a reset of the HP-SiP and Serial interface.
	 */
	private void resetDevice() {
		try {
			// Close and reopen the serial port
			if ((serialComms != null) && serialComms.isConnected()) {
				serialComms.disconnect();
				serialComms = new SerialComms(serialRspQueue);
				serialComms.connect();
			}
			sendSoftReset();
		} catch (InterruptedException e) {
			log.error("Unable to send command to RFID module " + e.toString());
		} catch (Exception e) {
			log.error("Unable to re-open Comm Port " + e.toString());
		}
	}

	/** 
	 * cleanup<P>
	 * This method performs all things necessary before exiting the SmartAntenna application.
	 */
	private void cleanup() {
		// Close the serial port
		if ((serialComms != null) && serialComms.isConnected()) {
			serialComms.disconnect();
		}
		// Turn off the camera
		camera.enablePower(false);
		// Turn off the LED
		try {
			led.close();
		} catch (IOException e) {
			log.error("Unable to control LED\n" + e.toString());
		}
	}
}
