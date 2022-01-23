#!/bin/sh

echo
echo "Building mapbox-flow for deployment to Heroku"
echo

cd ..

mvn clean install war:war -Pproduction
