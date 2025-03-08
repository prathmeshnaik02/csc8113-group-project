kubectl apply -f bookstore-ns.yml
./docker-creds.sh
kubectl apply -f configmap.yml
kubectl apply -f secrets.yml
kubectl apply -f postgres.yml
kubectl apply -f redis.yml
kubectl apply -f catalog-service.yml
kubectl apply -f cart-service.yml
