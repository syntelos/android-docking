#!/bin/bash

srcd=/media/MB860/Android/data/com.johnpritchard.docking/files/Pictures

if session=$(2>/dev/null ls ${srcd}/*.png | sed "s%.*Docking-%%g; s%-[0-9][0-9][0-9][0-9].png%%g;" | sort -u | head -n 1 ) && [ -n "${session}" ]
then
    echo session ${session}
    echo mkdir ${session}
    mkdir ${session}

    for srcf in ${srcd}/Docking-${session}*.png
    do
        digits=$(basename $(echo $srcf | sed 's/.*-//') .png)
        tgtf="${session}/${digits}.png"
        cp -p $srcf $tgtf
        ls -l $tgtf
        rm -f ${srcf}
        sleep 0.1
    done
    exit 0
else
    cat<<EOF>&2
$0: session not found
EOF
    exit 1
fi
