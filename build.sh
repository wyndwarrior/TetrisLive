#!/bin/sh

set -e

echo "Creating Build Directory"
rm -rf build
mkdir build

echo "Compiling"
javac -classpath lib/lwjgl.jar source/*.java -d build

cd build
echo "Creating Jar"
jar -cfm ../Tetris.jar ../source/manifest.txt wynd

echo "Done"