#!/bin/bash

aws dynamodb create-table \
  --table-name batch-demo-orders \
  --billing-mode=PAY_PER_REQUEST \
  --attribute-definitions AttributeName=OrderId,AttributeType=N \
  --key-schema AttributeName=OrderId,KeyType=HASH

