# GCP Setup

## 1. Create GKE Cluster

### Enable the Kubernetes Engine and Container Registry APIs

```sh
gcloud services enable \
  containerregistry.googleapis.com \
  container.googleapis.com
```

### Create cluster

```sh
gcloud container clusters create bookstore-cluster \
  --num-nodes=3 \
  --zone=europe-north2-a \
  --enable-autoscaling \
  --min-nodes=1 \
  --max-nodes=5 \
  --enable-ip-alias \
  --disk-size=50GB
```

**Note**: `--zone` can be excluded if a default zone has been set up.

GKE uses the gke-gcloud-auth-plugin to enable kubectl to authenticate via Google Cloud SDK (gcloud) when interacting with a GKE cluster. Without this plugin, kubectl won't be able to authenticate with the GKE cluster, and you won't be able to run commands like `kubectl get pods` or `kubectl get all`. RUN this to install the plugin:

```sh
gcloud components install gke-gcloud-auth-plugin
```

### Authenticate kubectl with GKE

```sh
gcloud container clusters get-credentials bookstore-cluster --zone europe-north2-a
```

## 2. Authenticate Docker with GCR (optional)

```sh
gcloud auth configure-docker
```

## 3. GitHub Actions Setup

### GCP Service Account

#### Create a service account:

```sh
gcloud iam service-accounts create github-actions \
 --description="GitHub Actions Service Account" \
 --display-name="GitHub Actions"
```

#### Add permissions to the service account

<!-- Allow deploying & managing Kubernetes workloads: -->

```sh
gcloud projects add-iam-policy-binding $PROJECT_ID \
  --member="serviceAccount:github-actions@$PROJECT_ID.iam.gserviceaccount.com" \
 --role="roles/container.admin"

gcloud projects add-iam-policy-binding $PROJECT_ID \
  --member="serviceAccount:github-actions@$PROJECT_ID.iam.gserviceaccount.com" \
 --role="roles/artifactregistry.writer"

gcloud projects add-iam-policy-binding $PROJECT_ID \
  --member="serviceAccount:github-actions@$PROJECT_ID.iam.gserviceaccount.com" \
  --role="roles/storage.admin"
```

Generate JSON key

```sh
gcloud iam service-accounts keys create gcp-sa-key.json \
 --iam-account=github-actions@$PROJECT_ID.iam.gserviceaccount.com
```

## 4. Extra

'Pause' GKE cluster:

```sh
kubectl scale deployment --all --replicas=0 -n bookstore
```
