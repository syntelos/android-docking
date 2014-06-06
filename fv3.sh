#!/bin/bash

export progn=$0
export wd=$(cd $(dirname $0); pwd)
export bin=/tmp/bin

export lib=${wd}/libs
export tgt=${lib}/fv3.jar
export src=${wd}/libs_fv3

function android_jar {
    root_dir=${ANDROID_HOME}
    if [ -n "${root_dir}" ]&&[ -d "${root_dir}" ]
    then
        plat_dir=${root_dir}/platforms
        if [ -d "${plat_dir}" ]
        then
            cc=0
            ver=4

            while [ ${cc} -lt 20 ]
            do
                ver_dir="${plat_dir}/android-${ver}"
                if [ -d "${ver_dir}" ]
                then
                    jarf="${ver_dir}/android.jar"
                    if [ -f "${jarf}" ]
                    then
                        echo "${jarf}"
                        return 0
                    else
                        cc=$(( ${cc} + 1 ))
                        ver=$(( ${ver} + 1 ))
                    fi
                else
                    cc=$(( ${cc} + 1 ))
                    ver=$(( ${ver} + 1 ))
                fi
            done

            cat<<EOF>&2
${progn}: error: platform version directory not found under ${plat_dir}.
EOF
            exit 1
        else
            cat<<EOF>&2
${progn}: error: directory not found: ${plat_dir}.
EOF
            exit 1
        fi
    else
        cat<<EOF>&2
${progn}: error: ANDROID_HOME not found.
EOF
        exit 1
    fi
}

export prog=$0
export name=''

function usage {
    cat<<EOF>&2
Synopsis

    $prog

Description

    Compile and package '${src}' into 'libs'.

EOF
    exit 1
}
function compile {

    if [ ! -d "${bin}" ]
    then
        if ! mkdir ${bin}
        then
            return 1
        fi
    else
        rm -rf $(find ${bin} -type f )
    fi

    if javac -cp ${classpath} -d ${bin} $(find ${src} -type f -name '*.java')
    then
        return 0
    else
        cat<<EOF>&2
${progn}: error: javac -cp ${classpath} -d ${bin} ${src}
EOF
        return 1
    fi
}
function package {

    if cd ${bin} && jar cf ${tgt} *
    then
        ls -l ${tgt}
        return 0
    else
        cat<<EOF>&2
${progn}: error: jar cf ${tgt}
EOF
        return 1
    fi
}


export android_jarf

if ! android_jarf=$(android_jar) || [ -z "${android_jarf}" ]
then
    cat<<EOF>&2
Missing 'android.jar'
EOF
    exit 1

elif [ ! -d "${src}" ]
then
    cat<<EOF>&2
Missing '${src}'
EOF
    exit 1

else
    export classpath=${android_jarf}

    if [ -n "${1}" ]
    then
        usage

    elif compile && package
    then
        exit 0
    else
        cat<<EOF>&2
${progn}: error: compile or package failed.
EOF
        exit 1
    fi
fi
