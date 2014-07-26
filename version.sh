#!/bin/bash
if version=$(ls -d version-* | egrep 'version-[0-9]+' | sort -u | tail -n 1 ) && [ -n "${version}" ]&&[ -d "${version}" ]
then
    echo "version ${version}"

    set -x
    cp -p bin/Docking-debug.apk ${version}
    cp -p bin/Docking-release.apk ${version}
    cp -p bin/proguard.txt  ${version}
    for src in  bin/proguard/*.txt
    do 
        tgt=${version}/proguard-$(basename $src )
        cp -p $src $tgt
    done
    git add ${version}
    ls -l ${version}
    exit 0
else
    cat<<EOF>&2
$0 error: version not found.
EOF
    exit 1
fi
