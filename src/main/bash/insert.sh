#!/bin/bash
set -euo pipefail
IFS=$'\n\t'
set -x

DIR="$(dirname "$(realpath "$0")")"

export PGPASSWORD=accountant
psql -U accountant -d accounting -h localhost -f "${DIR}"/../../../target/insert.sql