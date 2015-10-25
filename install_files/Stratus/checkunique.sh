#!/bin/sh -e

echo "Reading mac address chip"
MAC="$(/usr/sbin/read_mac_chip)"
OLD_IFS=$IFS    # save internal field separator
IFS=":"         # set it to ':'
set -- $MAC     # make the result positional parameters
IFS=$OLD_IFS    # restore IFS

# The hostname is SSP-xxyyzz, where
# xx yy zz are the last three bytes
# of the MAC address
#
OLD_HOSTNAME="$(cat /etc/hostname)"
NEW_HOSTNAME="SSP-$4$5$6"

if [ "$OLD_HOSTNAME" != "$NEW_HOSTNAME" ]; then

    echo "First time boot detected"
    echo "Changing eth0 hwaddress to $MAC"
    sed -i "s/.*hwaddress ether.*/    hwaddress ether $MAC/" /etc/network/interfaces
    echo "Changing hostname to $NEW_HOSTNAME"
    sed -i "s/$OLD_HOSTNAME/$NEW_HOSTNAME/" /etc/hostname
    reboot

fi

exit 0
