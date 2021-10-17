#!/bin/bash
# http://wiki.obrienlabs.cloud/display/DEV/Experiment+1901%3A+Sending+Packets+around+the+Earth+-+magellan
# source from http://jira.obrienlabs.cloud/browse/MAGELLAN-1
# https://github.com/obrienlabs/magellan
# Michael O'Brien

BUILD_ID=10001
BUILD_DIR=builds
mkdir ../../$BUILD_DIR
TARGET_DIR=../../$BUILD_DIR/$BUILD_ID
mkdir $TARGET_DIR
CONTAINER_IMAGE=collatz-se
#TAG=intel
TAG=arm

cp ../../../*.jar $TARGET_DIR
cp DockerFile* $TARGET_DIR
cp startService.sh $TARGET_DIR
cd $TARGET_DIR
docker build --no-cache --build-arg build-id=$BUILD_ID -t obrienlabs/$CONTAINER_IMAGE -f DockerFile_$TAG .
#docker tag $CONTAINER_IMAGE:latest $CONTAINER_IMAGE:latest
docker tag obrienlabs/$CONTAINER_IMAGE obrienlabs/$CONTAINER_IMAGE:$TAG
# dockerhub
docker push obrienlabs/$CONTAINER_IMAGE:$TAG
# locally
docker stop $CONTAINER_IMAGE
docker rm $CONTAINER_IMAGE
echo "starting: $CONTAINER_IMAGE"
docker run --name $CONTAINER_IMAGE \
    -d -p 8888:8080 \
    -e os.environment.configuration.dir=/ \
    -e os.environment.ecosystem=sbx \
    obrienlabs/$CONTAINER_IMAGE:$TAG
cd ../../src/docker

