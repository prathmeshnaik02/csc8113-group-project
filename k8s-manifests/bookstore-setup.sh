#!/bin/bash

# function to apply Kubernetes manifests with retries
apply_with_retry() {
    local file=$1
    local retries=5
    local delay=5

    for i in $(seq 1 $retries); do
        echo "Attempt $i: Applying $file..."
        kubectl apply -f "$file" && return 0
        echo "Failed to apply $file. Retrying in $delay seconds..."
        sleep $delay
    done

    echo "Failed to apply $file after $retries attempts. Exiting..."
    exit 1
}

# create namespace for the bookstore app
kubectl apply -f bookstore-ns.yml

# add docker credentials
./docker-creds.sh

# config maps and secrets
kubectl apply -f configmap.yml
kubectl apply -f secrets.yml

# deploy postgres and redis databases
kubectl create configmap postgres-init-script \
    --from-file=./postgres/init.sql -n bookstore
kubectl apply -f postgres.yml

# deploy the catalog and cart services
kubectl apply -f catalog-service.yml
kubectl apply -f cart-service.yml

# deploy HPA for catalog service
kubectl apply -f hpa-catalogservice.yaml

# deploy the bookstore web app
kubectl apply -f frontend.yml

# expose frontend and APIs outside cluster using Ingress
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml

sleep 30
apply_with_retry frontend-ingress.yml
apply_with_retry catalog-ingress.yml
apply_with_retry cart-ingress.yml
