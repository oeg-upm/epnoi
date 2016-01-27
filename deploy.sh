#!/bin/bash
rm -rf /Users/cbadenes/Projects/drinventor/tomcat/webapps/hoarder*
cp hoarder/target/hoarder.war /Users/cbadenes/Projects/drinventor/tomcat/webapps/
scp hoarder/target/hoarder.war cbadenes@zavijava.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm -rf /Users/cbadenes/Projects/drinventor/tomcat/webapps/harvester*
cp harvester/target/harvester.war /Users/cbadenes/Projects/drinventor/tomcat/webapps/
scp harvester/target/harvester.war cbadenes@zavijava.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm -rf /Users/cbadenes/Projects/drinventor/tomcat/webapps/modeler*
cp modeler/target/modeler.war /Users/cbadenes/Projects/drinventor/tomcat/webapps/
scp modeler/target/modeler.war cbadenes@zavijava.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/

rm -rf /Users/cbadenes/Projects/drinventor/tomcat/webapps/comparator*
cp comparator/target/comparator.war /Users/cbadenes/Projects/drinventor/tomcat/webapps/
scp comparator/target/comparator.war cbadenes@zavijava.dia.fi.upm.es:/opt/drinventor/tomcat-background/webapps/
