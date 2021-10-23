FROM openjdk:10-alpine

USER root

RUN mkdir -p /usr/src/backend
WORKDIR /usr/src/backend

# install app dependencies
COPY target\tellus-springboot-0.0.1-SNAPSHOT.jar /usr/src/backend

# expose app port and run
EXPOSE 8080
CMD ["java","-jar","tellus-springboot.jar"]