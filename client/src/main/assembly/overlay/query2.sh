#!/bin/bash

java -Dquery=2 "$@" -cp 'lib/jars/*' "ar.edu.itba.pod.g3.client.Client"
