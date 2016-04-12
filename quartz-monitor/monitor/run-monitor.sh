#! /bin/sh

if [ ! -n "$1" ] ;then
    echo $"Usage: $0 <war-file-path>"
else
    java -jar jetty-runner-9.1.4.v20140401.jar $1
fi