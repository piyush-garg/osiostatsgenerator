FROM openjdk:8-jre
WORKDIR /root/
COPY target/statsGenerator-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java", "-verbose","-jar","statsGenerator-1.0-SNAPSHOT.jar"]
