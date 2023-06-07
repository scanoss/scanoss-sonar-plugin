package com.scanoss.plugins.sonar.settings;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.List;

import static java.util.Arrays.asList;

public class ScanOSSProperties {

  public static final String SCANOSS_API_URL_KEY = "sonar.scanoss.scan.apiurl";
  public static final String SCANOSS_API_URL_DEFAULT_VALUE = "https://osskb.org/api/scan/direct";
  public static final String SCANOSS_API_TOKEN_KEY = "sonar.scanoss.scan.key";
  public static final String SCANOSS_API_TOKEN_DEFAULT_VALUE = "";

  private ScanOSSProperties() {
    // only statics
  }

  public static List<PropertyDefinition> getProperties() {
    return asList(
            PropertyDefinition.builder(SCANOSS_API_URL_KEY)
      .multiValues(false)
      .defaultValue(SCANOSS_API_URL_DEFAULT_VALUE)
      .category("SCANOSS")
      .name("Scan API URL")
      .description("Scan API Endpoint with format \"http(s)://host:ip/api/scan/direct\". The --apiurl argument is used to pass it to the CLI.")
      .onQualifiers(Qualifiers.PROJECT)
      .build(),
            PropertyDefinition.builder(SCANOSS_API_TOKEN_KEY)
      .multiValues(false)
      .defaultValue(SCANOSS_API_TOKEN_DEFAULT_VALUE)
      .category("SCANOSS")
      .name("Scan API Token")
      .description("Scan API token. The --key argument is used to pass it to the CLI.")
      .onQualifiers(Qualifiers.PROJECT)
      .build()
    );
  }

}
