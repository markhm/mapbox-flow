#!/bin/sh

echo
echo "Deploying to mapbox-flow on Heroku"
echo

heroku war:deploy target/mapbox-flow-0.0.2-SNAPSHOT.war --app mapbox-flow

