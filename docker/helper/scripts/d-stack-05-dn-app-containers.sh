#!/bin/bash

set -e
set -u

pushd .
  SCRIPT_DIR=$(cd -P -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
  cd ${SCRIPT_DIR}/../..

  source ./0-conf.env

  declare -a APPS=(
    test-01
    staging-01
    controller
    em
    linker
    api
  )

  for APP in ${APPS[@]}; do
    SERVICE=${STACK_NAME}_${APP}
    NAME=`docker ps -f name=$SERVICE --format "{{.Names}}"`
    if [ -n "$NAME" ]; then
#     docker service scale $SERVICE=0
      docker wait $NAME
    fi
  done

  #docker service scale ${STACK_NAME}_test-01=0
  #docker service scale ${STACK_NAME}_staging-01=0
  #docker service scale ${STACK_NAME}_controller=0
  #docker service scale ${STACK_NAME}_em=0
  #docker service scale ${STACK_NAME}_linker=0
  #docker service scale ${STACK_NAME}_api=0

popd
