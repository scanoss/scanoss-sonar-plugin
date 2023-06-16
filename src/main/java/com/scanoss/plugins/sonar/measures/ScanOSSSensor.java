package com.scanoss.plugins.sonar.measures;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.scanoss.plugins.sonar.analyzers.AnalyzerException;
import com.scanoss.plugins.sonar.analyzers.ScanOSSAnalyzer;
import com.scanoss.plugins.sonar.measures.ScanOSSMetrics;
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

import static com.scanoss.plugins.sonar.measures.ScanOSSMetrics.SCANOSS_QUALITY_SCORE;

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
     * The configuration object for the connection details.
     */
    private final Configuration config;
    /**
     * The logger object for the sensor.
     */
    private final Logger log = Loggers.get(this.getClass());
    /**
     * Constructor that sets the file system object for the
     * project being analysed.
     *
     * @param fileSystem the project file system
     */
    public ScanOSSSensor(FileSystem fileSystem, Configuration config) {

        this.fileSystem = fileSystem;
        this.config = config;
    }

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Scan with SCANOSS");
    }

    @Override
    public void execute(SensorContext sensorContext) {
        File rootDir = fileSystem.baseDir();

        log.info("[SCANOSS] Analysing project root: " + rootDir.getAbsolutePath());

        String url = config.get(ScanOSSProperties.SCANOSS_API_URL_KEY).get();
        String token = config.get(ScanOSSProperties.SCANOSS_API_TOKEN_KEY).get();
        ScanOSSAnalyzer analyzer = new ScanOSSAnalyzer(rootDir, url, token);
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
            Map<String, List<ScanData>> files = projectInfo.getFiles();
            log.info("[SCANOSS] Scanned files: " + files.entrySet().size());

            for (String fileKey: files.keySet()) {
                Iterator<InputFile> it = this.fileSystem.inputFiles(fileSystem.predicates().hasRelativePath(fileKey)).iterator();
                if(!it.hasNext()) {
                    log.warn("[SCANOSS] Could not find Sonar project file in for : " + fileKey);
                    continue;
                }
                InputFile file = it.next();
                List<ScanData> scanDataList = files.get(file.relativePath());
                log.info("[SCANOSS] Found project file '" + file.filename() + "' ("+ file.uri().getPath() +") and matched output to " +  scanDataList.size() + " result.");
                if (scanDataList == null || scanDataList.size() == 0){
                    log.warn("[SCANOSS] Could not match Sonar project file with SCANOSS output: " + fileKey);
                    continue;
                }

                log.info("[SCANOSS] Saving measures for file " + file.filename());
                ScanData fileScanResult = scanDataList.get(0);
                saveLicenses(sensorContext, file, fileScanResult);
                saveQualityData(sensorContext, file, fileScanResult);
                saveVulnerabilities(sensorContext, file, fileScanResult);
            }

        } catch (Exception e) {
            log.error("[SCANOSS] Error while running ScanOSS Sensor", e);
        }
    }

    private void saveQualityData(SensorContext sensorContext, InputFile file, ScanData fileScanResult) {
        List<QualityAttribute> qualityList = fileScanResult.getQuality();
        if(qualityList == null || qualityList.size()==0) {
            log.warn("[SCANOSS] No quality information found for file: " + file.filename());
            return;
        }

        QualityAttribute quality = qualityList.get(0);
        Float score = quality.getScoreAsFloat();
        if (score == null) {
            log.warn("[SCANOSS] Could not find score for file:" + file.filename());
            return;
        }

        sensorContext.<Integer>newMeasure()
                .forMetric(SCANOSS_QUALITY_SCORE)
                .on(file)
                .withValue(score.intValue())
                .save();

    }

    private void saveLicenses(SensorContext sensorContext, InputFile file, ScanData scanData) {
        List<LicenseInfo> licenses = scanData.getLicenses();
        if (licenses == null || licenses.size() == 0) {
           log.debug("[SCANOSS] No licenses found for: " + file.filename());
           return;
        }

        boolean copyleft = licenses.stream().anyMatch(licenseInfo -> licenseInfo.isCopyleft());
        log.info("[SCANOSS] Any Copyleft declaration found for file '"+file.filename()+"': "+ (copyleft? "yes":"no"));
        sensorContext.<Integer>newMeasure()
                .forMetric(ScanOSSMetrics.COPYLEFT_LICENSE_COUNT)
                .on(file)
                .withValue(copyleft ? 1 : 0)
                .save();
    }

    private void saveVulnerabilities(SensorContext sensorContext, InputFile file, ScanData scanData) {
        List<VulnerabilityInfo> vulnerabilities = scanData.getVulnerabilities();

        int vulnerabilityCount = 0;
        if (vulnerabilities != null) {
            vulnerabilityCount = vulnerabilities.size();
        }

        log.info("[SCANOSS] Vulnerabilities found for file '"+file.filename()+"': "+ vulnerabilityCount);
        sensorContext.<Integer>newMeasure()
                .forMetric(ScanOSSMetrics.VULNERABILITY_COUNT)
                .on(file)
                .withValue(vulnerabilityCount)
                .save();
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
