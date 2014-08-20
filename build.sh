#!/bin/sh

set -e

echo "Creating Build Directory"
rm -rf build
mkdir build

echo "Compiling"
javac -classpath lib/lwjgl.jar src/*.java -d build

cd build
echo "Creating Jar"
jar -cfm ../Tetris.jar ../src/manifest.txt wynd

echo "Done"