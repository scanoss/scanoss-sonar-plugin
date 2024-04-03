package com.scanoss.plugins.sonar.analyzers;


import com.scanoss.Scanner;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final String apiUrl;

    /**
     * The API Token
     */
    private final String apiToken;

    /**
     * The Custom Certificate Chain
     */
    private final String customCertChain;

    /**
     * The logger.
     */
    private static final Logger LOGGER = Loggers.get(ScanOSSScanner.class);

    /**
     * SBOM identify file name
     */
    private final String sbomIdentify;


    /**
     * SBOM identify file name
     */
    private final String sbomIgnore;

    /**
     * ScanOSSScanner Constructor
     * @param apiUrl Scan API Url
     * @param apiToken Scan API Token
     * @param customCertChain Custom Certificate Chain PEM
     */
    public ScanOSSScanner(String apiUrl, String apiToken, String customCertChain, String sbomIdentify, String sbomIgnore){
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.customCertChain = customCertChain;
        this.sbomIdentify = sbomIdentify;
        this.sbomIgnore = sbomIgnore;
    }

    /**
     * Scan files in the given path against the given API endpoint url
     * @param basePath Base Path
     * @param files list of file paths to scan
     * @return Scan result (in JSON format)
     * @throws RuntimeException Scanning went wrong
     */
    public List<String> runScan(String basePath, List<String> files) throws RuntimeException {
        LOGGER.info("[SCANOSS] Scanning {} files from {} ...", files.size(), basePath);
        Scanner scanner = this.buildScanner();
        List<String> output = scanner.scanFileList(basePath, files);
        LOGGER.info("[SCANOSS] Scan finished");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[SCANOSS] {}", Arrays.toString(output.toArray()));
        }
        return output;
    }

    /**
     * Load the specified file into a string
     * @param filename filename to load
     * @return loaded string
     */
    private String loadFileToString(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException(String.format("File does not exist or is not a file: %s", filename));
        }
        try {
            return Files.readString(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds a SCANOSS scanner instance with credentials and certificates in place
     * @return SCANOSS Scanner instance
     */
    private Scanner buildScanner(){
        Scanner.ScannerBuilder scannerBuilder = Scanner.builder().url(this.apiUrl).apiKey(this.apiToken);
        if(this.sbomIgnore != null && !this.sbomIgnore.isEmpty()){
            scannerBuilder.sbomType("blacklist");
            scannerBuilder.sbom(loadFileToString(this.sbomIgnore));
        }

        if(this.sbomIdentify !=null && !this.sbomIdentify.isEmpty()){
            scannerBuilder.sbomType("identify");
            scannerBuilder.sbom(loadFileToString(this.sbomIdentify));
            scannerBuilder.scanFlags("512");
        }


        if(this.customCertChain != null && !this.customCertChain.isEmpty()) {
            LOGGER.info("[SCANOSS] Setting custom certificate chain");
            LOGGER.debug("[SCANOSS] {}", this.customCertChain);
            scannerBuilder = scannerBuilder.customCert(this.customCertChain);
        }
        return scannerBuilder.build();
    }
}
