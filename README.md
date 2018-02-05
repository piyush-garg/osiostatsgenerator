# OpenShift.io Stats Generator

This repository contain an application which will show some stats for Openshift.io repository in your browser.

## How to run

- Clone the repo
- Move to folder `osiostatsgenerator`
- Build app: `mvn clean compile assembly:single`
- Run app: `java -jar target/statsGenerator-1.0-SNAPSHOT.jar`
- You can also run with Authentication : `java -jar target/statsGenerator-1.0-SNAPSHOT.jar Github_Token`
- Open browser and hit `http://localhost:8000/stats`

You can see [here](https://help.github.com/articles/creating-a-personal-access-token-for-the-command-line/) how to get access token.

## How to run on OpenShift

The latest image has been pushed to Docker Hub. To deploy the application on OpenShift, simply run

- `kedge apply -f kedge.yml`

Now service and deployment wiil be created. Expose your service to create route.

- `oc expose service service_name`

Get the api endpoint of Route and append /stats to it at the end. (Example : http://------.nio.io/stats)

Open the link in your browser. You will be able to see OSIO stats.
