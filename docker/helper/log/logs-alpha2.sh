#!/bin/bash

set -e
set -u

source ../../0-conf.env
docker service logs --raw ${STACK_NAME}_jempi-alpha-02
echo