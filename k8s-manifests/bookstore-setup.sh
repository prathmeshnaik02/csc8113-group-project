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
kubectl apply -f redis.yml

# deploy the catalog and cart services
kubectl apply -f catalog-service.yml
kubectl apply -f cart-service.yml

# deploy the bookstore web app
kubectl apply -f frontend.yml

# expose frontend and APIs outside cluster using Ingress
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
kubectl apply -f frontend-ingress.yml
kubectl apply -f catalog-ingress.yml
kubectl apply -f cart-ingress.yml
