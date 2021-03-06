#!/usr/bin/env bash

set -e

#typically Docker HAT container folder is in HAT2.0/deployment/docker/hat
HAT_HOME=${HAT_HOME:-"$PWD"} #if executing from deployment/  : "$PWD/../.."
DOCKER=${DOCKER:-"$PWD/deployment/docker/hat"}
DOCKER_DEPLOY=$DOCKER/docker-deploy

echo "Creating $DOCKER_DEPLOY"
mkdir $DOCKER_DEPLOY

echo "Building HAT : sbt docker:stage"
sbt docker:stage

if [ ! -f "$HAT_HOME/target/docker/Dockerfile" ]; then
    echo "Missing $HAT_HOME/target/docker/Dockerfile"
    echo "The docker-hat container was not created."
    echo "Please run 'sbt docker:stage' on main folder and re-run this script to generate it."
    exit
fi

cp -r $HAT_HOME/target/docker/stage/opt $DOCKER_DEPLOY/

cp $DOCKER/Dockerfile $DOCKER_DEPLOY/Dockerfile

echo "Building hat docker image: docker-hat"
docker build -t hubofallthings/hat-experimental $DOCKER_DEPLOY

echo "Cleaning up"
rm -r $DOCKER_DEPLOY
