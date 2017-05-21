#!/bin/sh
set -xue;
find -path './src/main/res/*mipmap*/*.png' |
	while read file; do
		size="$(identify "$file" |awk '{print $3}')";
		convert store/icon-512.png -resize "${size}" "$file";
	done;
