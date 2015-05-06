#!/bin/bash

if [ $1 -eq 0 ]; then

    echo -n "1" > /sys/class/gpio/export
    echo -n "out" > /sys/class/gpio/gpio1/direction
    echo -n "0" > /sys/class/gpio/gpio1/value
    echo -n "1" > /sys/class/gpio/unexport

else

    echo -n "1" > /sys/class/gpio/export
    echo -n "out" > /sys/class/gpio/gpio1/direction
    echo -n "1" > /sys/class/gpio/gpio1/value
    echo -n "1" > /sys/class/gpio/unexport

fi
