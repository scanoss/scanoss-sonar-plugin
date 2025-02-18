// SPDX-License-Identifier: MIT
/*
 * Copyright (c) 2023, SCANOSS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.scanoss.plugins.sonar.analyzers;

import com.scanoss.dto.ScanFileDetails;
import com.scanoss.dto.ScanFileResult;
import com.scanoss.plugins.sonar.model.ScanResult;
import com.scanoss.plugins.sonar.utils.PackageDetails;
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
     * HPSM option
     */
    private final Boolean isHpsmEnabled;

    /**
     * SCANOSS SETTINGS
     */
    private final Boolean isScanossSettingsEnabled;

    private final String scanossSettingsFilePath;

    /**
     * Analyzer constructor
     *
     * @param rootDir         Base directory to scan
     * @param url             SCANOSS API Endpoint
     * @param key             SCANOSS API Access Key (optional)
     * @param customCertChain SCANOSS Custom Certificate Chain (optional)
     */
    public ScanOSSAnalyzer(final File rootDir, String url, String key, String customCertChain, Boolean isHpsmEnabled,
                           Boolean isScanossSettingsEnabled, String scanossSettingsFilePath) {
        super();
        this.rootDir = rootDir;
        this.url = url;
        this.key = key;
        this.customCertChain = customCertChain;
        this.isHpsmEnabled = isHpsmEnabled;
        this.isScanossSettingsEnabled = isScanossSettingsEnabled;
        this.scanossSettingsFilePath = scanossSettingsFilePath;
    }

    /**
     * This function calls the SCANOSS scanner for a given set of file paths and processes the output
     * @param inputFilePaths List of input file paths
     * @return Scan result with a map containing all filenames as keys and the scan results as values
     */
    public ScanResult analyze(List<String> inputFilePaths)  {
        log.info("[SCANOSS] Starting scan process...");
        log.info("[SCANOSS] Plugin version: {} (SDK version: {})", PackageDetails.getVersion(), com.scanoss.utils.PackageDetails.getVersion());
        ScanOSSScanner scanner = new ScanOSSScanner(this.url,
                this.key,
                this.customCertChain,
                this.isHpsmEnabled,
                this.isScanossSettingsEnabled,
                this.scanossSettingsFilePath
        );
        List<String> output = scanner.runScan(rootDir.getPath(), inputFilePaths);
        if(output == null || output.isEmpty()){
            log.warn("[SCANOSS] Empty result");
            return new ScanResult();
        }
        // Process output
        return processOutput(output);
    }

    private ScanResult processOutput(List<String> output){
        ScanResult scanResult = new ScanResult();
        List<ScanFileResult> outputObject = JsonUtils.toScanFileResults(output);
        Map<String, List<ScanFileDetails>> map = outputObject.stream().collect(Collectors.toMap(ScanFileResult::getFilePath, ScanFileResult::getFileDetails));
        scanResult.setFiles(map);
        return scanResult;
    }

}
