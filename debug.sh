#!/bin/sh

cd /opt/projects/zuhause-api/

nohup java -jar dist/zuhause-api.jar --debug > /dev/null 2>&1&

echo $! > save_pid.txt
