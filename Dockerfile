FROM openjdk:11-jdk

USER root

RUN mkdir -p /usr/src/backend
WORKDIR /usr/src/backend

COPY target/*.jar /usr/src/backend

# expose app port and run
EXPOSE 8080
CMD ["java","--jar","tellus-springboot-0.0.1-SNAPSHOT.jar"]
