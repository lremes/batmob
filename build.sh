#!/usr/bin/env bash
# this file is run on mac with the http://www.bat.org/batclient/exe/batclient-mac-osx.pkg installed
# ps amarth if you see this, fix that default installation dir ,)
mvn clean install
cp target/batMob-0.0.7-full.jar /mnt/c/Users/larsr/batclient/plugins/
cp batMobCli.bcs /mnt/c/Users/larsr/batclient/scripts/
#javaws ~/Downloads/batclient.jnlp
