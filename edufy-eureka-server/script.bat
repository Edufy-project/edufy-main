@echo off
docker stop edufy-eureka-server-image
docker rm edufy-eureka-server-image
docker rmi edufy-eureka-server-image
call mvn package -DskipTests
docker build -t edufy-eureka-server-image .
docker run -d -p 8761:8761 --name edufy-eureka-server-image --network edufy-network edufy-eureka-server-image