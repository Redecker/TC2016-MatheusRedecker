#!/bin/bash

export CLASSPATH=$CLASSPATH:/home/redecker/Documents/TC2016-MatheusRedecker/code/lib/antlr.jar:/home/redecker/Documents/TC2016-MatheusRedecker/code/lib/JSHOP2.jar:.

java JSHOP2.InternalDomain JSHOP/basic

java JSHOP2.InternalDomain -r JSHOP/problem

perl -ne 'BEGIN{print "package ai.abstraction;\n"} print' problem.java > problemNew.java

mv problemNew.java problem.java 

mv problem.java src/ai/abstraction
