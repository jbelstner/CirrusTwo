#!/bin/sh
SERVICE_NAME=rfid_smart
PID_NAME=/tmp/rfid_smart-pid
cd /opt/encinitaslabs/rfid
case $1 in
    start)
        echo "$SERVICE_NAME starting ..."
        if [ ! -f $PID_NAME ]; then
            java -jar /opt/encinitaslabs/rfid/CirrusII.jar false &
            echo $! > $PID_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_NAME ]; then
            PID=$(cat $PID_NAME);
            echo "$SERVICE_NAME stopping ..."
            kill -15 $PID;
            ps -ef | grep "java" | awk '{print $2}' | xargs kill;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_NAME ]; then
            PID=$(cat $PID_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill -15 $PID;
	    ps -ef | grep "java" | awk '{print $2}' | xargs kill;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_NAME
            echo "$SERVICE_NAME starting ..."
            java -jar /opt/encinitaslabs/rfid/CirrusII.jar false &
            echo $! > $PID_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac
