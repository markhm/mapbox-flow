@ECHO OFF

SET JAVA_HOME=c:\opt\jdk-13
SET ANT_HOME=c:\opt\ant
SET M2_HOME=c:\opt\maven
SET NODE_HOME=c:\opt\nodejs
SET OPT_BIN=c:\opt\bin
SET GIT_HOME=c:\opt\Git

SET PATH=%PATH%;%JAVA_HOME%\bin;%ANT_HOME%\bin;%M2_HOME%\bin;%NODE_HOME%;c:\windows\system32;%OPT_BIN%;%GIT_HOME%\bin

ECHO Cleaning all build output
mvn clean

ECHO Removing node_modules
rd /s /q node_modules

ECHO Removing package-lock.json
del package-lock.json

EXIT /b $?
