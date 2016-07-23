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

import java.io.IOException;

public class LedControl {
	
	private final int blinkTime_ms = 125;
	private final int slowMask = 7;
	private final int slowTime = 4;
	private final int mediumMask = 3;
	private final int mediumTime = 2;
	private Color currentColor = Color.Off;
	private Color nextColor = Color.Off;
	private BlinkState currentState = BlinkState.Constant;
	private BlinkState nextState = BlinkState.Constant;
	private boolean stateChange = false;
	private boolean beaconActive = false;
	private boolean errorPresent = false;
	private boolean ledOn = false;
	private int timerCount = 0;

	public enum Color {
		Off,
		Red,
		Green,
		Lime,
		Blue,
		Pink,
		Cyan,
		White
	}
	
	public enum BlinkState {
		Constant,
		SlowBlink,
		MediumBlink,
		FastBlink
	}
	
	/** 
	 * LedControl<P>
	 * Class Constructor
	 * @throws IOException 
	 */
	public LedControl( ) throws IOException {
		openControl();

		Thread blinkTimerWorker = new Thread () {
			public void run() {
				while ( true ) {
					try {
						Thread.sleep(blinkTime_ms);
					} catch (InterruptedException e) {
						// something bad happened
					}
					// Check for a change in color or state
					if ((nextColor.compareTo(currentColor) != 0) ||
						(nextState.compareTo(currentState) != 0)) {
						stateChange = true;
						currentState = nextState;
						currentColor = nextColor;
					}
					// Processing based on current state
					if (currentState.compareTo(BlinkState.Constant) == 0) { 
						if (stateChange) {
							try { setLed(currentColor); } catch (IOException e) { }
						}
					} else if (currentState.compareTo(BlinkState.SlowBlink) == 0) {
						if ((timerCount & slowMask) == slowTime) {
							if (ledOn == true) {
								ledOn = false;
								try { setLed(Color.Off); } catch (IOException e) { }
							} else {
								ledOn = true;
								try { setLed(currentColor); } catch (IOException e) { }
							}
						}
					} else if (currentState.compareTo(BlinkState.MediumBlink) == 0) {
						if ((timerCount & mediumMask) == mediumTime) {
							if (ledOn == true) {
								ledOn = false;
								try { setLed(Color.Off); } catch (IOException e) { }
							} else {
								ledOn = true;
								try { setLed(currentColor); } catch (IOException e) { }
							}
						}
					} else if (currentState.compareTo(BlinkState.FastBlink) == 0) {
						if (ledOn == true) {
							ledOn = false;
							try { setLed(Color.Off); } catch (IOException e) { }
						} else {
							ledOn = true;
							try { setLed(currentColor); } catch (IOException e) { }
						}
					}
					timerCount++;
					stateChange = false;
				}
			}
		};
		blinkTimerWorker.start();
	}
	
	/** 
	 * close<P>
	 * This method closes control of the LED on the Smart Antenna.
	 * @throws IOException 
	 */
	public void close( ) throws IOException {
		closeControl();
	}
	
	/** 
	 * openLedControl<P>
	 * This method initializes control of the LED on the Smart Antenna.
	 * @throws IOException 
	 */
	private void openControl() throws IOException {
		Runtime rt = Runtime.getRuntime();
//		rt.exec("./export_led_ctrl.sh");
		rt.exec("./led.sh 0");
	}
	
	/** 
	 * closeLedControl<P>
	 * This method closes control of the LED on the Smart Antenna.
	 * @throws IOException 
	 */
	private void closeControl() throws IOException {
		Runtime rt = Runtime.getRuntime();
		rt.exec("./led.sh 6");
//		rt.exec("./unexport_led_ctrl.sh");
	}
	
	/** 
	 * setLed<P>
	 * This method controls the LED of the Smart Antenna by making
	 * an appropriate system call.
	 * @param color The desired LED color.
	 * @throws IOException 
	 */
	private void setLed(Color color) throws IOException {
		if (color == Color.Off) {
			Runtime.getRuntime().exec("./led.sh 0");
		} else if (color == Color.Red) {
			Runtime.getRuntime().exec("./led.sh 1");
		} else if (color == Color.Green) {
			Runtime.getRuntime().exec("./led.sh 2");
		} else if (color == Color.Lime) {
			Runtime.getRuntime().exec("./led.sh 3");
		} else if (color == Color.Blue) {
			Runtime.getRuntime().exec("./led.sh 4");
		} else if (color == Color.Pink) {
			Runtime.getRuntime().exec("./led.sh 5");
		} else if (color == Color.Cyan) {
			Runtime.getRuntime().exec("./led.sh 6");
		} else if (color == Color.White) {
			Runtime.getRuntime().exec("./led.sh 7");
		}
	}	

	/** 
	 * set<P>
	 * This method specifies the state of the LED on the Smart Antenna.
	 * The color can only be changed by this method if the beacon is not
	 * active or no error condition exists.
	 * @param color The desired LED color.
	 * @param blinkState The desired Blink State of the LED.
	 */
	public void set(Color color_, BlinkState blinkState_) {
		if (!(beaconActive || errorPresent)) {
			nextColor = color_;
			nextState = blinkState_;
		}
	}	

	/** 
	 * beacon<P>
	 * This method invokes the beacon state of the LED on the Smart Antenna.
	 * @param params A String object containing the enable state.
	 */
	public void beacon(String params) {
		if (params != null) {
			if (params.equalsIgnoreCase("Enable")) {
				beaconActive = true;
				nextColor = Color.White;
				nextState = BlinkState.SlowBlink;
			} else {
				beaconActive = false;				
			}
		}
	}	

	/** 
	 * error<P>
	 * This method invokes the error state of the LED on the Smart Antenna.
	 * @param params A boolean indicating the error state.
	 */
	public void error(boolean present) {
		errorPresent = present;
		if (errorPresent) {
			nextColor = Color.Red;
			nextState = BlinkState.MediumBlink;
		}
	}	
}
