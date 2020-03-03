FROM openjdk:11-jre

COPY target/parkhaus-manager-spring-1.0-SNAPSHOT.jar /deployments/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/deployments/app.jar"]