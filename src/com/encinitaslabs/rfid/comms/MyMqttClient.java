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

import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.encinitaslabs.rfid.Log;

public class MyMqttClient implements MqttCallback {

	// Configurable MQTT parameters
	private String broker_uri = null;
	private String client_id = null;
	private int mqttQos = 1;
	private int mqttKeepAliveInterval = 30;
	private int mqttConnectionTimeout = 1;
	private MqttAsyncClient client = null;
	private MqttConnectOptions conOpt = null;
	LinkedBlockingQueue<String> msgQueue = null;
	private Log logObject = null;
	
	/** 
	 * MqttClient<P>
	 * Class Constructor
	 * @param broker_ The URI of the MQTT broker
	 * @param client_ The ID used for this client
	 * @throws MqttException 
	 */
	public MyMqttClient(String broker_, String client_) throws MqttException {
		this.broker_uri = broker_;
		this.client_id = client_;
		// Construct a non blocking MQTT client
    	// (here is where the exception could come from)
		client = new MqttAsyncClient(this.broker_uri, client_id);
		// Set this class as the callback handler
    	client.setCallback(this);
    }

	/**
     * setLastWill<P>
     * Set the MQTT Last Will and Testament Connection Option
     * @throws MqttException
     */
    public void setLastWill(String topicName) throws MqttException {
    	if (client != null) {
    		// Set up last will and testament
    		String params = "{\"device_id\":\"" + client_id + "\",\"status\":\"lost\"}";
    		// Create the JSON RPC indication record
    		StringBuilder last_will = new StringBuilder("{\"jsonrpc\":\"2.0\",");
    		last_will.append("\"method\":\"status_update\",");
    		last_will.append("\"params\":" + params + "}");
        	conOpt = new MqttConnectOptions();
        	conOpt.setCleanSession(true);
        	conOpt.setKeepAliveInterval(mqttKeepAliveInterval);
        	conOpt.setConnectionTimeout(mqttConnectionTimeout);
        	conOpt.setWill(topicName, last_will.toString().getBytes(), mqttQos, true);
    	}
    }
	
	/**
     * connect<P>
     * Connects to the MQTT server
     * @throws MqttException
     */
    public void connect() throws MqttException {
    	if (client != null) {
        	// Connect to the MQTT server 
        	// issue a non-blocking connect and then use the token to wait until the
        	// connect completes. An exception is thrown if connect fails.
        	log("Connecting to " + broker_uri + " with client ID " + client.getClientId(), Log.Level.Information);
        	IMqttToken conToken = client.connect(conOpt, null, null);
        	conToken.waitForCompletion();
        	log("Connected", Log.Level.Information);
    		// Create a JSON RPC command
    		StringBuilder jsonRecord = new StringBuilder("{\"jsonrpc\":\"2.0\",");
			jsonRecord.append("\"method\":\"subscribe_to_topics\",");
    		jsonRecord.append("\"id\":\"0\"}");
        	// Queue the subscribe command
    		try {
    			msgQueue.put(jsonRecord.toString());
    		} catch (InterruptedException e) {
    			log("Unable to queue command\n" + e.toString(), Log.Level.Error);
    		}
    	}
    }
	
    /**
     * disconnect<P>
     * Disconnects from the MQTT server
     * @throws MqttException
     */
    public void disconnect() throws MqttException {
    	if (client != null) {
        	// Disconnect the client
        	// Issue the disconnect and then use a token to wait until 
        	// the disconnect completes.
        	log("Disconnecting", Log.Level.Information);
        	IMqttToken discToken = client.disconnect(null, null);
        	discToken.waitForCompletion();
        	log("Disconnected", Log.Level.Information);
    	}
    }
	
