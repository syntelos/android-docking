#!/bin/bash
set -x
cp -p bin/Docking-debug.apk version-1/
cp -p bin/Docking-release.apk version-1/
cp -p bin/proguard.txt  version-1/
for src in  bin/proguard/*.txt
do 
    tgt=version-1/proguard-$(basename $src )
    cp -p $src $tgt
done
ls -l version-1/
