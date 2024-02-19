FROM openjdk:17

COPY target/user_management_service-0.0.1-SNAPSHOT.jar  /usr/app/

WORKDIR /usr/app/

ENTRYPOINT ["java", "-jar", "user_management_service-0.0.1-SNAPSHOT.jar"]
