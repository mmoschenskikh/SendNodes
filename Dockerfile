FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle fatJar

FROM openjdk:11
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/send-nodes.jar
