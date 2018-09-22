#GRPC Chat Server.

## Seeding the data 
The data to be seeded is in the init folder. It can used directly with redis-cli
```
./redis-cli < seed.txt
```

## Assumptions
On receive message JWT token is sent as part of request data.