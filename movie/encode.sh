#!/bin/bash
#
# Source frames named {0...N}.png
#

tgt=movie.h264

rm -f $tgt

set -x

avconv -i %d.png -c:v libx264 -preset veryslow -r 1 -s 960x540 -qp 0 ${tgt}
