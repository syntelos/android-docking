#!/bin/bash

if [ -n "${1}" ]
then
    for src in /media/MB860/Android/data/com.johnpritchard.docking/files/Pictures/*${1}*
    do
        digits=$(basename $(echo $src | sed 's/.*-//') .png)
        t_digits=$(echo "${digits} + 500" | bc)
        tgt="movie/${t_digits}.png"
        cp -p $src $tgt
        ls -l $tgt
    done
else
    cat<<EOF>&2
usage: $0 name-filter
EOF
    exit 1
fi
