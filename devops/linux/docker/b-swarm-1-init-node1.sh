#!/bin/bash

set -e
set -u

source ./conf.env

docker swarm init --advertise-addr 127.0.0.1
# Get current node ID and set the label (for later use by the deployment contraints)
SELF_NODE_ID=$(docker node inspect self | grep -i -E -o '"ID": "([a-z0-9]+)"' | rev | cut -c 2- | rev | cut -c 8)
docker node update --label-add name=${NODE1} ${SELF_NODE_ID} 

echo

WORKER_TOKEN=$(docker swarm join-token worker --quiet)
MANAGER_TOKEN=$(docker swarm join-token manager --quiet)
  
echo "run this on the other nodes --worker"
echo "docker swarm join --token ${WORKER_TOKEN} 127.0.0.1:2377"
echo
echo "run this on the other nodes -- manager"
echo "docker swarm join --token ${MANAGER_TOKEN} 127.0.0.1:2377"


