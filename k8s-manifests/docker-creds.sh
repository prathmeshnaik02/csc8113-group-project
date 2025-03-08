#!/bin/bash

# Check if .env file exists, and if so, load it
if [ -f .env ]; then
  echo ".env file found, loading environment variables..."
  export $(cat .env | xargs)
else
  echo ".env file not found, using GitHub secrets..."
  
  # Fall back to GitHub Secrets if not running locally
  # For GitHub Actions, these environment variables will be automatically passed
  DOCKER_USERNAME=${DOCKER_USERNAME:-$GITHUB_DOCKER_USERNAME}
  DOCKER_PASSWORD=${DOCKER_PASSWORD:-$GITHUB_DOCKER_PASSWORD}
fi

# Ensure that all necessary variables are available
if [ -z "$DOCKER_USERNAME" ] || [ -z "$DOCKER_PASSWORD" ]; then
  echo "Error: Missing required environment variables (DOCKER_USERNAME, DOCKER_PASSWORD)"
  exit 1
fi

kubectl create secret docker-registry regcred \
  --docker-server=https://index.docker.io/v1/ \
  --docker-username=$DOCKER_USERNAME \
  --docker-password=$DOCKER_PASSWORD \
  --namespace=bookstore
