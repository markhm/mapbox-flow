@ECHO OFF

SET JAVA_HOME=c:\opt\jdk-13
SET ANT_HOME=c:\opt\ant
SET M2_HOME=c:\opt\maven
SET NODE_HOME=c:\opt\nodejs
SET OPT_BIN=c:\opt\bin
SET GIT_HOME=c:\opt\Git

SET PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%M2_HOME%\bin;%NODE_HOME%;c:\windows\system32;%OPT_BIN%;%GIT_HOME%\bin
REM SET PATH=%PATH%;%JAVA_HOME%\bin;%ANT_HOME%\bin;%M2_HOME%\bin;%NODE_HOME%;c:\windows\system32;%OPT_BIN%;%GIT_HOME%\bin

mvn clean install -Pdev

EXIT /b $?