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
