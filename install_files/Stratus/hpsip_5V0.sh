#!/bin/bash

if [ $1 -eq 0 ]; then

    echo -n "0" > /sys/class/gpio/gpio1/value

else

    echo -n "1" > /sys/class/gpio/gpio1/value

fi
