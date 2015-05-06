#!/bin/bash

# Configure gpio1 for Galileo Gen 2 DIGITAL_6 LED1 output

echo -n "1" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio1/direction
echo -n "0" > /sys/class/gpio/gpio1/value
#echo -n "1" > /sys/class/gpio/unexport

# Configure gpio38 for Galileo Gen 2 DIGITAL_7 LED2 output

echo -n "38" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio38/direction
echo -n "0" > /sys/class/gpio/gpio38/value
#echo -n "38" > /sys/class/gpio/unexport

