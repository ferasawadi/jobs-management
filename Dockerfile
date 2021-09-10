# build the jar file.
FROM gradle:7.2.0-jdk16 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
#  add --no-daemon to disable logs.
RUN gradle build
# building the image
FROM openjdk:16-ea-1-jdk-slim
# create dir
RUN mkdir /app
# compy the build jar
COPY --from=build /home/gradle/src/build/libs/*.jar /app/JobManagement-1.0.101.jar
# the port
EXPOSE 30000
# run the app
ENTRYPOINT ["java","-jar","/app/JobManagement-1.0.101.jar"]
