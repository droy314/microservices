FROM resin/rpi-raspbian:jessie
MAINTAINER Deepu Roy
RUN apt-get install -y openjdk-7-jre

ADD /jars /jars
EXPOSE 8080
ENTRYPOINT java -jar users-0.0.1-SNAPSHOT.jar