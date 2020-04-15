echo "Building mapbox-flow for deployment to Heroku"

mvn clean install war:war -Pproduction

rem mvn clean install war:war -Pproduction -Pdirectory

