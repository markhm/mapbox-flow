#!/bin/sh

echo
echo "Building mapbox-flow and installing locally."
echo

cd ..
mvn clean install -Pdirectory
