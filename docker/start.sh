#!/bin/sh

docker build -t bsumonitor .

docker run -p 8181:8080 -d bsumonitor
