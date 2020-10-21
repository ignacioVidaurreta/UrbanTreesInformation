#!/bin/bash

java -Dquery=1 "$@" -cp 'lib/jars/*' "ar.edu.itba.pod.g3.client.Client"
