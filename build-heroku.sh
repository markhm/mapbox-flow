#!/bin/sh

echo
echo "Building mapbox-flow for deployment to Heroku"
echo

mvn clean install -Pproduction 

