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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Log Object
 * <P>Attributes and functionality corresponding to logging.
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class Log {
	
	public enum Level {
		Error,
		Warning,
		Information,
		Debug
	}
	
	private String filename = null;
	private boolean logToFile = false;
	private boolean logToConsole = false;
	private Level logLevel = Level.Warning;

	/** 
	 * Log<P>
	 * Class Constructor
	 * @param filename_ The log file name to use
	 * @param logLevel_ The threshold level for making a log entry
	 */
	public Log(String filename_, Level logLevel_, boolean logToConsole_) {
		logLevel = logLevel_;
		logToConsole = logToConsole_;
		if ((filename_ != null) && (!filename_.startsWith("none"))) {
			logToFile = true;
			filename = filename_;
		}
		Date now = new Date();
		makeEntry("New Log @ " + now.toString(), logLevel);
	}
	
    /**
     * setLevel<P>
     * Utility method to handle logging.
	 * @param logLevel_ The threshold level for making a log entry
     */
    public void setLevel(Level logLevel_) {
		logLevel = logLevel_;
    }
	
	/**
     * makeEntry<P>
     * Utility method to handle logging.
     * @param message The message to log
     * @param level_ The log level of this particular information
     */
    public void makeEntry(String message, Level level_) {
    	if (level_.compareTo(this.logLevel) <= 0) {
    		if (logToConsole) {
        		System.out.println(level_.toString() + ": " + message);    			
    		}
        	if (logToFile) {
    		    FileWriter fstream;
    			try {
    				fstream = new FileWriter(filename, true);
    			    BufferedWriter out = new BufferedWriter(fstream);
    			    out.write(level_.toString() + ": " + message + "\n");
    			    out.close();
    			} catch (IOException e) {
    				System.out.println("Unable to open log file\n" + e.toString());
    			}
        	}
    	}
    }
}
