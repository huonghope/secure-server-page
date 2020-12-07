#!/bin/bash

PROJECT_PATH=$1 #ROOT path
USER_VALUE=$2 #User path
USER_PORT=$3 #User port

NODE_PORT=$4
PYTHON_PORT=$5
JAVA_PORT=$6

echo $1
echo $2
echo $3


mkdir -p "/$PROJECT_PATH/$USER_VALUE"


# Build and run
# docker build -t vscode
# PORT mappping:
# $NODE_PORT:3000 node.js
# $PYTHON_PORT:8080 python
# $JAVA_PORT:10000 java
docker run \
	--name $USER_VALUE \
	--detach \
	--restart unless-stopped \
	--user 0:0 \
	--publish $NODE_PORT:3000 \
	--publish $PYTHON_PORT:8080 \
	--publish $JAVA_PORT:10000 \
	--mount type=bind,source="$PWD/$PROJECT_PATH/$USER_VALUE",target="/home/coder/projects" \
	--publish $3:5111 \
vscode:latest