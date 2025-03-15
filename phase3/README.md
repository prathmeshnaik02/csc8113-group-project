# Velero Setup

Instructions for setting up Velero CLI: [Velero Documentation](https://velero.io/docs/v1.15/basic-install/)

## 1. Installation for GCP

```sh
velero install \
  --provider gcp \
  --plugins velero/velero-plugin-for-gcp:v1.11.0 \
  --bucket $BUCKET_NAME \
  --secret-file $SERVICE_ACCOUNT_JSON
```

## 2. Set up a backup schedule

```sh
velero schedule create postgres-daily \
  --schedule="0 3,15 * * *" \
  --selector="app=postgres" \
  --include-namespaces=bookstore
```

Optionally, manual backups can be done using the schedule created in the previous step:

```sh
velero backup create --from-schedule postgres-daily
```

## 3. Recovery with Velero

### Simulate Data Recovery

1. Scale down Postgres StatefulSet

   ```sh
   kubectl scale statefulset postgres -n bookstore --replicas=0
   ```

2. Delete PersistentVolumeClaim (PVC) and the PersistentVolume (PV) it is bound to.

   ```sh
   kubectl delete pvc -n bookstore postgres-storage-postgres-0
   kubectl delete pv -n bookstore pvc-dec09e37-e930-428c-89b8-a4a05c60822d  # note: the pv name is randomised
   ```

3. Initiate Velero Restoration and Wait for it to Complete

   ```sh
   velero restore create postgres-restore \
    --from-backup postgres-daily-20250315152804 \
    --namespace-mappings bookstore:bookstore \
    --existing-resource-policy=update \
    --wait
   ```

4. Ensure PVC and PV have been created and bound.

   ```sh
   kubectl get pvc,pv -n bookstore
   ```

5. Scale Up Postgres StatefulSet

   ```sh
   kubectl scale statefulset postgres -n bookstore --replicas=1
   ```
