SCANOSS SonarQube Custom Plugin Example 
==========

An example SonarQube plugin compatible with SonarQube 10.x.

Back-end
--------

Example SonarQube Plugin leveraging the SCANOSS Python CLI to perform scans. The following metrics are reported to SonarQube:

* Copyleft License Count
* Copyright Declarations Count
* Vulnerability Count
* Quality Score

NOTE: Some of the example information may require a SCANOSS Premium subscription. 

### Building

To build the plugin JAR file, call:

```
mvn clean package
```

The JAR will be deployed to `target/scanoss-sonar-plugin-VERSION.jar`. Copy this to your SonarQube's `extensions/plugins/` directory, and re-start SonarQube.

### Running

The SCANOSS Python CLI is required in the runner. We provide an example Dockerfile adding the Python client to the Local SonarQube runner.

To build the container image, call:

```
docker build -t sonar-scanoss-cli .
``` 

To run a scan using the container image:

```
export SONARQUBE_URL=127.0.0.1:9000
export PROJECT_KEY=[PROJECT_KEY]
export myAuthenticationToken=[AUTH_TOKEN]
docker run \
    --rm \
    -e SONAR_HOST_URL="http://${SONARQUBE_URL}" \
    -e SONAR_SCANNER_OPTS="-Dsonar.projectKey=${PROJECT_KEY}" \
    -e SONAR_TOKEN="${myAuthenticationToken}" \
    -v "$(pwd):/usr/src" \
    sonar-scanoss-cli
```