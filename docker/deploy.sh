#!/bin/sh

docker-machine create \
  --driver generic \
  --generic-ip-address=192.168.8.222 \
  --generic-ssh-key ~/.ssh/id_rsa \
  bguwork
