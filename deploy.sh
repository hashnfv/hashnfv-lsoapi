#!/bin/bash   

echo ... stopping tomcat
/Library/Tomcat/bin/shutdown.sh

declare -a services=('cos' 'evc' 'svc')
for svc in "${services[@]}"
do
	for del in `ls -d /Library/Tomcat/webapps/${svc}*`; do
		echo ... removing $del
		rm -rf $del
	done
done 

echo ... building new war files
mvn clean install

echo ... moving war files to tomcat root
cp ./cos/cosmgr/target/cosmgr.war /Library/Tomcat/webapps/.
cp ./evc/evcmgr/target/evcmgr.war /Library/Tomcat/webapps/.
cp ./svc/svcmgr/target/svcmgr.war /Library/Tomcat/webapps/.
ls -l /Library/Tomcat/webapps/*.war

echo ... starting tomcat
/Library/Tomcat/bin/startup.sh
