#!/bin/sh

nohup java -jar dist/Zuhause.jar > sout.log 2>&1&
echo $! > save_pid.txt
