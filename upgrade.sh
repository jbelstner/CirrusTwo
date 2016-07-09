#!/bin/bash

apt-get remove apache2

rm /etc/rc*/S02apache2
rm /etc/rc*/K01apache2

rm /opt/encinitaslabs/rfid/*.log

rm /var/log/*.1
rm /var/log/*.2
rm /var/log/*.3
rm /var/log/*.4
rm /var/log/*.5
