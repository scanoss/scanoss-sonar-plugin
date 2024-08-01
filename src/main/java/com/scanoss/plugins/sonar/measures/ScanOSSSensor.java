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
package com.scanoss.plugins.sonar.measures;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.scanoss.dto.*;
import com.scanoss.plugins.sonar.analyzers.ScanOSSAnalyzer;
import com.scanoss.plugins.sonar.measures.processors.*;
import com.scanoss.plugins.sonar.model.*;
import com.scanoss.plugins.sonar.rules.ScanOSSRuleDefinitionBuilder;
import com.scanoss.plugins.sonar.settings.ScanOSSProperties;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.fs.FileSystem;

import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * IDE Metadata plugin sensor. It analyses project directory in search for
 * IDE metadata configuration files and extracts relevant information.
 *
 */
public class ScanOSSSensor implements Sensor {

    /**
     * The file system object for the project being analysed.
     */
    private final FileSystem fileSystem;

    /**
     * Available measure processors
     */
    private final ArrayList<MeasureProcessor> processors;

    /**
     * The configuration object for the connection details.
     */
    private final Configuration config;

    /**
     * The logger object for the sensor.
     */
    private final Logger log = Loggers.get(this.getClass());

    /**
     * Constructor that sets the file system and config objects
     * for the project being analysed.
     * @param fileSystem the project file system
     * @param config Plugin configuration
     */
    public ScanOSSSensor(FileSystem fileSystem, Configuration config) {
        this.fileSystem = fileSystem;
        this.config = config;
        this.processors = new ArrayList<MeasureProcessor>(){{
            add(new LicenseDetailsProcessor());
            add(new CopyrightDetailsProcessor());
            add(new VulnerabilityDetailsProcessor());
        }};

        // Only add UndeclaredComponentProcessor if SBOM-identify.json file exists
        if(!(getStringConfigValue(ScanOSSProperties.SCANOSS_SBOM_IDENTIFY).isEmpty())){
            this.processors.add(new UndeclaredComponentProcessor());

        }

    }

    /**
     * Sensor's Descriptor setting function
     * @param sensorDescriptor Sonar Sensor Descriptor
     */
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Scan with SCANOSS");
    }


    /**
     * Sensor's main function. Collects project's files, runs the scan and processes output
     * @param sensorContext Sonar Sensor Context
     */
    @Override
    public void execute(SensorContext sensorContext) {
        Boolean isEnabled = getBooleanConfigValue(ScanOSSProperties.SCANOSS_IS_ENABLED_KEY);
        if(! isEnabled)
        {
            log.info("[SCANOSS] Analysing project is disabled" );
        	return;
        }
        List<String> inputFilePaths = this.getInputFiles(sensorContext);
        File rootDir = fileSystem.baseDir();

        if (log.isDebugEnabled()) {
            log.debug("Root Path: {}", rootDir);
            log.debug("Input Files: {}", inputFilePaths);
        }
        String url = getStringConfigValue(ScanOSSProperties.SCANOSS_API_URL_KEY);
        String token = getStringConfigValue(ScanOSSProperties.SCANOSS_API_TOKEN_KEY);
        String customCertChain = getStringConfigValue(ScanOSSProperties.SCANOSS_CUSTOM_CERT_CHAIN_KEY);
        String sbomIdentify = getStringConfigValue(ScanOSSProperties.SCANOSS_SBOM_IDENTIFY);
        String sbomIgnore = getStringConfigValue(ScanOSSProperties.SCANOSS_SBOM_IGNORE);
        Boolean isHpsmEnabled = getBooleanConfigValue(ScanOSSProperties.SCANOSS_HPSM_KEY);


        ScanOSSAnalyzer analyzer = new ScanOSSAnalyzer(rootDir, url, token, customCertChain, sbomIdentify, sbomIgnore, isHpsmEnabled);
        ScanResult projectScanResult;

        try {
            projectScanResult = analyzer.analyze(inputFilePaths);

            if (projectScanResult == null) {
                log.error("[SCANOSS] Output is unavailable. Aborting...");
                return;
            }
            log.info("[SCANOSS] Analysis done");
            if (log.isDebugEnabled()) {
                Gson gson = new Gson();
                log.debug("This is what we've found: {}", gson.toJson(projectScanResult));
            }
            // Process all SCANOSS results
            Map<String, List<ScanFileDetails>> files = projectScanResult.getFiles();
            log.info("[SCANOSS] Scanned files: {}", files.entrySet().size());

            for (String fileKey: files.keySet()) {
                Iterator<InputFile> it = this.fileSystem.inputFiles(fileSystem.predicates().hasRelativePath(fileKey)).iterator();
                if(!it.hasNext()) {
                    log.warn("[SCANOSS] Could not find Sonar project file in for: {}", fileKey);
                    continue;
                }
                InputFile sonarFile = it.next();

                List<ScanFileDetails> scanDataList = files.get(fileKey);
                log.info("[SCANOSS] Found project file '{}' ({}) and matched output to {} result.", sonarFile.filename(), sonarFile.uri().getPath(), scanDataList.size());
                if (scanDataList.isEmpty()) {
                    log.warn("[SCANOSS] Could not match Sonar project file with SCANOSS output: {}", fileKey);
                    continue;
                }
                log.info("[SCANOSS] Saving measures for file {}", sonarFile.filename());
                ScanFileDetails fileScanResult = scanDataList.get(0);

                for (MeasureProcessor processor: processors) {
                    processor.processScanDetails(sensorContext, sonarFile, fileScanResult);
                }
            }
        } catch (Exception e) {
            log.error("[SCANOSS] Error while running ScanOSS Sensor ({}): {}", e.getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }
    }

    /**
     * Gets the projects files as a list of strings. It honors
     * @param context Sensor context
     * @return List of file paths
     */
    private List<String> getInputFiles(SensorContext context) {
        FilePredicates p = context.fileSystem().predicates();
        Iterable<InputFile> it = context.fileSystem().inputFiles(p.all());
        Stream<InputFile> stream = StreamSupport.stream(it.spliterator(), false);

        String rootPath = context.fileSystem().baseDir().getAbsolutePath();

        return stream.map(inputFile -> {
            File file = context.fileSystem().resolvePath(inputFile.uri().getPath());
            // Make it relative to the root path
            return Paths.get(rootPath).relativize(Paths.get(file.getAbsolutePath())).toString();
        }).collect(Collectors.toList());
    }

    /**
     * Gets a configuration value from the Sonar config store
     * @param key Sonar configuration key
     * @return Value for the given key
     */
    private String getStringConfigValue(String key){
        String value = "";
        Optional<String> optToken = config.get(key);
        if (optToken.isPresent()) {
            value = optToken.get();
        }
        return value;
    }

    /**
     * Gets a configuration value from the Sonar config store
     * @param key Sonar configuration key
     * @return Value for the given key
     */
    private Boolean getBooleanConfigValue(String key){
        Boolean value = Boolean.FALSE;
        Optional<Boolean> optToken = config.getBoolean(key);
        if (optToken.isPresent()) {
            value = optToken.get();
        }
        return value;
    }

    /**
     * Returns the name of the sensor as it will be used in logs during analysis.
     *
     * @return the name of the sensor
     */
    public String toString() {
        return "SCANOSSSensor";
    }

}
