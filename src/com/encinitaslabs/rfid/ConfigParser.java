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
import java.util.HashMap;


public class ConfigParser {
	public HashMap<String, String> config = new HashMap<String, String>();

	//Class Constructor
	public ConfigParser(String filename_ ){
		try {
			parseConfigFile(filename_);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	@SuppressWarnings("resource")
	private void parseConfigFile(String filename) throws IOException {
		BufferedReader br = null;
		String currentLine;
		br = new BufferedReader(new FileReader(filename));

		try {
			while ((currentLine = br.readLine()) != null) {
				String st[] = currentLine.split(" ");
				if (currentLine.startsWith("#")) {
					// Do nothing as this is a comment
				} else if (st.length >= 2){
					config.put(st[0], currentLine.substring(st[0].length() + 1));
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
