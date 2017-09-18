#!/usr/bin/env bash
mvn install:install-file -Dfile=svmLight-mac-8.4.jar -DgroupId=svm.light -DartifactId=svmLight-mac -Dversion=8.4 -Dpackaging=jar
mvn install:install-file -Dfile=svmLight-linux-8.4.jar -DgroupId=svm.light -DartifactId=svmLight-linux -Dversion=8.4 -Dpackaging=jar
mvn install:install-file -Dfile=svmLight-windows-8.4.jar -DgroupId=svm.light -DartifactId=svmLight-windows -Dversion=8.4 -Dpackaging=jar