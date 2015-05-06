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

/**
 * MeshAgent
 * <P>Interfaces to the MeshCentral Agent running
 * on the target.  Based on an implementation by
 * Intel's Ylian Saint-Hilaire, 3/5/2014
 *  
 * @author Encinitas Labs
 * @version 0.1
 */
public class MyMeshClient {

	private Log log = null;

	/** 
	 * MeshAgent<P>
	 * Class Constructor
	 * @param log_ The log class for logging
	 */
	public MyMeshClient(Log log_) {
		log = log_;
    }


}
