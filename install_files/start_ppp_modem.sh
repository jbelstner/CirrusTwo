#!/bin/sh
#
# start_modem
#
# Starts the Verizon 4G cellular modem.
#
sleep 5
wvdial vzw4g
sleep 5
route del default
route add default ppp0

