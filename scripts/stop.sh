#!/bin/bash
CONTAINER_NAME="perfscale"

if docker ps -q -f name=$CONTAINER_NAME | grep -q .; then
    echo "Stopping container: $CONTAINER_NAME"
    docker stop $CONTAINER_NAME
    docker rm $CONTAINER_NAME
    echo "Container stopped."
else
    echo "No running container named $CONTAINER_NAME. Skipping."
fi
