#!/bin/bash

java -Dquery=4 "$@" -cp 'lib/jars/*' "ar.edu.itba.pod.g3.client.Client"
