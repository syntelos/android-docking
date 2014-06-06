#!/bin/bash

export progn=$0
export wd=$(cd $(dirname $0); pwd)
export bin=${wd}/bin
export tools=${wd}/tools

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
function fv3_jar {
    root_dir=${HOME}/src/syntelos/fv3
    if [ -n "${root_dir}" ]&&[ -d "${root_dir}" ]
    then
        
        if jarf=$(2>/dev/null ls ${root_dir}/fv3-*.jar | tail -n 1 ) &&[ -n "${jarf}" ]&&[ -f "${jarf}" ]
        then
            echo "${jarf}"
            return 0
        else
            cat<<EOF>&2
${progn}: error: file not found ${jarf}.
EOF
            exit 1
        fi
    else
        cat<<EOF>&2
${progn}: error: ${root_dir} not found.
EOF
        exit 1
    fi
}
function path_jar {
    root_dir=${HOME}/src/syntelos/path
    if [ -n "${root_dir}" ]&&[ -d "${root_dir}" ]
    then
        
        if jarf=$(2>/dev/null ls ${root_dir}/path-*.jar | tail -n 1 ) &&[ -n "${jarf}" ]&&[ -f "${jarf}" ]
        then
            echo "${jarf}"
            return 0
        else
            cat<<EOF>&2
${progn}: error: file not found ${jarf}.
EOF
            exit 1
        fi
    else
        cat<<EOF>&2
${progn}: error: ${root_dir} not found.
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

    Compile 'tools/<name>.java'.  The compilation class path includes
    the application 'bin/classes', and the 'android', 'fv3', and
    'path' packages.

Synopsis

    $prog r <name> [args]

Description

    Run the binary produced from 'tools/<name>.java', accepting
    optional arguments which are passed through to the main function.

    The runtime class path includes the application 'bin/classes', and
    the 'android', 'fv3', and 'path' packages.

EOF
    exit 1
}
function compile {

    javac -cp ${classpath} -d ${tools} ${tools}/${name}.java $*
}
function run {

    java -cp ${classpath}:${tools} ${name} $*
}

export bin_classes=${bin}/classes

export android_jarf
export fv3_jarf
export path_jarf

if ! android_jarf=$(android_jar) || [ -z "${android_jarf}" ]
then
    cat<<EOF>&2
Missing 'android.jar'
EOF
    exit 1
elif ! fv3_jarf=$(fv3_jar) || [ -z "${fv3_jarf}" ]
then
    cat<<EOF>&2
Missing 'fv3-jar'
EOF
    exit 1
elif ! path_jarf=$(path_jar) || [ -z "${path_jarf}" ]
then
    cat<<EOF>&2
Missing 'path-jar'
EOF
    exit 1
elif [ ! -d "${tools}" ]
then
    cat<<EOF>&2
Missing '${tools}'
EOF
    exit 1
elif [ ! -d "${bin_classes}" ]
then
    cat<<EOF>&2
Missing '${bin_classes}'
EOF
    exit 1
else
    export classpath=${bin_classes}:${android_jarf}:${path_jarf}:${fv3_jarf}

    if [ -n "${1}" ]
    then
        case "${1}" in
            c*)
                shift
                name="${1}"
                if [ -n "${name}" ]&&[ -f "${tools}/${name}.java" ]
                then
                    shift
                    compile $*
                else
                    usage
                fi;;
            r*)
                shift
                name="${1}"
                if [ -n "${name}" ]&&[ -f "${tools}/${name}.java" ]
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
