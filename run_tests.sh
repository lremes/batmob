#!/bin/bash
jars=$(find target/lib -name "*.jar" | tr '\n' ':')
echo $jars
java -cp target/test-classes\:${jars}\
:target/classes\
:bat_interfaces.jar \
fi.altanar.batmob.gui.DetailsGuiTest
#fi.altanar.batmob.gui.SearchGuiTest

