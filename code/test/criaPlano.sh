#!/bin/bash

export CLASSPATH=$CLASSPATH:/home/redecker/Documents/TC2016-MatheusRedecker/code/lib/antlr.jar:/home/redecker/Documents/TC2016-MatheusRedecker/code/lib/JSHOP2.jar:.

java JSHOP2.InternalDomain ahtnTeste

java JSHOP2.InternalDomain -r2 problem

javac gui.java

java gui

rm *.class
