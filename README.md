# BatMOB
# Batmud monster companion

## What does is do
Stores information about encountered NPCs and kills for later reference.
Also collects any detects spells.
Supports sarching mobs, querying wiki for additional information and reporting info to party.

This repository contains code to build the BatMob plugin for batclient. ( http://www.bat.org/play/batclient )

## How it works
The plugin detects mobs based on color codes. Thus, it may detect quite a few false positives and also will not detected any mods that have unusual color codes.
To mitigate the issue of false positives the plugin has an exclusion list hat can be updated. 

## How to build & run it locally #
```
mvn clean install

copy target/batMob-0.0.5-full.jar into batclient/plugins directory    

start batclient

```

## How to test the panels # 
The runnable TestClasses can be found under src/test/...
Simply run them as java apps

## How to contribute or give feedback #
Feel free to submit pull requests, there is lots of room for improvement. Preferably use the github issues etc.

You can try to contact me ingame in batmud too

++Armin
