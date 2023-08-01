package com.scanoss.plugins.sonar.measures;

import java.io.File;
import java.util.*;

import com.google.gson.Gson;
import com.scanoss.dto.*;
import com.scanoss.plugins.sonar.analyzers.ScanOSSAnalyzer;
import com.scanoss.plugins.sonar.measures.processors.CopyrightDetailsProcessor;
import com.scanoss.plugins.sonar.measures.processors.LicenseDetailsProcessor;
import com.scanoss.plugins.sonar.measures.processors.MeasureProcessor;
import com.scanoss.plugins.sonar.measures.processors.VulnerabilityDetailsProcessor;
import com.scanoss.plugins.sonar.model.*;
import com.scanoss.plugins.sonar.settings.ScanOSSProperties;
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
 * @author jorge.hidalgo
 * @version 1.0
 */
public class ScanOSSSensor implements Sensor {

    /**
     * The file system object for the project being analysed.
     */
    private final FileSystem fileSystem;

    /**
     * Available measure processors
     */
    private final MeasureProcessor[] processors = new MeasureProcessor[]{
            new LicenseDetailsProcessor(),
            new CopyrightDetailsProcessor(),
            new VulnerabilityDetailsProcessor()
    };

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

        File rootDir = fileSystem.baseDir();

        log.info("[SCANOSS] Analysing project root: " + rootDir.getAbsolutePath());

        String url = getStringConfigValue(ScanOSSProperties.SCANOSS_API_URL_KEY);
        String token = getStringConfigValue(ScanOSSProperties.SCANOSS_API_TOKEN_KEY);
        String customCertChain = getStringConfigValue(ScanOSSProperties.SCANOSS_CUSTOM_CERT_CHAIN_KEY);

        ScanOSSAnalyzer analyzer = new ScanOSSAnalyzer(rootDir, url, token, customCertChain);
        ScanResult projectInfo;

        try {
            projectInfo = analyzer.analyze();

            if (projectInfo == null) {
                log.error("[SCANOSS] Output is unavailable. Aborting...");
                return;
            }
            log.info("[SCANOSS] Analysis done");
            Gson gson = new Gson();
            log.debug("this is what we've found: " + gson.toJson(projectInfo));

            // Process all SCANOSS results
            Map<String, List<ScanFileDetails>> files = projectInfo.getFiles();
            log.info("[SCANOSS] Scanned files: " + files.entrySet().size());

            for (String fileKey: files.keySet()) {
                Iterator<InputFile> it = this.fileSystem.inputFiles(fileSystem.predicates().hasRelativePath(fileKey)).iterator();
                if(!it.hasNext()) {
                    log.warn("[SCANOSS] Could not find Sonar project file in for : " + fileKey);
                    continue;
                }
                InputFile file = it.next();
                List<ScanFileDetails> scanDataList = files.get(file.relativePath());
                log.info("[SCANOSS] Found project file '" + file.filename() + "' ("+ file.uri().getPath() +") and matched output to " +  scanDataList.size() + " result.");
                if (scanDataList == null || scanDataList.size() == 0){
                    log.warn("[SCANOSS] Could not match Sonar project file with SCANOSS output: " + fileKey);
                    continue;
                }

                log.info("[SCANOSS] Saving measures for file " + file.filename());
                ScanFileDetails fileScanResult = scanDataList.get(0);

                for (MeasureProcessor processor: processors) {
                    processor.processScanDetails(sensorContext, file, fileScanResult);
                }

            }
        } catch (Exception e) {
            log.error("[SCANOSS] Error while running ScanOSS Sensor", e);
        }
    }

    /**
     * Gets a configuration value from the Sonar config store
     * @param key
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
     * @param key
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
