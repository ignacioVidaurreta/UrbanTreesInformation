#!/bin/bash

cd ../client/target/UrbanTreesInformation-client-1.0-SNAPSHOT/ || (echo "Error!"; exit 1)

./run-client.sh "$@"