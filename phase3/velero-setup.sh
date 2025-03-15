velero install \
  --provider gcp \
  --plugins velero/velero-plugin-for-gcp:v1.11.0 \
  --bucket csc8113-group8-velero-backups \
  --secret-file ../velero-sa-key.json
