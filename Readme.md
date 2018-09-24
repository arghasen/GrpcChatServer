#GRPC Chat Server.

## Seeding the data 
The data to be seeded is in the init folder. It can used directly with redis-cli
```
./redis-cli < seed.txt
```

## Assumptions/Decisons


1. On receive message JWT token is sent as part of request data.
2. No checking is done in whether the hostname/port is correct.
3. JWT tokens are not encrypted at the moment.

# Instructions to build

Use the following commands
```
./gradlew 
./gradlew build
```

# Instructions to run
Unpack the tar from build/distributions
run the GrpcServer and GrpcClient from the bin folder of the tarball.
```
./GRPCServer <redis-url>
./GRPCClient 
```
Client needs to be run on a machine with GUI.

# Docker
The dockerfile is present in **docker** folder.
To run that need to build and copy the java distribution folder to the docker folder.
Post that run the following from docker folder.
```
docker build -t grpcchatdocker .
docker run -p 6379:6379 -p 50051:50051 grpcchatdocker:latest
```
The docker image can be seeded with data in the same way as described above.

# FAQ
1. If you are getting login failed repeatedly, the database might be unseeded.
2. The server listens to port 50051 so that needs to be open.