#!/bin/bash

case "${1}" in
u)
    set -x
    adb -d uninstall com.johnpritchard.docking
    ;;
i)
    set -x
    adb -d logcat -c
    adb -d install bin/Docking-debug.apk
    adb -d logcat | grep Docking
    ;;
*)
    cat<<EOF>&2
usage
  $0 [u|i]
EOF
    exit 1
esac
