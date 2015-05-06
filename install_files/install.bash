#!/bin/bash

echo "Encinitas Labs SmartAntenna Installer"
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

    if [ $# -eq 3 ]; then
        echo "Updating the IP address to $2"
        echo "Updating the default gateway to $3"
        sed "s/440/$2/g" interfaces.static > interfaces.tmp
        sed "s/450/$3/g" interfaces.tmp > interfaces
    else
        mv interfaces.dhcp interfaces
    fi

    echo "Updating the Reader ID to $1"
    mv application.conf application.tmp
    sed "s/220/$1/g" application.tmp > application.conf
    rm application.tmp

    echo "Updating the hostname to $1"
    mv hosts hosts.tmp
    sed "s/220/$1/g" hosts.tmp > hosts
    rm hosts.tmp
    mv hostname hostname.tmp
    sed "s/220/$1/g" hostname.tmp > hostname
    rm hostname.tmp

    if [ ! -d "/opt/encinitaslabs/rfid" ]; then
        echo "Making rfid directories"
        mkdir /opt/encinitaslabs
        mkdir /opt/encinitaslabs/rfid
    fi

    echo "Copying shell scripts"
    mv *.sh /opt/encinitaslabs/rfid/

    echo "Copying config files"
    mv *.conf /opt/encinitaslabs/rfid/

    echo "Copying jar file"
    mv SmartAntenna.jar /opt/encinitaslabs/rfid/

    if [ ! -f /usr/lib/librxtxSerial.so ]; then
        echo "Copying serial library"
        mv librxtxSerial.so /usr/lib/
    else
        rm librxtxSerial.so
    fi

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

    echo "Usage:"
    echo "./install.bash <reader_id>"
    echo "or"
    echo "./install.bash <reader_id> <ip_address> <ip_gateway>"
    echo " "

fi

