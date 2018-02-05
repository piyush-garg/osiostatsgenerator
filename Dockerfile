FROM maven:3.5-jdk-8 as BUILD_IMAGE
COPY src /usr/src/osio-stats/src
COPY pom.xml /usr/src/osio-stats
RUN mvn -f /usr/src/osio-stats/pom.xml clean compile assembly:single

FROM openjdk:8-jre
WORKDIR /root/
COPY --from=BUILD_IMAGE /usr/src/osio-stats/target/statsGenerator-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","statsGenerator-1.0-SNAPSHOT.jar"]
