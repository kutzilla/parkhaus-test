FROM openjdk:8-jdk-alpine

COPY target/lib/* /deployments/lib/
COPY target/*-runner.jar /deployments/app.jar

EXPOSE 8180

ENTRYPOINT ["java","-jar","/deployments/app.jar"]