    /**
     * publish<P>
     * Send a message to an MQTT server
     * @param topicName the name of the topic to publish to
     * @param payload the set of bytes to send to the MQTT server 
     * @throws MqttException
     */
    public void publish(String topicName, String payload) throws MqttException {
    	
    	log("Publishing to topic \"" + topicName + "\" qos " + mqttQos, Log.Level.Information);
    	
    	// Construct the message to send
   		MqttMessage message = new MqttMessage(payload.getBytes());
    	message.setQos(mqttQos);
	
    	// Send the message to the server, control is returned as soon 
    	// as the MQTT client has accepted to deliver the message. 
    	// Use the delivery token to wait until the message has been
    	// delivered	
    	IMqttDeliveryToken pubToken = client.publish(topicName, message, null, null);
    	pubToken.waitForCompletion(); 	
    	log("Published", Log.Level.Information);       	
    }
    
    /**
     * subscribe<P>
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server 
     * that match the subscription. It continues listening for messages until the enter key is 
     * pressed.
     * @param topicName to subscribe to (can be wild carded)
     * @throws MqttException
     */
    public void subscribe(String topicName) throws MqttException {
    	
    	// Subscribe to the requested topic.
    	// Control is returned as soon client has accepted to deliver the subscription. 
    	// Use a token to wait until the subscription is in place.
    	log("Subscribing to topic \"" + topicName + "\" qos " + mqttQos, Log.Level.Information);
  
    	IMqttToken subToken = client.subscribe(topicName, mqttQos, null, null);
    	subToken.waitForCompletion();
    	log("Subscribed to topic \"" + topicName, Log.Level.Information);
    }
    
    /****************************************************************/
	/* Methods to implement the MqttCallback interface              */
	/****************************************************************/
    
    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
	public void connectionLost(Throwable cause) {
		// Called when the connection to the server has been lost.
		// An application may choose to implement reconnection
		// logic at this point. This sample simply exits.
		log("Connection to MQTT broker " + broker_uri + " lost!\n" + cause.toString(), Log.Level.Warning);
	}
	
    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
	public void deliveryComplete(IMqttDeliveryToken token) {
		// Called when a message has been delivered to the
		// server. The token passed in here is the same one
		// that was passed to or returned from the original call to publish.
		// This allows applications to perform asynchronous 
		// delivery without blocking until delivery completes.
		//
		// This sample demonstrates asynchronous deliver and 
		// uses the token.waitForCompletion() call in the main thread which
		// blocks until the delivery has completed. 
		// Additionally the deliveryComplete method will be called if 
		// the callback is set on the client
		// 
		// If the connection to the server breaks before delivery has completed
		// delivery of a message will complete after the client has re-connected.
		// The getPendinTokens method will provide tokens for any messages
		// that are still to be delivered.
		try {
			log("Delivery complete callback: Publish Completed " + token.getMessage(), Log.Level.Information);
		} catch (Exception e) {
			log("Exception in delivery complete callback\n" + e.toString(), Log.Level.Error);
		}
	}
	
    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
	public void messageArrived(String topic, MqttMessage message) throws MqttException {
		// Called when a message arrives from the server that matches any
		// subscription made by the client.
		// Parse the received JSON record
		
		// THIS PIECE OF HACK CODE CORRECTS FOR A BUG IN THE MQTT CLIENT WHERE
		// SOME OF THE QUOTATION MARKS (") ERRONIOUSLY END UP AS ???.
		byte[] oldPayload = message.toString().getBytes();
		String newPayload = new String(oldPayload).replace("???", "\"");
		log(newPayload, Log.Level.Debug);

		try {
			msgQueue.put(newPayload);
		} catch (InterruptedException e) {
			log("Unable to queue message\n" + e.toString(), Log.Level.Error);
		}
	}

	/** 
	 * isConnected<P>
	 * This method checks to see if the MQTT client is connected
	 * @return True if the MQTT client is connected
	 */
	public boolean isConnected( ) {
		return client.isConnected();
	}
	
	/** 
	 * setInMessageQueue<P>
	 * This method passes in the queue for storing incoming messages.
	 */
	public void setInMessageQueue(LinkedBlockingQueue<String> queue_) {
		msgQueue = queue_;
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
