#!/usr/bin/env bash

export IMAGE_NAME="$1"
export IMAGE_VERSION="$2"
docker-compose -f docker-compose.yaml up --detach
echo "success sh cmd"
export TEST=testvalue