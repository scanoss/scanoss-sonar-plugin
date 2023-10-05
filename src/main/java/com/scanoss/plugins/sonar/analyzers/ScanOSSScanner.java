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
     * Builds a SCANOSS scanner instance with credentials and certificates in place
     * @return SCANOSS Scanner instance
     */
    private Scanner buildScanner(){
        Scanner.ScannerBuilder scannerBuilder = Scanner.builder().url(this.apiUrl).apiKey(this.apiToken);
        if(this.customCertChain != null && !this.customCertChain.isEmpty()) {
            LOGGER.info("[SCANOSS] Setting custom certificate chain");
            LOGGER.debug("[SCANOSS] {}", this.customCertChain);
            scannerBuilder = scannerBuilder.customCert(this.customCertChain);
        }
        return scannerBuilder.build();
    }
}
