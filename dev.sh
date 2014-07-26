#!/bin/bash

case "${1}" in
u)
    set -x
    adb -d uninstall com.johnpritchard.docking
    ;;
i|d)
    set -x
    adb -d logcat -c
    adb -d install bin/Docking-debug.apk
    adb -d logcat | tee docking.log | grep Docking
    ;;
r)
    set -x
    adb -d logcat -c
    adb -d install bin/Docking-release.apk
    adb -d logcat | tee docking.log | grep Docking
    ;;
*)
    cat<<EOF>&2
usage
  $0 [u|i|r]
EOF
    exit 1
esac
