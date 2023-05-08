# Send Nodes

![Build and Push to Docker Hub](https://github.com/mmoschenskikh/SendNodes/actions/workflows/docker-build-and-push.yml/badge.svg?branch=master)

A tiny blockchain implementation
for [Network Programming course](https://github.com/SemenMartynov/Software-Engineering-2022/blob/main/NetworkProgrammingTask.md).

## Getting started

### Running with Docker

Run the demo with three nodes:

```
docker-compose up
```

### Running without Docker

First, build the fat jar image with Gradle:

```
./gradlew fatJar
```

Then, run as many nodes as you need:

```
java -jar ./build/libs/SendNodes-1.0.jar 127.0.0.1:1337 127.0.0.1:1338,127.0.0.1:1339
java -jar ./build/libs/SendNodes-1.0.jar 127.0.0.1:1338 127.0.0.1:1339,127.0.0.1:1337
java -jar ./build/libs/SendNodes-1.0.jar 127.0.0.1:1339 127.0.0.1:1337,127.0.0.1:1338
...
```
