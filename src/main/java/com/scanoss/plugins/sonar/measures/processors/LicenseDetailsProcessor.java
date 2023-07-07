package com.scanoss.plugins.sonar.measures.processors;

import com.google.gson.Gson;
import com.scanoss.dto.LicenseDetails;
import com.scanoss.dto.ScanFileDetails;
import com.scanoss.plugins.sonar.measures.ScanOSSMetrics;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Arrays;
import java.util.List;

/**
 * License Details Measure Processor
 */
public class LicenseDetailsProcessor implements MeasureProcessor {

    /**
     * Creates empty LicenseDetailsProcessor
     */
    public LicenseDetailsProcessor(){
    }

    /**
     * The logger object for the measure processor.
     */
    private final Logger log = Loggers.get(this.getClass());

    /**
     * Saves Licenses data
     * @param sensorContext Sonar Sensor Context
     * @param file Project file
     * @param scanData File's scan result
     */
    @Override
    public void processScanDetails(SensorContext sensorContext, InputFile file, ScanFileDetails scanData) {
        LicenseDetails[] licenseDetails = scanData.getLicenseDetails();
        if (licenseDetails == null || licenseDetails.length == 0) {
            log.debug("[SCANOSS] No licenses found for: " + file.filename());
            return;
        }

        List<LicenseDetails> licenses = Arrays.asList(licenseDetails);
        Gson gson = new Gson();

        log.debug("[SCANOSS] " + gson.toJson(licenses));

        boolean copyleft = licenses.stream().map(LicenseDetails::isCopyleft).anyMatch(copyleftValue -> copyleftValue != null && copyleftValue);
        log.info("[SCANOSS] Any Copyleft declaration found for file '"+file.filename()+"': "+ (copyleft? "yes":"no"));
        sensorContext.<Integer>newMeasure()
                .forMetric(ScanOSSMetrics.COPYLEFT_LICENSE_COUNT)
                .on(file)
                .withValue(copyleft ? 1 : 0)
                .save();
    }
}
