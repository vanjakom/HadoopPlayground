#!/bin/sh

java -cp target/HadoopJobTrackerAnalyzer-1.0-jar-with-dependencies.jar:/etc/hadoop/conf/ com.mungolab.HadoopJobTrackerAnalyzer.MainClass $@