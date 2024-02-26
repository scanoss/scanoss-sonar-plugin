package com.scanoss.plugins.sonar.settings;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import java.util.List;

import static java.util.Arrays.asList;

  /**
   * SCANOSS Properties class
   */
public class ScanOSSProperties {

    /**
     * SCANOSS Enable/Disable Scan key
     */
    public static final String SCANOSS_IS_ENABLED_KEY = "sonar.scanoss.scan.enabled";
  /**
   * SCANOSS API URL Configuration key
   */
  public static final String SCANOSS_API_URL_KEY = "sonar.scanoss.scan.apiurl";

  /**
   * SCANOSS API URL Configuration default value
   */
  public static final String SCANOSS_API_URL_DEFAULT_VALUE = "https://osskb.org/api/scan/direct";

  /**
   * SCANOSS API Token Configuration key
   */
  public static final String SCANOSS_API_TOKEN_KEY = "sonar.scanoss.scan.key";

  /**
   * SCANOSS API Token Configuration default value
   */
  public static final String SCANOSS_API_TOKEN_DEFAULT_VALUE = "";

  /**
   * SCANOSS Custom Certificate Chain key
   */
  public static final String SCANOSS_CUSTOM_CERT_CHAIN_KEY = "sonar.scanoss.scan.custom_cert_chain";

  /**
   * SCANOSS Custom Certificate Chain default value
   */
  public static final String SCANOSS_CUSTOM_CERT_CHAIN_DEFAULT_VALUE = "";

  /**
   * SCANOSS Enable/Disable Scan SBOM Identify key
   */
  public static final String SCANOSS_IS_SBOM_IDENTIFY_ENABLED_KEY = "sonar.scanoss.scan.sbom.enabled";

  /**
   * SCANOSS Enable/Disable Scan key
   */
  public static final String SCANOSS_SBOM_FILENAME_KEY = "sonar.scanoss.scan.sbom.filename";

  /**
   * SCANOSS API URL Configuration default value
   */
  public static final String SCANOSS_SBOM_FILENAME_DEFAULT_VALUE = "sbom.json";

  /**
   * Private constructor to allow only statics
   */
  private ScanOSSProperties() {
    // only statics
  }

  /**
   * Builds the list of property definitions for Sonar
   * @return List of property definitions
   */
  public static List<PropertyDefinition> getProperties() {
        PropertyDefinition isEnabled = PropertyDefinition.builder(SCANOSS_IS_ENABLED_KEY)
            .multiValues(false)
            .type(PropertyType.BOOLEAN)
            .defaultValue(String.valueOf(false))
            .category("SCANOSS")
            .name("Is Scan Enabled")
            .description("Is ScanOSS scanning enabled?")
            .onQualifiers(Qualifiers.PROJECT)
            .index(0)
            .build();
    PropertyDefinition apiUrl = PropertyDefinition.builder(SCANOSS_API_URL_KEY)
            .multiValues(false)
            .defaultValue(SCANOSS_API_URL_DEFAULT_VALUE)
            .category("SCANOSS")
            .name("Scan API URL")
            .description("Scan API Endpoint with format \"http(s)://host:ip/api/scan/direct\". The --apiurl argument is used to pass it to the CLI.")
            .onQualifiers(Qualifiers.PROJECT)
            .index(1)
            .build();
    PropertyDefinition apiToken = PropertyDefinition.builder(SCANOSS_API_TOKEN_KEY)
            .multiValues(false)
            .defaultValue(SCANOSS_API_TOKEN_DEFAULT_VALUE)
            .category("SCANOSS")
            .name("Scan API Token")
            .description("Scan API token. The --key argument is used to pass it to the CLI.")
            .onQualifiers(Qualifiers.PROJECT)
            .index(2)
            .build();
    PropertyDefinition customCertChain = PropertyDefinition.builder(SCANOSS_CUSTOM_CERT_CHAIN_KEY)
            .type(PropertyType.TEXT)
            .multiValues(false)
            .defaultValue(SCANOSS_CUSTOM_CERT_CHAIN_DEFAULT_VALUE)
            .category("SCANOSS")
            .name("Custom Certificate Chain")
            .description("The custom certificate chain pem value.")
            .onQualifiers(Qualifiers.PROJECT)
            .index(3)
            .build();
    PropertyDefinition isIdentifyEnabled = PropertyDefinition.builder(SCANOSS_IS_SBOM_IDENTIFY_ENABLED_KEY)
            .multiValues(false)
            .type(PropertyType.BOOLEAN)
            .defaultValue(String.valueOf(true))
            .category("SCANOSS")
            .name("Is SBOM identify Enabled")
            .description("Is ScanOSS SBOM identify enabled?")
            .onQualifiers(Qualifiers.PROJECT)
            .index(4)
            .build();
    PropertyDefinition sbomFilename = PropertyDefinition.builder(SCANOSS_SBOM_FILENAME_KEY)
            .multiValues(false)
            .defaultValue(SCANOSS_SBOM_FILENAME_DEFAULT_VALUE)
            .category("SCANOSS")
            .name("Scan input SBOM filename")
            .description("Scan and identify components in SBOM file. The --identify argument is used to pass it to the CLI.")
            .onQualifiers(Qualifiers.PROJECT)
            .index(5)
            .build();

    return asList(
            isEnabled,
            apiUrl,
            apiToken,
            customCertChain,
            isIdentifyEnabled,
            sbomFilename
    );
  }

}
