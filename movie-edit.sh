#!/bin/bash

export fok=1
export fer=0

export session
export session_begin
export session_end

export src
export tgt
export count

function ftrim {
    a=$(echo "${1}" | sed 's%.*/%%; s/^0//g;')
    while [ -n "$(echo ${a} | egrep '^0')" ]
    do
        a=$(echo "${a}" | sed 's/^0//g')
    done
    echo ${a}
}
function fcopy {
    printf -v fcopy_src "%s/%04d.png" ${session} ${src}

    number=${tgt}
    end=$(( ${tgt} + ${count} ))

    while [ ${number} -lt ${end} ]
    do
        printf -v fcopy_tgt "%s/%04d.png" ${session} ${number}

        if echo cp ${fcopy_src} ${fcopy_tgt}
        then
            number=$(( ${number} + 1 ))
        else
            cat<<EOF>&2
$0 error in 'cp ${fcopy_src} ${fcopy_tgt}'
EOF
            return 1
        fi
    done
    return 0
}
function fmove {

    for file in $(ls ${session}/*.png | sort -r )
    do
        number_old=$(ftrim $(basename ${file} .png) )

        if [ ${tgt} -le ${number_old} ]
        then
            number_new=$(( ${number_old} + ${count} ))

            printf -v fmove_src "%s/%04d.png" ${session} ${number_old}
            printf -v fmove_tgt "%s/%04d.png" ${session} ${number_new}

            if echo mv ${fmove_src} ${fmove_tgt}
            then
                continue
            else
                cat<<EOF>&2
$0 error in 'mv ${fmove_src} ${fmove_tgt}'
EOF
                return 1
            fi
        else
            return 0
        fi
    done
    return 0
}
function finit {

    session_begin=$(ftrim $(basename $(2>/dev/null ls ${session}/*.png | head -n 1 ) .png ) )

    session_end=$(ftrim $(basename $(2>/dev/null ls ${session}/*.png | tail -n 1 ) .png ) )

    printf "session %s (begin: %04d, end: %04d)\n" ${session} ${session_begin} ${session_end}

    return 0
}
function finsert {
    if [ $src -ge $session_begin ]&&[ $src -le $session_end ]&&
       [ $tgt -ge $session_begin ]&&[ $tgt -le $session_end -o $tgt -eq $(( $session_end + 1 )) ]&&
       [ $src -lt $tgt ]
    then
        echo "${command} (session: ${session}, src: ${src}, tgt: ${tgt}, count: ${count})"

        if fmove && fcopy
        then
            return ${fok}
        else
            return ${fer}
        fi
    else
        cat<<EOF>&2
$0 error: source (${src}) and target (${tgt}) violate list constraints.
EOF
        exit 1
    fi
}

session="${1}"
shift

if [ -n "${session}" ]&&[ -d "${session}" ]&& finit
then
    while [ -n "${1}" ]
    do
        command="${1}"
        shift

        case "${command}" in
            copy)
                printf -v src "%d" $(ftrim "${1}" )
                shift
                printf -v count "%d" $(ftrim "${1}" )
                shift

                printf -v tgt "%d" $(( ${src} + 1 ))

                if finsert
                then
                    cat<<EOF>&2
$0: Error from command "${command}".
EOF
                    exit 1
                fi
                ;;
            insert)
                printf -v src "%d" $(ftrim "${1}" )
                shift
                printf -v tgt "%d" $(ftrim "${1}" )
                shift
                printf -v count "%d" $(ftrim "${1}" )
                shift

                if finsert
                then
                    cat<<EOF>&2
$0: Error from command "${command}".
EOF
                    exit 1
                fi
                ;;
            *)
                cat<<EOF>&2
$0: Error unrecognized command "${command}".
EOF
                exit 1
                ;;
        esac
    done
else
    cat<<EOF>&2
Synopsis

  $0 <session> copy <frame> <count>

Description

  In directory <session>, copy frame number <frame> for <count> times
  in sequence.

Synopsis

  $0 <session> insert <src> <tgt> <count>

Description

  In directory <session>, copy frame number <src> into sequence before
  frame number <tgt> for <count> times in sequence.

EOF
    exit 1
fi
