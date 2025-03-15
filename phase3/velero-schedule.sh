velero schedule create postgres-daily \
  --schedule="0 3,15 * * *" \
  --selector="app=postgres" \
  --include-namespaces=bookstore
