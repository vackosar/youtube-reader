#!/bin/sh
set -xue;
cd src/main/res;
find -path '*mipmap*/*.png' ! -path '*xxx*' |
	while read file; do
		size="$(identify "$file" |awk '{print $3}')";
		convert mipmap-xxxhdpi/ic_launcher.png -resize "${size}" "$file";
	done;
