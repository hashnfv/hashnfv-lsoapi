#!/bin/bash   

export LIBRARY=/home/ubuntu/apache-tomcat-8.0.24
#echo ... stopping tomcat
#$LIBRARY/bin/shutdown.sh

declare -a services=('cos' 'evc' 'svc')
for svc in "${services[@]}"
do
	for del in `ls -d $LIBRARY/webapps/${svc}*`; do
		echo ... removing $del
		rm -rf $del
	done
done 

echo ... building new war files
mvn clean install

echo ... moving war files to tomcat root
cp ./svc/svcmgr/target/svcmgr.war $LIBRARY/webapps/.
cp ./evc/evcmgr/target/evcmgr.war $LIBRARY/webapps/.
cp ./cos/cosmgr/target/cosmgr.war $LIBRARY/webapps/.
ls -l $LIBRARY/webapps/*.war

#echo ... starting tomcat
#$LIBRARY/bin/shutdown.sh
#$LIBRARY/bin/startup.sh
