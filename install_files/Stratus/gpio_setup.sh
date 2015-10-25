#!/bin/bash

# Configure GPIO9 (gpio1) as Stratus Module 5V Enable
echo -n "1" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio1/direction
echo -n "1" > /sys/class/gpio/gpio1/value
#echo -n "1" > /sys/class/gpio/unexport

# Configure GPIO8 (gpio0) as Stratus HPSIP Enable
echo -n "0" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio0/direction
echo -n "1" > /sys/class/gpio/gpio0/value
#echo -n "0" > /sys/class/gpio/unexport

# Configure GPIO7 (gpio15) as Stratus HPSIP Wake
echo -n "15" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio15/direction
echo -n "1" > /sys/class/gpio/gpio15/value
#echo -n "15" > /sys/class/gpio/unexport

# Configure GPIO6 (gpio14) as Stratus LED Red
echo -n "14" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio14/direction
echo -n "1" > /sys/class/gpio/gpio14/value
#echo -n "14" > /sys/class/gpio/unexport

# Configure GPIO5 (gpio13) as Stratus LED Green
echo -n "13" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio13/direction
echo -n "0" > /sys/class/gpio/gpio13/value
#echo -n "13" > /sys/class/gpio/unexport

# Configure GPIO4 (gpio12) as Stratus LED Blue
echo -n "12" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio12/direction
echo -n "0" > /sys/class/gpio/gpio12/value
#echo -n "12" > /sys/class/gpio/unexport

# Configure GPIO3 (gpio11) as unused (output)
echo -n "11" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio11/direction
echo -n "0" > /sys/class/gpio/gpio11/value
echo -n "11" > /sys/class/gpio/unexport

# Configure GPIO2 (gpio10) as DDR3_REF_PGOOD input
echo -n "10" > /sys/class/gpio/export
echo -n "in" > /sys/class/gpio/gpio10/direction
#cat /sys/class/gpio/gpio10/value
#echo -n "10" > /sys/class/gpio/unexport

# Configure GPIO1 (gpio9) as 5V0_MODE output
echo -n "9" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio9/direction
echo -n "0" > /sys/class/gpio/gpio9/value
#echo -n "9" > /sys/class/gpio/unexport

# Configure GPIO0 (gpio8) as 5V0_PGOOD Input
echo -n "8" > /sys/class/gpio/export
echo -n "in" > /sys/class/gpio/gpio8/direction
#cat /sys/class/gpio/gpio8/value
#echo -n "8" > /sys/class/gpio/unexport

# Configure GPIO_SUS5 (gpio7) as Motion Sensor Input
echo -n "7" > /sys/class/gpio/export
echo -n "in" > /sys/class/gpio/gpio7/direction
#cat /sys/class/gpio/gpio7/value
#echo -n "7" > /sys/class/gpio/unexport

# Configure GPIO_SUS4 (gpio6) as ACCEL_INT Input
echo -n "6" > /sys/class/gpio/export
echo -n "in" > /sys/class/gpio/gpio6/direction
#cat /sys/class/gpio/gpio6/value
#echo -n "6" > /sys/class/gpio/unexport

# Configure GPIO_SUS3 (gpio5) as 8V5_EXT_PGOOD input
echo -n "5" > /sys/class/gpio/export
echo -n "in" > /sys/class/gpio/gpio5/direction
#cat /sys/class/gpio/gpio5/value
#echo -n "5" > /sys/class/gpio/unexport

# Configure GPIO_SUS2 (gpio4) as EN_8V5_EXT output
echo -n "4" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio4/direction
echo -n "0" > /sys/class/gpio/gpio3/value
#echo -n "4" > /sys/class/gpio/unexport

# Configure GPIO_SUS1 (gpio3) as SHUTTER output
echo -n "3" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio3/direction
echo -n "0" > /sys/class/gpio/gpio3/value
#echo -n "3" > /sys/class/gpio/unexport

# Configure GPIO_SUS0 (gpio2) as FOCUS output
echo -n "2" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio2/direction
echo -n "0" > /sys/class/gpio/gpio2/value
#echo -n "2" > /sys/class/gpio/unexport
