#!/bin/bash

NEW_VERSION="$1"

if [ "$NEW_VERSION" != "blue" ] && [ "$NEW_VERSION" != "green" ]; then
    echo "Invalid version. Please specify 'blue' or 'green'."
    exit 1
fi

kubectl patch service catalog-service -n bookstore \
    --type=json -p "[{\"op\": \"replace\", \"path\": \"/spec/selector/version\", \"value\": \"$NEW_VERSION\"}]"
