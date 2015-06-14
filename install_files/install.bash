#!/bin/bash

echo "Encinitas Labs Cirrus-II Installer"
echo " "

if [ $# -gt 0 ]; then

    echo "Stopping the rfid process before install"
    if [ -f /etc/init.d/rfid.sh ]; then
        /etc/init.d/rfid.sh stop
    else
        ps -ef | grep "java" | awk '{print $2}' | xargs kill
    fi

    echo "Removing old files"
    rm -rf /opt/encinitaslabs

    echo "Expanding tarball"
    tar -xf install_*.tgz
    chmod 777 *.sh

    if [ $# -eq 4 ]; then
        echo "Updating the IP address to $2"
        echo "Updating the default gateway to $3"
        sed "s/440/$2/g" interfaces.static > interfaces.tmp
        sed "s/450/$3/g" interfaces.tmp > interfaces
    else
        mv interfaces.dhcp interfaces
    fi

    echo "Updating the Location to $1"
    sed "s/330/$1/g" application.conf > application.tmp1
    echo "Updating the Camera Name to $2"
    sed "s/331/$2/g" application.tmp1 > application.tmp2
    mv application.tmp2 application.conf
    rm application.tmp1

    echo "Updating the Linux hostname to $2"
    mv hosts hosts.tmp
    sed "s/220/$2/g" hosts.tmp > hosts
    rm hosts.tmp
    mv hostname hostname.tmp
    sed "s/220/$2/g" hostname.tmp > hostname
    rm hostname.tmp

    if [ ! -f /usr/lib/librxtxSerial.so ]; then
        echo "Copying serial library files"
        mv librxtxSerial.so /usr/lib/
        execstack -o /usr/lib/librxtxSerial.so
    else
        rm librxtxSerial.so
    fi

    if [ ! -d "/opt/encinitaslabs/rfid" ]; then
        echo "Making rfid directories"
        mkdir /opt/encinitaslabs
        mkdir /opt/encinitaslabs/rfid
    fi

    echo "Copying shell scripts"
    chmod +x *.sh
    mv *.sh /opt/encinitaslabs/rfid/

    echo "Copying config files"
    mv wpa_supplicant.conf /etc/wpa_supplicant.conf
    mv *.conf /opt/encinitaslabs/rfid/

    echo "Copying jar file"
    mv CirrusII.jar /opt/encinitaslabs/rfid/

    if [ ! -f /etc/init.d/rfid.sh ]; then
        echo "Making soft links"
        ln -s /opt/encinitaslabs/rfid/rfid.sh /etc/init.d/rfid.sh
    fi

    echo "Copying startup files"
    mv rc.local /etc/
    chmod +x /etc/rc.local
    mv hosts /etc/
    mv hostname /etc/
    mv interfaces /etc/network/

    echo "Cleaning up"
    rm interfaces.*
    rm *.tgz

    sync

    echo "Install complete"
    echo "Rebooting..."
    reboot

else

    echo "Usage:  (dhcp)"
    echo "./install.bash <location> <camera>"
    echo "or (static ip)"
    echo "./install.bash <location> <camera> <ip_address> <ip_gateway>"
    echo " "
    echo "Example:"
    echo './install.bash "Shallotte Adventure Mgmt Group" "Shallotte6"'
    echo "or"
    echo './install.bash "Myrtle Adventure Mgmt Group" "Myrtle4" 192.168.1.14 192.168.1.1'
    echo " "

fi

