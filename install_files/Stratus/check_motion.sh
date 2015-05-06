#!/bin/bash

# Configure gpio7 as Motion Sensor Input

#echo -n "7" > /sys/class/gpio/export
#echo -n "in" > /sys/class/gpio/gpio7/direction
cat /sys/class/gpio/gpio7/value
#echo -n "7" > /sys/class/gpio/unexport

