#!/bin/bash

set -o errexit
set -o nounset
set -o pipefail
set -o xtrace

./gradlew build
docker-compose build
docker-compose up