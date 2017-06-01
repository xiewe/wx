#!/bin/bash

cd `dirname $0`
d=`date +%Y%m%d`
d30=`date -d'30 day ago' +%Y%m%d`

cd /root/apache-tomcat-8.0.23/logs

cp catalina.out catalina.out.${d}
echo "" > catalina.out
rm -rf catalina.out.${d30}

