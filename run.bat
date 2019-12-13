@echo off

REM set code page to UTF-8 for better compatibility
@chcp.com 65001 > NUL

if not "%MAVEN_HOME%" == "" set MAVEN_HOME=%USERPROFILE:\=\\%\.m2\repository

set classpath="%cd:\=\\%\\target\\classes;C:\\Users\\%USERNAME%\\.m2\\repository\\mysql\\mysql-connector-java\\8.0.18\\mysql-connector-java-8.0.18.jar;C:\\Users\\%USERNAME%\\.m2\\repository\\com\\google\\protobuf\\protobuf-java\\3.6.1\\protobuf-java-3.6.1.jar"
java -Dfile.encoding=utf-8 -classpath %classpath% %1