FROM openjdk:8-jdk-alpine

COPY target/parkhaus-manager-micronaut-1.0-SNAPSHOT.jar /deployments/app.jar

EXPOSE 8280

ENTRYPOINT ["java","-jar","/deployments/app.jar"]