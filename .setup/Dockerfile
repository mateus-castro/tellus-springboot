FROM openjdk:11-jdk

USER root

RUN mkdir -p /usr/src/backend
WORKDIR /usr/src/backend

COPY ./target/*.jar /usr/src/backend

# PORTA LOCAL E HML 8080 ~ * ~ PORTA PRD 8082 - 8083
#EXPOSE 8080
#CMD ["java","-Dmaven.test.skip=true", "-Dserver.port=8080","-jar","tellus-springboot-0.0.1-SNAPSHOT.jar"]

EXPOSE 8082
CMD ["java","-Dmaven.test.skip=true", "-Dserver.port=8082","-jar","tellus-springboot-0.0.1-SNAPSHOT.jar"]
