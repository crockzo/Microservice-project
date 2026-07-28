Local Set up guide.

Environment
- Java version : 25
- Spring boot version : 4.1.0
- gradle : groovy

First install the libraries 
- Go to observation project and run
  - ./gradlew clean build
  - ./gradlew publishToMavenLocal

- Go to Security project and run
  - ./gradlew clean build
  - ./gradlew publishToMavenLocal

Now Run the grafana docker compose file.
- docker compose up
NOTE : This docker image is a big image so it will take time to download and run.

Now run microservices
- eureka server
- user api
- book api
- library api

When running services like user, book and library.
- go to build.gradle and check if it is using com.ms:observability-starter
- it it is present, we need to add certain configs in the application.yaml file.
- Go to external libraries, find com.ms:observability-starter.
- it contains a copy-application.yaml
- just copy paste it in the above services.


When running services like user, book and library.
- go to build.gradle and check if it is using com.ms:security-starter
- it it is present, we need to add certain configs in the application.yaml file.
- Go to external libraries, find com.ms:security-starter.
- it contains a copy-application.yaml
- just copy paste it in the above services.
