#!/bin/bash

if [ $1 -eq 1 ]; then

    #echo "Camera Shutter ON"
    echo -n "1" > /sys/class/gpio/gpio3/value

else

    #echo "Camera Shutter OFF"
    echo -n "0" > /sys/class/gpio/gpio3/value

fi

