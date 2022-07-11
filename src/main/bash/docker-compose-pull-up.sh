#!/bin/bash
set -euo pipefail
IFS=$'\n\t'
set -x

docker-compose -f docker-compose-non-dev.yml pull
docker-compose -f docker-compose-non-dev.yml up
