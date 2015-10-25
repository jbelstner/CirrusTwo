#!/bin/bash

if [ $1 -eq 1 ]; then

    #echo "Camera ON"
    echo -n "0" > /sys/class/gpio/gpio40/value

else

    #echo "OFF"
    echo -n "1" > /sys/class/gpio/gpio40/value

fi

