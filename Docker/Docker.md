<div id='Back'/>

# Table of contents
1. [Docker Installation & Configuration](#DCE)
2. [Image Creation, management and registry](#IMR)
3. [Orchestration](#OR)
4. [Storage & Volume](#SV)
5. [Networking](#NW)
6. [Security](#SS)
7. [Docker Kubernetes Service](#DKS)


<div id='DCE'/> 

### Docker Installation and Configuration

[GoBack](#Back)

* [Docker Installation on Ubuntu](https://docs.docker.com/engine/install/ubuntu/)

* Add docker group to the local user using below command.
  Logout/In for the below command to work.
  ``` sudo usermod -a -G docker cloud_user ```
  
* Selecting a storage driver: Storage driver provides plugggable framework for managing the temporary internal storage of a container's writable layer.Docker supports a variety of storage drivers. The best storage driver to use depends on your environment and your storage needs.
  * overlay2: File-based storage. Default for ubuntu and cent os.
  * device mapper: Block storage for doing more efficient writes.
  * ``` docker info ``` is the command to see the driver details.
  * In order to explicitly mention the driver, please use the below command to edit the daemon.json file,
  * ```sudo vi /etc/docker/daemon.json```
  * add the key value pair,    ``` {"storage-driver": "devicemapper"} ```
  * ```sudo systemctl restart docker``` to take the effect

* Run container
    * [Documentation](https://docs.docker.com/engine/reference/run/)
    * ```docker run <imageid> <shell run command>```
    * docker run -d (detach mode); Run container in detached mode.   The docker run command will exit immediately and the container will run in the background.
    * --name; A container is assigned a name.
    * --restart; Specify when the container should be automatically restart.
      * no (default); Never going to be restarted.
      * on-failure; Only if it fails.
      * always;  Always restarts whether it succeeds or fails.
      * unless-stopped; Always restart the container whether it suceeds or fails when manually stopped.

      * -p; to publish port from the container to machine port on which it is hosted. <host>:<container>.
      * --rm: Automatically deletes the container the moment it is stopped.
    * Updating the docker engine.
        * Downgrading:
          * ``` sudo systemctl stop docker ```
          * ``` sudo apt-get remove -y docker-ce docker-ce-cli```
        * Upgrading:
          *  sudo apt-get update
          *  sudo apt-get install -y docker-ce=<<docker version>>```
     * Logging Drivers: logging drivers are pluggable framework for accessing log data from services and container from the docker.
       * Mention the logging drivers details in daemon.json
       * [logging documentation](https://docs.docker.com/config/containers/logging/configure/)
       * ``` docker info | grep Logging ```
     * [Docker Swarm](https://docs.docker.com/engine/swarm/key-concepts/)
       * Distributed cluster of docker machine to run the containers.
       * Add manager and workers,
         * ``` docker swarm init --advertise-addr 172.31.16.70 (pvt ip addr) ```
         * ``` docker node ls ``` # to list our the manager and workers in the swarm.
         * Swarm Token: ```docker swarm join-token worker```
         * Run the command on the workers in order to add them in the swarm.

    * [Namespaces](https://docs.docker.com/engine/security/userns-remap/) 

<div id='IMR'/> 

### Image Creation, management and registry

[GoBack](#Back)

* Docker Image is a file containing the code and components needed to run the s/w in a container.
* Containers and images use a layered file system. Each layer contains only the differences from the previous layer.
* The image contains one or more read only layers whereas the container adds the writable layer.
