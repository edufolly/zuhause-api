#!/bin/sh

ant clean
git pull
ant compile
ant jar
