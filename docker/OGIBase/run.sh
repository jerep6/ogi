#!/bin/bash

# Create ogi structure into data directory. Volume is mounted at run
mkdir -p /data/ && chown -R ogi:ogi /data/
su - ogi -c "mkdir -p /data/tmp && mkdir -p /data/ogi/storage && mkdir -p /data/elasticsearch && mkdir -p /data/backup && mkdir -p /data/mysql"

# Run mysql
/etc/init.d/mysql start

# Run tomcat
#/app/tomcat/bin/startup.sh
su - ogi -c "/app/tomcat/bin/catalina.sh run"
