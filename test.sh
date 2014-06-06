#!/bin/bash

export progn=$0
export wd=$(cd $(dirname $0); pwd)
export bin=${wd}/bin
export test=${wd}/test

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
                    jarf=${ver_dir}/android.jar
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

    $prog c <name>

Description

    Compile 'test/<name>.java'.  The compilation class path includes
    the application 'bin/classes' and the android runtime.

Synopsis

    $prog r <name> [args]

Description

    Run the binary produced from 'test/<name>.java', accepting
    optional arguments which are passed through to the main function.

    The runtime class path includes the application 'bin/classes' and
    the android runtime.

EOF
    exit 1
}
function compile {

    javac -cp ${classpath} -d ${test} ${test}/${name}.java $*
}
function run {

    java -cp ${classpath}:${test} ${name} $*
}

export bin_classes=${bin}/classes

export android_jarf

if ! android_jarf=$(android_jar) || [ -z "${android_jarf}" ]
then
    cat<<EOF>&2
Missing 'android.jar'
EOF
    exit 1
elif [ ! -d "${test}" ]
then
    cat<<EOF>&2
Missing '${test}'
EOF
    exit 1
elif [ ! -d "${bin_classes}" ]
then
    cat<<EOF>&2
Missing '${bin_classes}'
EOF
    exit 1
else
    export classpath=${bin_classes}:${android_jarf}

    if [ -n "${1}" ]
    then
        case "${1}" in
            c*)
                shift
                name="${1}"
                if [ -n "${name}" ]&&[ -f "${test}/${name}.java" ]
                then
                    shift
                    compile $*
                else
                    usage
                fi;;
            r*)
                shift
                name="${1}"
                if [ -n "${name}" ]&&[ -f "${test}/${name}.java" ]
                then
                    shift
                    run $*
                else
                    usage
                fi;;
            *)
                usage ;;
        esac
    else
        usage
    fi
fi
