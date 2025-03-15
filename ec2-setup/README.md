# Kubernetes Setup on AWS EC2

This guide outlines the steps to set up a Kubernetes cluster on AWS EC2 instances using containerd as the container runtime.

## Prerequisites

- AWS account with permission to create EC2 instances and security groups
- SSH access to EC2 instances
- Basic knowledge of Linux commands

## AWS Resources Setup

### 1. Create Security Groups

#### Control Plane Security Group Inbound Traffic Rules

| Security Group        | Protocol | Port Range  | Source        | Description           |
| --------------------- | -------- | ----------- | ------------- | --------------------- |
| sgr-099c04b136dcc2991 | TCP      | 6783        | 172.31.0.0/16 | Weave Net CNI         |
| sgr-097dcad9ae12ab814 | TCP      | 2379-2380   | 172.31.0.0/16 | etcd communication    |
| sgr-0c9f601881922b1b8 | TCP      | 22          | 0.0.0.0/0     | SSH                   |
| sgr-0f055efd52c2025f2 | UDP      | 6783-6784   | 172.31.0.0/16 | Weave Net CNI UDP     |
| sgr-049a26f71bedaeda4 | TCP      | 6443        | 0.0.0.0/0     | Kubernetes API Server |
| sgr-0b50f657936e33147 | TCP      | 10250-10259 | 172.31.0.0/16 | Kubelet API           |

#### Worker Node Security Group Inbound Traffic Rules

| Security Group        | Protocol | Port Range  | Source        | Description       |
| --------------------- | -------- | ----------- | ------------- | ----------------- |
| sgr-0738404ab09204a97 | TCP      | 22          | 0.0.0.0/0     | SSH               |
| sgr-0eb11ced9c7579c94 | TCP      | 30000-32767 | 0.0.0.0/0     | NodePort traffic  |
| sgr-072131ca833d3bf32 | UDP      | 6783-6784   | 172.31.0.0/16 | Weave Net CNI UDP |
| sgr-095c6a0ed5b63a655 | TCP      | 10250       | 172.31.0.0/16 | Kubelet API       |
| sgr-062ec9a801f080343 | TCP      | 6783        | 172.31.0.0/16 | Weave Net CNI     |

**Note**: Replace 172.31.0.0/16 with your VPC's IPv4 CIDR.

### 2. Create EC2 Instances

- One instance for the control plane
- One or more instances for worker nodes
- Assign the appropriate security groups

## Docker and Kubernetes Setup

### 1. Prepare the System

Run the following command to disable swap:

```sh
sudo swapoff -a
```

### 2. Install Container Runtime (containerd)

Run the `containerd-install.sh` script on each instance:

```sh
chmod u+x containerd-install.sh
./containerd-install.sh
```

Verify installation:

```sh
service containerd status
```

### 3. Install Kubernetes Components

Run the `k8s-install.sh` script on each instance:

```sh
chmod u+x k8s-install.sh
./k8s-install.sh
```

Verify installation:

```sh
kubeadm version
service kubelet status  # Should be inactive until cluster is initialized
```

### 4. Initialize the Cluster (Control Plane)

On the control plane instance, run:

```sh
sudo kubeadm init --ignore-preflight-errors=Mem,NumCPU
```

**Note:** Since t2.micro instances have only 1 vCPU and 1 GiB RAM, we use the `--ignore-preflight-errors` flag. Ideally, you would have at least 2 vCPUs and 2 GiB RAM for the control plane.

### 5. Configure kubectl

Run the following commands on the control plane:

```sh
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

Check the status of the static pods:

```sh
kubectl get pod -A
```

In some cases, the static pods may experience failures. This is addressed below.

### 6. Fix Crash Loop Error (on all nodes)

Edit `/etc/containerd/config.toml` and set the `SystemCgroup` key to `true` under the `plugins` object:

```sh
sudo sed -i 's/SystemdCgroup = false/SystemdCgroup = true/' /etc/containerd/config.toml
sudo systemctl restart containerd
```

### 7. Deploy a Pod Network (Weave Net)

Ensure necessary ports are open in security groups as listed above.

Deploy Weave Net:

```sh
kubectl apply -f https://github.com/weaveworks/weave/releases/download/v2.8.1/weave-daemonset-k8s.yaml
```

Verify installation:

```sh
kubectl get pod -A
```

### 8. Join Worker Nodes to the Cluster

1. Apply the crash loop fix from step 6 on each worker node if you haven't already.
2. Run the `kubeadm join` command copied during cluster initialization.
   - If lost, generate it again on the control plane:
   ```sh
   kubeadm token create --print-join-command
   ```
3. Run the generated `kubeadm join` command on each worker node:
   ```sh
   kubeadm join <CONTROL_PLANE_IP>:6443 --token <TOKEN> --discovery-token-ca-cert-hash sha256:<HASH>
   ```

### 9. Verify Cluster Setup

On the control plane, run:

```sh
kubectl get nodes
kubectl get pod -A -o wide
```

## Credits

This guide is based on [this article by Mahesh Rajput](https://mrmaheshrajput.medium.com/deploy-kubernetes-cluster-on-aws-ec2-instances-f3eeca9e95f1).
