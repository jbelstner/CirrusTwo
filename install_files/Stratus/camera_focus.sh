#!/bin/bash

if [ $1 -eq 1 ]; then

    #echo "Camera Focus ON"
    echo -n "1" > /sys/class/gpio/gpio2/value

else

    #echo "Camera Focus OFF"
    echo -n "0" > /sys/class/gpio/gpio2/value

fi

