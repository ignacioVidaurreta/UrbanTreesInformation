#!/bin/bash

cd ../server/target/UrbanTreesInformation-server-1.0-SNAPSHOT/ || (echo "Error!"; exit 1)

./run-server.sh "$@"