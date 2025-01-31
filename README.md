SCANOSS SonarQube Custom Plugin
==========

SonarQube Plugin leveraging the SCANOSS Java SDK to perform scans, compatible with SonarQube 10.x.

## Breaking change v1.0.1

- Removed parameters:
  - `identify.json`
  - `ignore.json`

### Converting from sbom.json to scanoss.json
The SBOM configuration format has changed and the file name must be updated from **sbom.json** to **scanoss.json**. Here's how to convert your existing configuration:

Old format (sbom.json):
```json
{
  "components": [
    {
      "purl": "pkg:github/scanoss/scanner.c"
    }
  ]
}
```

New format (scanoss.json):
```json
{
  "bom": {
    "include": [
      {
        "purl": "pkg:github/scanoss/scanner.c"
      }
    ]
  }
}
```

## Features

### Capabilities
* High Precision Snippet Matching (HPSM)
* SCANOSS Settings Ingestion

### Reported metrics 
* Copyleft License Count
* Copyright Declarations Count
* Vulnerability Count

### Reported Issues
* Undeclared Components

**NOTE**: Some information may require a SCANOSS Premium subscription. 

## Building

To build the plugin JAR file, call:

```
mvn clean package
```

The JAR will be deployed to `target/scanoss-sonar-plugin-VERSION.jar`. Copy this to your SonarQube's `extensions/plugins/` directory, and re-start SonarQube.

## How to use

Once the plugin has been copied into SonarQube, restart Sonar and proceed to configure the SCANOSS plugin as needed.

### Configuration options:
| **Parameter**              | **Description**                                                                                                                                          | **Required** | **Default**             | 
|----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|-------------------------|
| Scan API URL               | SCANOSS API Endpoint with format "http(s)://host:ip".                                                                                                    | Optional     | `https://api.osskb.org` |
| Scan API Token             | SCANOSS API token.                                                                                                                                       | Optional     | -                       |
| Custom Certificate Chain   | The custom certificate chain pem value.                                                                                                                  | Optional     | -                       |
| SCANOSS HPSM               | Use High Precision Snippet Matching algorithm (Only available with premium subscription).                                                                | Optional     | `false`                 |
| SCANOSS Settings           | Settings file to use for scanning. See the SCANOSS settings [documentation](https://scanoss.readthedocs.io/projects/scanoss-py/en/latest/#settings-file) | Optional     | `true`                  |
| SCANOSS Settings File Path | Filepath of the SCANOSS settings to be used for scanning                                                                                                 | Optional     | `scanoss.json`          |



### Issues Reporting

The plugin identifies Undeclared Components that are not listed in the SBOM IDENTIFY file.

To activate this feature, ensure that you have configured the SBOM IDENTIFY file and set up the following Quality Profile.

#### Setting Up Quality Profile
1. Navigate to the Quality Profile tab.
2. In the desired language section, create a new profile named "SCANOSS Way".
3. Within the "SCANOSS Way" profile, include a new activity.
4. Search for the "Undeclared Component" activity in the SCANOSS Analyser repository.
5. Activate the "Undeclared Component" activity to add it to the "SCANOSS Way" profile.

#### Setting Up Project Quality Profile or issues
1. Navigate to your project.
2. Click on Project Settings and select Quality Profiles option.
3. Click on "Add language".
4. Choose "text" language and select "SCANOSS Way" profile.


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



# Extracting SCANOSS data from Sonar Web API

## API Requirements:
Access to the Sonar Web API requires a user-type token. Refer to the SonarQube API documentation https://next.sonarqube.com/sonarqube/web_api.


## Authorization:
Following header in your request is required:
Authorization: Bearer [TOKEN]

## Metrics API

- ### List metrics
  This endpoint lists all available metrics and can be filtered to find all SCANOSS metrics

       https://{SONARQUBE_URL}/api/metrics/search?ps=500

- ### Show metric
  This endpoint shows details of the given metrics

       https://{SONARQUBE_URL}/api/measures/component?component=integration-sonarqube&metricKeys=copyleft_license_count,copyright_count,vulnerability_count 

- ### Issues API
    This endpoint allows searching of created issues given a Rule ID
    
      https://{SONARQUBE_URL}/api/issues/search?rules=SCANOSS:UndeclaredComponent