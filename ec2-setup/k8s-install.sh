:'
Credits:
- https://mrmaheshrajput.medium.com/deploy-kubernetes-cluster-on-aws-ec2-instances-f3eeca9e95f1
'

sudo apt-get update

# apt-transport-https may be a dummy package; if so, you can skip that package
sudo apt-get install -y apt-transport-https ca-certificates curl gpg


curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.29/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.29/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list

sudo apt-get update

echo "Installing latest versions"
sudo apt-get install -y kubelet kubeadm kubectl

echo "Fixate version to prevent upgrades"
sudo apt-mark hold kubelet kubeadm kubectl
