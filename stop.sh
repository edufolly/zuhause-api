#!/bin/sh

sudo kill -9 $(cat save_pid.txt)
rm -f save_pid.txt
