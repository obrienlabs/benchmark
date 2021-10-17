#!/bin/bash
cd /opt/app
#if [ -z "${java_runtime_arguments}" ]; then
#  java  -Xms128m -Xmx768m -jar /opt/app/lib/magellan-nbi.jar
#else
  java  -cp /opt/app/lib/org.obrienscience.collatz.server.ForkJoinCollatzServer.jar org.obrienscience.collatz.server.ForkJoinCollatzServer 5 6 1

#fi
