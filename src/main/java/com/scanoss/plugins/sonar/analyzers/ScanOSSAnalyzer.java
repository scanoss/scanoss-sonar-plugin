package com.scanoss.plugins.sonar.analyzers;

import com.scanoss.dto.ScanFileDetails;
import com.scanoss.dto.ScanFileResult;
import com.scanoss.plugins.sonar.model.ScanResult;
import com.scanoss.utils.JsonUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SCANOSS Analyzer
 *
 * <p>
 * Performs a scan and processes the output
 * </p>
 */
public class ScanOSSAnalyzer {

    /**
     * The project root directory.
     */
    private final File rootDir;

    /**
     * The API  URL.
     */
    private final String url;

    /**
     * The API key/token
     */
    private final String key;

    /**
     * The Custom Certificate Chain
     */
    private final String customCertChain;

    /**
     * The logger.
     */
    private final Logger log = Loggers.get(this.getClass());

    /**
     * Analyzer constructor
     *
     * @param rootDir         Base directory to scan
     * @param url             SCANOSS API Endpoint
     * @param key             SCANOSS API Access Key (optional)
     * @param customCertChain SCANOSS Custom Certificate Chain (optional)
     */
    public ScanOSSAnalyzer(final File rootDir, String url, String key, String customCertChain) {
        super();
        this.rootDir = rootDir;
        this.url = url;
        this.key = key;
        this.customCertChain = customCertChain;
    }

    /**
     * This function calls the SCANOSS scanner and processes the output
     * @return Scan result with a map containing all filenames as keys and the scan results as values
     */
    public ScanResult analyze()  {
        log.info("[SCANOSS] Starting scan process...");
        ScanOSSScanner scanner = new ScanOSSScanner(this.url, this.key, this.customCertChain);
        List<String> output = scanner.runScan(rootDir.getPath());
        if(output == null || output.isEmpty()){
            log.warn("[SCANOSS] Empty result");
            return null;
        }
        // Process output
        ScanResult scanResult = new ScanResult();
        List<ScanFileResult> outputObject = JsonUtils.toScanFileResults(output);
        Map<String, List<ScanFileDetails>> map = outputObject.stream().collect(Collectors.toMap(ScanFileResult::getFilePath, ScanFileResult::getFileDetails));
        scanResult.setFiles(map);
        return scanResult;
    }
}
