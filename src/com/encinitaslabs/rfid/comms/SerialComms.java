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
package com.encinitaslabs.rfid.comms;

import com.encinitaslabs.rfid.Log;
import com.encinitaslabs.rfid.cmd.MtiCmd;
import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;
import java.util.concurrent.LinkedBlockingQueue;
//import javax.comm.*;

/**
 * SerialComms Object
 * 
 * <P>Attributes and functionality for reading and writing COM ports.
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class SerialComms implements SerialPortEventListener {

	public static final String TAG = "SerialComms";
    private static CommPortIdentifier portId;
	private boolean isOpen = false;
	private boolean DEBUG = false;
	private OutputStream out = null;
	private InputStream in = null;
	private SerialPort serialPort = null;
	private CommPort commPort = null;
	private LinkedBlockingQueue<byte[]> serialRspQueue = null;
	private Log logObject = null;

	private boolean lookForNewPacket = true;
	private int packetLength = 0;
	private final int circularBufferSize = 4096;
	private int headerIndex = circularBufferSize;
	private byte[] circularBuffer = new byte[circularBufferSize];
	private int inIndex = 0;
	private int outIndex = 0;

	
	
	/** 
	 * SerialComms
	 * 
	 * Class Constructor
	 */
    public SerialComms( LinkedBlockingQueue<byte[]> serialRspQueue, Boolean debug_ ) {
		this.serialRspQueue = serialRspQueue;
		this.DEBUG = debug_;
	}
	
	/** 
	 * connect<P>
	 * This method opens the specified serial port (if available).
	 * @param portName must be one of the available ports
	 * @return True is the serial port was successfully opened
     * @throws Exception
	 */
	public boolean connect( String portName, Integer baudRate ) throws Exception {
		boolean status = false;
		portId = CommPortIdentifier.getPortIdentifier( portName );
		if ( portId.isCurrentlyOwned() ) {
			System.out.println( "Error: Port is currently in use" );
		} else {
			int timeout = 2000;
			commPort = portId.open( "SerialComms", timeout );

			if ( commPort instanceof SerialPort ) {
				serialPort = ( SerialPort )commPort;
				serialPort.setSerialPortParams( baudRate.intValue(),
												SerialPort.DATABITS_8,
												SerialPort.STOPBITS_1,
												SerialPort.PARITY_NONE );
	 
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();

//		        ( new Thread( new SerialReader( in ) ) ).start();

				try {
				    serialPort.addEventListener(this);
				} catch (TooManyListenersException e) {
					System.out.println(e.toString());
				}
				serialPort.notifyOnDataAvailable(true);

				isOpen = true;
				status = true;

			} else {
				System.out.println( "Error: Invalid serial port name." );
			}
		}
		return status;
	}

	/** 
	 * isConnected<P>
	 * This method returns whether or not the serial port is open.
	 */
	public boolean isConnected() {
		return isOpen;
	}
	
	/** 
	 * disconnect<P> 
	 * This method closes the serial port opened by the connect method.
	 */
	public void disconnect() {
		if (isOpen) {
			try {
				in.close();
				out.close();
				isOpen = false;
				serialPort.close();
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}
	
	/** 
	 * SerialReader<P> 
	 * This class implements a non event driven serial port reader.
	 */
	public class SerialReader implements Runnable {

		InputStream in;
		
		public SerialReader( InputStream in ) {
			this.in = in;
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
		
		public void run() {
			byte[] readBuffer = new byte[1024];
			try {
				while (in.available() > 0) {
					int numBytes = in.read(readBuffer, 0, readBuffer.length);
					serialRead(readBuffer, numBytes);
				} 
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
	}

	/**
     * serialEvent<P>
	 * This method processes the serial port event.
     * @param event The JavaComm API event
     */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {

			case SerialPortEvent.BI:
				break;

			case SerialPortEvent.OE:
				log( "Serial Port Overrun!", Log.Level.Error );
				break;

			case SerialPortEvent.FE:
				log( "Serial Port Framing!", Log.Level.Error );
				break;

			case SerialPortEvent.PE:
				log( "Serial Port Parity!", Log.Level.Error );
				break;

			case SerialPortEvent.CD:
				break;

			case SerialPortEvent.CTS:
				break;

			case SerialPortEvent.DSR:
				break;

			case SerialPortEvent.RI:
				break;

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;

			case SerialPortEvent.DATA_AVAILABLE:
				byte[] readBuffer = new byte[1024];
				try {
					while (in.available() > 0) {
						int numBytes = 0;
						if (freeSpace() > readBuffer.length) {
							numBytes = readBuffer.length;
						} else {
							numBytes = freeSpace();							
						}
						// Only take as much as we can currently handle
						int bytesRead = in.read(readBuffer, 0, numBytes);
						if (bytesRead > 0) {
							try {
								// Process the serial data
								serialRead(readBuffer, bytesRead);
							} catch (ArrayIndexOutOfBoundsException e) {
								log( "bytesRead = " + bytesRead + "\ninIndex   = " + inIndex + "\noutIndex  = " + outIndex + "\n" + e.toString(), Log.Level.Error );
							}
						}
					} 
				} catch (Exception e) {
					log( "Serial Port Read failed!\n" + e.toString(), Log.Level.Error );
				}
				break;
				
			default:
				break;	
		}
	} 

    /** 
	 * serialWrite<P>
	 * This method writes an array of bytes out the serial port.
	 * @param message to be sent
	 * @param length in bytes of the message to be sent
	 * @return True is the write to the serial port was successful
	 */
	public boolean serialWrite (byte[] message, int length) {
		boolean status = false;
		if (isOpen) {
			try {
				out.write(message, 0, length);
				status = true;
				if (DEBUG) {
					System.out.println( "serialWrite: " + Integer.toString(length) + " bytes " + MtiCmd.byteArrayToString(message, true) );
//					System.out.println( "serialWrite: " + Integer.toString(length) );
				}
			} catch( IOException e ) {
				log( "Serial Port Write failed!\n" + e.toString(), Log.Level.Error );
			}
		}
		return status;
	}

	/** 
	 * serialRead<P>
	 * This method parses the array of bytes received off the serial port
	 * and assembles it into a complete packet.
	 * @param dataBuffer A copy of the received data buffer.
	 * @param length The length in bytes of valid data in the data buffer.
	 */
	public void serialRead( byte[] dataBuffer, int length ) {

		if (DEBUG) {
			System.out.println( "serialRead(): " + Integer.toString(length) + " bytes " + MtiCmd.byteArrayToString(dataBuffer, length, true) );
//			System.out.println( "serialRead(): " + Integer.toString(length) );
		}
		
		// Copy into the circular buffer for processing (guaranteed to have room)
		for (int i = 0; i < length; i++) {
			circularBuffer[inIndex] = dataBuffer[i];
			inIndex = (inIndex+1 < circularBufferSize) ? inIndex+1: 0;
		}
		boolean done = false;
		while (!done) {
			if (lookForNewPacket == true) {
				// See if we have enough bytes to look for a packet header
				if (cBufferLength() > MtiCmd.cmdHeader.length) {
					// Loop through the bytes looking for a packet header
					while (cBufferLength() - MtiCmd.cmdHeader.length > 0) {
						if (isHeader(outIndex)) {
							headerIndex = outIndex;
							break; // exit the while loop
						} else {
							// Increment outIndex and handle the loop around
							outIndex = (outIndex+1 < circularBufferSize) ? outIndex+1: 0;
						}
					}
					// See if we found a packet header
					if (headerIndex == outIndex) {
						// Check if we have the whole packet
						packetLength = MtiCmd.getCommandLength(circularBuffer[outIndex]);
						if (packetLength <= cBufferLength()) {
							copyAndSendPacket();
						} else {
							// We only have a piece of a packet and need more bytes
							lookForNewPacket = false;
							done = true;
						}
					} else {
						// Continue to look for a new packet in the buffer
						lookForNewPacket = true;
					}
				} else {
					// Need to gather more bytes
					lookForNewPacket = true;
					done = true;
				}
			} else {
				// See if we finally have the whole packet
				if (packetLength <= cBufferLength()) {
					copyAndSendPacket();
				} else {
					// We only have a few pieces of a packet
					lookForNewPacket = false;
					done = true;
				}
			}
		}
	}
	
	/** 
	 * cBufferLength<P>
	 * This helper method calculates the number of unprocessed
	 * bytes currently in the circular buffer.
	 * @return The number of bytes.
	 */
	private int cBufferLength() {
		if (inIndex >= outIndex) {
			return (inIndex - outIndex);
		} else {
			return (circularBufferSize - outIndex + inIndex);
		}
	}
	
	/** 
	 * freeSpace<P>
	 * This helper method calculates the free space available in
	 * the circular buffer.
	 * @return The amount of free space in bytes.
	 */
	private int freeSpace() {
		if (inIndex >= outIndex) {
			return circularBufferSize - (inIndex - outIndex);
		} else {
			return (outIndex - inIndex);
		}
	}
	
	/** 
	 * isHeader<P>
	 * This helper method checks for the presence of the MTI header at the
	 * specified index.
	 * @param index The index into the circular buffer to look for a header.
	 * @return Returns true is an MTI header is found.
	 */
	private boolean isHeader(int index) {
		int index1 = (index+1 < circularBufferSize) ? index+1: (index+1) - circularBufferSize;
		int index2 = (index+2 < circularBufferSize) ? index+2: (index+2) - circularBufferSize;
		int index3 = (index+3 < circularBufferSize) ? index+3: (index+3) - circularBufferSize;
		return ((circularBuffer[index1] == MtiCmd.cmdHeader[1]) &&
				(circularBuffer[index2] == MtiCmd.cmdHeader[2]) &&
				(circularBuffer[index3] == MtiCmd.cmdHeader[3]));
	}
	
	/** 
	 * copyAndSendPacket<P>
	 * This helper method copies a complete packet out of the circular
	 * buffer and sends it up for processing.
	 */
	private void copyAndSendPacket() {
		// copy the whole packet
		byte[] packet = new byte[MtiCmd.MAX_CMD_LENGTH];
		for (int i = 0; i < packetLength; i++) {
			packet[i] = circularBuffer[outIndex];
			// Increment outIndex and handle the loop around
			outIndex = (outIndex+1 < circularBufferSize) ? outIndex+1: 0;
		}
		try {
			// Now send it up
			serialRspQueue.put(packet);
		} catch (InterruptedException e) {
			log( "Queueing Packet failed!\n" + e.toString(), Log.Level.Error );
		}
		// Reset all values to look for a new packet
		lookForNewPacket = true;
		headerIndex = circularBufferSize;
		packetLength = 0;
	}
	
	/** 
	 * setLogObject<P>
	 * This method is used for making log entries.
	 */
	public void setLogObject(Log logObject_) {
		logObject = logObject_;
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