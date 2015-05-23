#!/bin/bash
# Configure UART0_RXD for Galileo Gen 2 DIGITAL_0
# Configure UART0_TXD for Galileo Gen 2 DIGITAL_1

echo -n "32" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio32/direction
echo -n "1" > /sys/class/gpio/gpio32/value
echo -n "32" > /sys/class/gpio/unexport

echo -n "33" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio33/direction
echo -n "1" > /sys/class/gpio/gpio33/value
echo -n "33" > /sys/class/gpio/unexport

echo -n "28" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio28/direction
echo -n "0" > /sys/class/gpio/gpio28/value
echo -n "28" > /sys/class/gpio/unexport

echo -n "29" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio29/direction
echo -n "hiz" > /sys/class/gpio/gpio29/drive
echo -n "29" > /sys/class/gpio/unexport

echo -n "45" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio45/direction
echo -n "1" > /sys/class/gpio/gpio45/value
echo -n "45" > /sys/class/gpio/unexport

# Configure gpio1 for Galileo Gen 2 DIGITAL_6 LED1 output

echo -n "20" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio20/direction
echo -n "0" > /sys/class/gpio/gpio20/value
echo -n "20" > /sys/class/gpio/unexport

echo -n "21" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio21/direction
echo -n "1" > /sys/class/gpio/gpio21/value
echo -n "21" > /sys/class/gpio/unexport

echo -n "68" > /sys/class/gpio/export
#echo -n "out" > /sys/class/gpio/gpio68/direction
echo -n "0" > /sys/class/gpio/gpio68/value
echo -n "68" > /sys/class/gpio/unexport

echo -n "1" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio1/direction
echo -n "0" > /sys/class/gpio/gpio1/value
echo -n "1" > /sys/class/gpio/unexport

# Configure gpio38 for Galileo Gen 2 DIGITAL_7 LED2 output

echo -n "39" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio39/direction
echo -n "1" > /sys/class/gpio/gpio39/value
echo -n "39" > /sys/class/gpio/unexport

echo -n "38" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio38/direction
echo -n "0" > /sys/class/gpio/gpio38/value
echo -n "38" > /sys/class/gpio/unexport

# Configure gpio40 for Galileo Gen 2 DIGITAL_8 Camera Enable

echo -n "41" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio41/direction
echo -n "1" > /sys/class/gpio/gpio41/value
echo -n "41" > /sys/class/gpio/unexport

echo -n "40" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio40/direction
echo -n "0" > /sys/class/gpio/gpio40/value
echo -n "40" > /sys/class/gpio/unexport

