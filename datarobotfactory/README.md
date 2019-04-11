# DataRobotFactory

The DataRobotFactory solution is based on a collection of Node-RED flows deployed from running Docker containers.

# Node-RED

http://nodered.org

A visual tool for wiring the Internet of Things.

![Node-RED: A visual tool for wiring the Internet of Things](http://nodered.org/images/node-red-screenshot.png)

## Quick Start (not necessary to follow)

Check out http://nodered.org/docs/getting-started/ for full instructions on getting
started.

1. `sudo npm install -g --unsafe-perm node-red`
2. `node-red`
3. Open <http://localhost:1880>

# Docker

https://www.docker.com/

Package software into standardized units for development, shipment and deployment.

# Installation of DataRobotFactory

To get DataRobotFactory up and running you need to
   1. Install Docker.
   2. Load the tar file with an image of a Docker container with Node-RED and a test flow.
   3. Create a new Docker container based on this image.
   4. Configure the test flow, enable it and deploy.
   5. Create other containers with the delivered flows if you want to.

A prerequisite before actually running the flows is an installation of PostgreSQL and the HEPWAT database. The DataRobotFactory flows also contains a Kafka producer that expects you to have a host with a running Kafka installation available on port 9092.

## Quick Start

The following procedure explaining how to install the DataRobotFactory is based on an OS installation of ubuntu-16.04.1-server-amd64 but can easily be modified to be used with Windows.

1. Install Docker

        curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
        sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
        sudo apt-get update
        apt-cache policy docker-ce
        sudo apt-get install -y docker-ce=17.12.0~ce-0~ubuntu

2. Load tar file into Docker repository

        sudo docker load -i docker-image-hepwat-nodered.tar

3. Create a Docker container (e.g. deployed on port 2000)

        sudo docker run -dit --restart unless-stopped -p 2000:1880 --name hepwat-nodered-11 artogis/hepwat/node-red-11

4. Open <http://localhost:2000> and configure, enable and deploy the flow

        tbd
