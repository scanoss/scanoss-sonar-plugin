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

import com.scanoss.Scanner;
import com.scanoss.settings.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static com.scanoss.plugins.sonar.settings.ScanOSSProperties.SCANOSS_SETTINGS_FILE_PATH_DEFAULT_VALUE;

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
     * HPSM option
     */
    private final Boolean isHpsmEnabled ;

    /**
     * SCANOSS SETTINGS
     */
    private final Boolean isScanossSettingsEnabled;

    private final String scanossSettingsFilePath;


    /**
     * ScanOSSScanner Constructor
     * @param apiUrl Scan API Url
     * @param apiToken Scan API Token
     * @param customCertChain Custom Certificate Chain PEM
     */
    public ScanOSSScanner(String apiUrl, String apiToken, String customCertChain, Boolean isHpsmEnabled,
                          Boolean isScanossSettingsEnabled, String scanossSettingsFilePath) {
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.customCertChain = customCertChain;
        this.isHpsmEnabled = isHpsmEnabled;
        this.isScanossSettingsEnabled = isScanossSettingsEnabled;
        this.scanossSettingsFilePath = scanossSettingsFilePath;
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
        Scanner scanner = this.buildScanner(basePath);
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
    private Scanner buildScanner(String basePath){
        Scanner.ScannerBuilder scannerBuilder = Scanner.builder().url(this.apiUrl + "/scan/direct" ).apiKey(this.apiToken);

        LOGGER.info("[SCANOSS] SCANOSS Settings enabled: {}", this.isScanossSettingsEnabled);
        if(this.isScanossSettingsEnabled){
            boolean isUsingCustomSettingPath =  !this.scanossSettingsFilePath.equals(SCANOSS_SETTINGS_FILE_PATH_DEFAULT_VALUE);
            File f = new File(Path.of(basePath, this.scanossSettingsFilePath).toString());
            boolean fileExists = f.exists();
            // Check if user is using a different path as default. Throw exception in case the file not exists
            if(isUsingCustomSettingPath && !fileExists){
                throw new RuntimeException(String.format("SCANOSS settings file not found at: %s. Please provide a valid " +
                        "SCANOSS settings file path.", this.scanossSettingsFilePath));
            }

            // Check if file path exists. Not throw error if user is not using the default path.
            if(!fileExists){
                LOGGER.warn("[SCANOSS] SCANOSS Settings file not found. Please add the scanoss.json file in your project's root directory");
            }else{
                LOGGER.info("[SCANOSS] SCANOSS Settings file found: {}", this.scanossSettingsFilePath);
                scannerBuilder.settings(Settings.createFromPath(Path.of(basePath, this.scanossSettingsFilePath)));
            }
        }

        if(this.customCertChain != null && !this.customCertChain.isEmpty()) {
            LOGGER.info("[SCANOSS] Setting custom certificate chain");
            LOGGER.debug("[SCANOSS] {}", this.customCertChain);
            scannerBuilder = scannerBuilder.customCert(this.customCertChain);
        }


        if(this.isHpsmEnabled && (!this.apiToken.isEmpty())){
            scannerBuilder.hpsm(true);
        }

        return scannerBuilder.build();
    }
}
