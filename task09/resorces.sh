#!/bin/bash

syndicate generate lambda --name processor --runtime java

syndicate generate meta dynamodb --resource_name Weather \
  --hash_key_name id \
  --hash_key_type S \
  --read_capacity 1 \
  --write_capacity 1