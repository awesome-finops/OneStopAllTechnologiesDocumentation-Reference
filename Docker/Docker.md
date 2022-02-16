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
* Dockerfile: This is a set of instruction which can be used to construct an image. These instructions are also known as directives.
  * **From** starts a new build stage and sets a new base image.This is usually the first directive in the dockerfile(except ARG can be placed before FROM).
  * **ENV** are environment variables.These can be referenced in the dockerfile and are visible to container at runtime.
  * **RUN** creates a new layer on top of the previous layer by running a command inside the new layer and committing the changes.
  * **CMD** specifies a default command used to run a container at execution time.
  * [Dockerfile Documentation](https://docs.docker.com/engine/reference/builder/)
  * ``` docker  build  -t  custom-nginx  .``` 
  * ```docker  run  --name  custom-nginx  -d  -p  8080:80  custom-nginx```
  * ```curl  localhost:8080``` 
  * **EXPOSE** documents which ports are intended to published the while running a container.
  *  **WORKDIR** sets the current working directory for subsequent directives such as ADD, COPY,  CMD, ENTRYPOINT.
  *  **COPY** copy files from local machine to the image.
  *  **ADD** similar to the COPY, but can also pull files using url and extract an archive into loose files in the image.
  *  [DockerFile Example](chrome-extension://efaidnbmnnnibpcajpcglclefindmkaj/viewer.html?pdfurl=https%3A%2F%2Facloudguru-content-attachment-production.s3-accelerate.amazonaws.com%2F1597956137344-02_03_More%2520Dockerfile%2520Directives.pdf&clen=71624&chunk=true)
*  [Building Efficient Docker Image](https://docs.docker.com/develop/develop-images/multistage-build/)
*  **EXPORT** ```docker export <container_name> ><<flat.tar>>``` exports the filesytem of a container into an archive which is a single layer.
*  **IMPORT** ``` cat flat.tar | docker import - flat:latest ``` This converts the multilayer image into an single layer image.
*  **Docker Registry** is responsible for storing and distributing docker images.
*  We have already pulled images from default public registry , Docker Hub.
*  Can also create our own registry using docker open source registry software or Docker trusted registry, the non free enterprise solution.
*  To create a registry, simply run a container using the registry image and publish port 5000.
  
  