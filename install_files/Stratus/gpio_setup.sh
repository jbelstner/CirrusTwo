#!/bin/bash

# Configure gpio0 as Stratus HPSIP Enable

echo -n "0" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio0/direction
echo -n "1" > /sys/class/gpio/gpio0/value
echo -n "0" > /sys/class/gpio/unexport

# Configure gpio1 as Stratus Module 5V Enable

echo -n "1" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio1/direction
echo -n "1" > /sys/class/gpio/gpio1/value
echo -n "1" > /sys/class/gpio/unexport

# Configure gpio15 as Stratus HPSIP Wake

echo -n "15" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio15/direction
echo -n "1" > /sys/class/gpio/gpio15/value
echo -n "15" > /sys/class/gpio/unexport

# Configure gpio12 as Stratus LED Blue

echo -n "12" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio12/direction
echo -n "0" > /sys/class/gpio/gpio12/value
echo -n "12" > /sys/class/gpio/unexport

# Configure gpio13 as Stratus LED Green

echo -n "13" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio13/direction
echo -n "0" > /sys/class/gpio/gpio13/value
echo -n "13" > /sys/class/gpio/unexport

# Configure gpio14 as Stratus LED Red

echo -n "14" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio14/direction
echo -n "0" > /sys/class/gpio/gpio14/value
echo -n "14" > /sys/class/gpio/unexport

# Configure gpio7 as Stratus Motion Sensor Input

echo -n "7" > /sys/class/gpio/export
echo -n "in" > /sys/class/gpio/gpio7/direction
#cat /sys/class/gpio/gpio7/value
echo -n "7" > /sys/class/gpio/unexport

