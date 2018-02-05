# OpenShift.io Stats Generator

This repository contain an application which will show some stats for Openshift.io repository in your browser.

## How to run

- Clone the repo
- Move to folder `osiostatsgenerator`
- Build app: `mvn clean compile assembly:single`
- Run app: `java -jar target/statsGenerator-1.0-SNAPSHOT.jar`
- Open browser and hit `http://localhost:8000/stats`
