#!/bin/bash
/opt/drinventor/tomcat/bin/catalina.sh stop
/opt/drinventor/tomcat-background/bin/catalina.sh stop


rm -rf /opt/drinventor/tomcat/webapps/api*
mv api/target/api.war /opt/drinventor/tomcat/webapps/
#scp hoarder/target/hoarder.war cbadenes@drinventor.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/


rm -rf /opt/drinventor/tomcat-background/webapps/hoarder*
mv hoarder/target/hoarder.war /opt/drinventor/tomcat-background/webapps/
#scp hoarder/target/hoarder.war cbadenes@drinventor.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm -rf /opt/drinventor/tomcat-background/webapps/parser*
cp parser/target/parser.war /opt/drinventor/tomcat-background/webapps/parser1.war
cp parser/target/parser.war /opt/drinventor/tomcat-background/webapps/parser2.war
cp parser/target/parser.war /opt/drinventor/tomcat-background/webapps/parser3.war
cp parser/target/parser.war /opt/drinventor/tomcat-background/webapps/parser4.war
cp parser/target/parser.war /opt/drinventor/tomcat-background/webapps/parser5.war
#scp harvester/target/harvester.war cbadenes@drinventor.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm -rf /opt/drinventor/tomcat-background/webapps/harvester*
mv harvester/target/harvester.war /opt/drinventor/tomcat-background/webapps/
#scp harvester/target/harvester.war cbadenes@drinventor.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm -rf /opt/drinventor/tomcat-background/webapps/modeler*
mv modeler/target/modeler.war /opt/drinventor/tomcat-background/webapps/
#scp modeler/target/modeler.war cbadenes@drinventor.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm -rf /opt/drinventor/tomcat-background/webapps/comparator*
mv comparator/target/comparator.war /opt/drinventor/tomcat-background/webapps/
#scp comparator/target/comparator.war cbadenes@drinventor.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm /opt/drinventor/tomcat/logs/*.*
rm /opt/drinventor/tomcat-background/logs/*.*
