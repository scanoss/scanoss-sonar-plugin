package com.scanoss.plugins.sonar.analyzers;

import com.scanoss.plugins.sonar.model.ScanResult;
import com.scanoss.plugins.sonar.model.ScanData;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.util.List;
import java.util.Map;

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
     * The logger.
     */
    private final Logger log = Loggers.get(this.getClass());

    public ScanOSSAnalyzer(final File rootDir, String url, String key) {
        super();
        this.rootDir = rootDir;
        this.url = url;
        this.key = key;
    }

    public ScanResult analyze()  {
        log.info("[SCANOSS] Starting scan process...");
        ScanResult scanResult = new ScanResult();
        String output = ScanOSSScanner.runScan(rootDir.getPath(), this.url, this.key);
        if(output == null || output.isEmpty()){
            return null;
        }
        Map<String, List<ScanData>> stringListMap = ScanOSSParser.parseScanResult(output);
        scanResult.setFiles(stringListMap);
        return scanResult;
    }
}
