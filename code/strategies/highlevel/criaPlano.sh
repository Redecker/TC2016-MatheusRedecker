#!/bin/bash

var_path=$(dirname -- $(readlink -fn -- "$0"))

export CLASSPATH=$CLASSPATH:$var_path/lib/antlr.jar:$var_path/lib/JSHOP2.jar:.

java JSHOP2.InternalDomain -r problem

javac gui.java

java gui

rm *.class
