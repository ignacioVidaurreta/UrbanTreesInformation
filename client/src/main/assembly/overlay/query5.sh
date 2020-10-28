#!/bin/bash

java -Dquery=5 "$@" -cp 'lib/jars/*' "ar.edu.itba.pod.g3.client.Client"
