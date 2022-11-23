
# https://docs.docker.com/registry/
# https://registry.hub.docker.com/_/registry/tags
export REGISTRY_IMAGE=registry:2

# https://registry.hub.docker.com/r/bitnami/kafka/tags
export KAFKA_IMAGE=bitnami/kafka:3.3.1

# https://registry.hub.docker.com/r/dgraph/dgraph/tags
export DGRAPH_IMAGE=dgraph/dgraph:v22.0.1

# https://registry.hub.docker.com/r/dgraph/ratel/tags
export RATEL_IMAGE=dgraph/ratel:v21.03.2
    ports:
    - published: 50010
      target: 50000
      protocol: tcp
      mode: host  
