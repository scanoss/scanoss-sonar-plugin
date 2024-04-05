SCANOSS SonarQube Custom Plugin
==========

SonarQube Plugin leveraging the SCANOSS Java SDK to perform scans, compatible with SonarQube 10.x.

## Features

### Capabilities
* High Precision Snippet Matching (HPSM)
* SBOM Ingestion

### Reported metrics 
* Copyleft License Count
* Copyright Declarations Count
* Vulnerability Count

### Reported Issues
* Undeclared Components

NOTE: Some of the example information may require a SCANOSS Premium subscription. 

## Building

To build the plugin JAR file, call:

```
mvn clean package
```

The JAR will be deployed to `target/scanoss-sonar-plugin-VERSION.jar`. Copy this to your SonarQube's `extensions/plugins/` directory, and re-start SonarQube.

## How to use

Once the plugin has been copied in SonarQube, restart Sonar and proceed to configure the SCANOSS plugin as needed.

Configuration options:
- **Scan API URL** : SCANOSS API Endpoint with format "http(s)://host:ip".
- **Scan API Token**: SCANOSS API token.
- **Custom Certificate Chain**: The custom certificate chain pem value.
- **SCANOSS SBOM IDENTIFY**: SCANOSS SBOM identify filename.
- **SCANOSS SBOM IGNORE**: SCANOSS SBOM ignore filename.
- **SCANOSS HPSM**: Use High Precision Snippet Matching algorithm (Only available with premium subscription).

### Running

To run a scan, execute the following command:

```
export SONARQUBE_URL=host.docker.internal:9000
export PROJECT_KEY=[PROJECT_KEY]
export myAuthenticationToken=[AUTH_TOKEN]
docker run \
    --rm \
    --add-host=host.docker.internal:host-gateway \
    -e SONAR_HOST_URL="http://${SONARQUBE_URL}" \
    -e SONAR_SCANNER_OPTS="-Dsonar.projectKey=${PROJECT_KEY}" \
    -e SONAR_TOKEN="${myAuthenticationToken}" \
    -v "$(pwd):/usr/src" \
    sonarsource/sonar-scanner-cli -Dsonar.java.binaries=target    
```