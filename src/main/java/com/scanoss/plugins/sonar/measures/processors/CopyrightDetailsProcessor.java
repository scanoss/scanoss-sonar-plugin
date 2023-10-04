package com.scanoss.plugins.sonar.measures.processors;

import com.scanoss.dto.CopyrightDetails;
import com.scanoss.dto.ScanFileDetails;
import com.scanoss.plugins.sonar.measures.ScanOSSMetrics;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * Copyright Details Measure Processor
 */
public class CopyrightDetailsProcessor implements MeasureProcessor {

    /**
     * Creates empty CopyrightDetailsProcessor
     */
    public CopyrightDetailsProcessor(){
    }

    /**
     * The logger object for the measure processor.
     */
    private final Logger log = Loggers.get(this.getClass());

    /**
     * Saves Copyrights data
     * @param sensorContext Sonar Sensor Context
     * @param file Project file
     * @param scanData File's scan result
     */
    @Override
    public void processScanDetails(SensorContext sensorContext, InputFile file, ScanFileDetails scanData) {
        CopyrightDetails[] copyrights = scanData.getCopyrightDetails();

        if (copyrights == null || copyrights.length == 0) {
            log.debug("[SCANOSS] No copyrights entry found for: {}", file.filename());
            return;
        }

        // The copyrights metric equals to 1 if at least 1 copyright declaration is found, otherwise it is 0.
        int metricValueForFile =  copyrights.length > 0 ? 1 : 0;

        log.info("[SCANOSS] Any Copyright declarations found for file '{}': {}", file.filename(), (metricValueForFile>0? "yes":"no"));
        sensorContext.<Integer>newMeasure()
                .forMetric(ScanOSSMetrics.COPYRIGHT_COUNT)
                .on(file)
                .withValue(metricValueForFile)
                .save();
    }
}
