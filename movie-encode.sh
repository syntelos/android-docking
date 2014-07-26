#!/bin/bash

export session="${1}"
shift
export session_begin
export session_end


function ftrim {
    a=$(echo "${1}" | sed 's%.*/%%; s/^0//g;')
    while [ -n "$(echo ${a} | egrep '^0')" ]
    do
        a=$(echo "${a}" | sed 's/^0//g')
    done
    echo ${a}
}
function finit {

    session=$(echo "${session}" | sed 's%^\./%%; s%/%%g')

    if [ -n "${session}" ]&&[ -d "${session}" ]
    then

        session_begin=$(ftrim $(basename $(2>/dev/null ls ${session}/*.png | head -n 1 ) .png ) )

        session_end=$(ftrim $(basename $(2>/dev/null ls ${session}/*.png | tail -n 1 ) .png ) )

        printf "session %s (begin: %04d, end: %04d)\n" ${session} ${session_begin} ${session_end}

        return 0
    else
        return 1
    fi
}


if finit
then
    tgtf_yuv=${session}.yuv
    tgtf_wbm=${session}.webm

    if png2yuv -I p -f 2 -b 1 -n 359 -j ${session}/%04d.png > ${session}/${tgtf_yuv} &&
       vpxenc ${session}/${tgtf_yuv} -o ${session}/${tgtf_wbm} -w 960 -h 540 -p 2 --best --fps=2/1 -v
    then
        ls -l ${session}/${tgtf_yuv} ${session}/${tgtf_wbm}
        exit 0
    else
        exit 1
    fi
else
    cat<<EOF>&2
Usage
    $0 <session>

Description

    Generate video output
        <session>/<session>.yuv,
    and 
        <session>/<session>.webm
    from 
        <session>/[0-9][0-9][0-9][0-9].png

EOF
    exit 1
fi

