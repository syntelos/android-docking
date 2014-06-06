#!/bin/bash

pkg=com.johnpritchard.docking
pkg_dir=src/$(echo ${pkg} | sed 's%\.%/%g;')

head_file=HEAD.txt

function usage {
    cat<<EOF>&2
Synopsis

    $0 <basename>

Description

    Generate basic <basename>.java into
        ${pkg_dir}

EOF
    exit 1
}
function init {
    head_check=0

    while [ ! -f ${head_file} ]&&[ 3 -gt ${head_check} ]
    do
        head_file=../${head_file}
        head_check=$(( ${head_check} + 1 ))
    done

    if [ ! -f ${head_file} ]
    then
        cat<<EOF>&2
$0 error: unable to locate file 'HEAD.txt'.
EOF
        exit 1
    fi
}

if init 
then

    classname=""
    file=""
#
# Process cmdln arguments
#
    for arg in $*
    do
        if [ -n "$(echo ${arg} | egrep '^[A-Za-z_0-9]+$')" ]
        then
            classname="${arg}"
            file=${pkg_dir}/${classname}.java
        else
            usage
        fi
    done

#
# Process input
#
    if [ -n "${file}" ]
    then
        cp ${head_file} ${file}
        if cat<<EOF >> ${file}
package ${pkg};

/**
 * 
 */
public class ${classname} {


    public ${classname}(){
        super();
    }

}
EOF
        then
            ls -l ${file}
            git add ${file}
        else
            cat<<EOF>&2
$0: error: failed to write file '${file}'.
EOF
            exit 1
        fi

    else
        usage
    fi
else
    exit 1
fi
