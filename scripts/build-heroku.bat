echo "Building mapbox-flow for deployment to Heroku"

cd ..
mvn clean install war:war -Pproduction

rem mvn clean install war:war -Pproduction -Pdirectory

