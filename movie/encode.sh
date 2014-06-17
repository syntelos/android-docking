#!/bin/bash

rm -f movie.mp4
input=$(cat index.txt | sed 's/^/ -i /')

avconv ${input} -c mpeg4 -r 1 -s 960x540 movie.mp4
