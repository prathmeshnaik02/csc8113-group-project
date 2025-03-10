Ensure docker-creds.sh and bookstore-setup.sh are executable files (chmod on Linux). Also, store the DOCKER_USERNAME and DOCKER_PASSWORD variables in a .env file in this folder.

Run ./bookstore-setup.sh

While testing locally, make sure you add `127.0.0.1 bookstore.local` to `/etc/hosts` (on Linux/macOS).
