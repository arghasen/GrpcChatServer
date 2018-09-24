#!/bin/bash
nohup redis-server --protected-mode no &
./GrpcChat/bin/GRPCServer redis://localhost:6379/0 

