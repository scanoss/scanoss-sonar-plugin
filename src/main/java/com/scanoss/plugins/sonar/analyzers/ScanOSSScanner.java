package com.scanoss.plugins.sonar.analyzers;


import com.scanoss.Scanner;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Arrays;
import java.util.List;

/**
 * SCANOSS Scanner
 * <p>
 * Performs the scan of files using the scanoss.java library
 * </p>
 */
public class ScanOSSScanner {

    /**
     * The API Url
     */
    private String apiUrl;

    /**
     * The API Token
     */
    private String apiToken;

    /**
     * The Custom Certificate Chain
     */
    private String customCertChain;

    /**
     * The logger.
     */
    private static final Logger LOGGER = Loggers.get(ScanOSSScanner.class);

    /**
     * Empty constructor
     */
    public ScanOSSScanner(){

    }

    /**
     * ScanOSSScanner Constructor
     * @param apiUrl Scan API Url
     * @param apiToken Scan API Token
     * @param customCertChain Custom Certificate Chain PEM
     */
    public ScanOSSScanner(String apiUrl, String apiToken, String customCertChain){
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.customCertChain = customCertChain;
    }

    /**
     * API URL getter
     * @return Url string
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * API URL Setter
     * @param apiUrl Scan API URL
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * API Token getter
     * @return Token string
     */
    public String getApiToken() {
        return apiToken;
    }

    /**
     * API Token setter
     * @param apiToken Scan API Token
     */
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    /**
     * Custom Certificate Chain getter
     * @return Custom Certificate Chain
     */
    public String getCustomCertChain() {
        return customCertChain;
    }

    /**
     * Custom Certificate Chain setter
     * @param customCertChain Custom Certificate Chain (PEM format)
     */
    public void setCustomCertChain(String customCertChain) {
        this.customCertChain = customCertChain;
    }

    /**
     * Scan files in the given path against the given API endpoint url
     * @param path folder to scan
     * @return Scan result (in JSON format)
     * @throws RuntimeException Scanning went wrong
     */
    public List<String> runScan(String path) throws RuntimeException {
        LOGGER.info("[SCANOSS] Scanning path " + path + "...");
        Scanner.ScannerBuilder scannerBuilder = Scanner.builder().url(this.apiUrl).apiKey(this.apiToken);
        if(this.customCertChain != null && !this.customCertChain.isEmpty()) {
            LOGGER.info("[SCANOSS] Setting custom certificate chain");
            LOGGER.debug("[SCANOSS]" + this.customCertChain);
            scannerBuilder = scannerBuilder.customCert(this.customCertChain);
        }
        Scanner scanner = scannerBuilder.build();
        List<String> output = scanner.scanFolder(path);
        LOGGER.info("[SCANOSS] Scan finished");
        LOGGER.debug("[SCANOSS] " + Arrays.toString(output.toArray()));
        return output;
    }

    /**
     * Scan files in the given path against the given API endpoint url
     * @param basePath Base Path
     * @param files list of file paths to scan
     * @return Scan result (in JSON format)
     * @throws RuntimeException Scanning went wrong
     */
    public List<String> runScan(String basePath, List<String> files) throws RuntimeException {
        LOGGER.info("[SCANOSS] Scanning " + basePath + "-" + files.size() + " files...");
        Scanner scanner = this.buildScanner();
        List<String> output = scanner.scanFileList(basePath, files);
        LOGGER.info("[SCANOSS] Scan finished");
        LOGGER.debug("[SCANOSS] " + Arrays.toString(output.toArray()));
        return output;
    }

    /**
     * Builds a SCANOSS scanner instance with credentials and certificates in place
     * @return SCANOSS Scanner instance
     */
    private Scanner buildScanner(){
        Scanner.ScannerBuilder scannerBuilder = Scanner.builder().url(this.apiUrl).apiKey(this.apiToken);
        if(this.customCertChain != null && !this.customCertChain.isEmpty()) {
            LOGGER.info("[SCANOSS] Setting custom certificate chain");
            LOGGER.debug("[SCANOSS]" + this.customCertChain);
            scannerBuilder = scannerBuilder.customCert(this.customCertChain);
        }
        return scannerBuilder.build();
    }

}
