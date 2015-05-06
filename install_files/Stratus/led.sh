#!/bin/bash

if [ $1 -eq 1 ]; then

    #echo "RED"
    echo -n "0" > /sys/class/gpio/gpio12/value
    echo -n "0" > /sys/class/gpio/gpio13/value
    echo -n "1" > /sys/class/gpio/gpio14/value

elif [ $1 -eq 2 ]; then

    #echo "GREEN"
    echo -n "0" > /sys/class/gpio/gpio12/value
    echo -n "1" > /sys/class/gpio/gpio13/value
    echo -n "0" > /sys/class/gpio/gpio14/value

elif [ $1 -eq 3 ]; then

    #echo "LIME"
    echo -n "0" > /sys/class/gpio/gpio12/value
    echo -n "1" > /sys/class/gpio/gpio13/value
    echo -n "1" > /sys/class/gpio/gpio14/value

elif [ $1 -eq 4 ]; then

    #echo "BLUE"
    echo -n "1" > /sys/class/gpio/gpio12/value
    echo -n "0" > /sys/class/gpio/gpio13/value
    echo -n "0" > /sys/class/gpio/gpio14/value

elif [ $1 -eq 5 ]; then

    #echo "PINK"
    echo -n "1" > /sys/class/gpio/gpio12/value
    echo -n "0" > /sys/class/gpio/gpio13/value
    echo -n "1" > /sys/class/gpio/gpio14/value

elif [ $1 -eq 6 ]; then

    #echo "CYAN"
    echo -n "1" > /sys/class/gpio/gpio12/value
    echo -n "1" > /sys/class/gpio/gpio13/value
    echo -n "0" > /sys/class/gpio/gpio14/value

elif [ $1 -eq 7 ]; then

    #echo "WHITE"
    echo -n "1" > /sys/class/gpio/gpio12/value
    echo -n "1" > /sys/class/gpio/gpio13/value
    echo -n "1" > /sys/class/gpio/gpio14/value

else

    #echo "OFF"
    echo -n "0" > /sys/class/gpio/gpio12/value
    echo -n "0" > /sys/class/gpio/gpio13/value
    echo -n "0" > /sys/class/gpio/gpio14/value

fi

