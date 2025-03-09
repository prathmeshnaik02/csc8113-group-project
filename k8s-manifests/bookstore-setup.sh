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
