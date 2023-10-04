package com.scanoss.plugins.sonar.measures.processors;

import com.scanoss.dto.QualityDetails;
import com.scanoss.dto.ScanFileDetails;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import static com.scanoss.plugins.sonar.measures.ScanOSSMetrics.SCANOSS_QUALITY_SCORE;

/**
 * Quality Data Measure Processor
 */
public class QualityDetailsProcessor implements MeasureProcessor {

    /**
     * Creates empty QualityDetailsProcessor
     */
    public QualityDetailsProcessor(){
    }

    /**
     * The logger object for the measure processor.
     */
    private final Logger log = Loggers.get(this.getClass());

    /**
     * Saves Quality score  data
     * @param sensorContext Sonar Sensor Context
     * @param file Project file
     * @param scanData File's scan result
     */
    @Override
    public void processScanDetails(SensorContext sensorContext, InputFile file, ScanFileDetails scanData) {
        QualityDetails[] qualityList = scanData.getQualityDetails();
        if(qualityList == null || qualityList.length == 0) {
            log.warn("[SCANOSS] No quality information found for file: {}", file.filename());
            return;
        }

        QualityDetails quality = qualityList[0];
        String score = quality.getScore();
        if (score == null) {
            log.warn("[SCANOSS] Could not find score for file: {}", file.filename());
            return;
        }
        String[] values = score.split("/");
        double a = Double.valueOf(values[0]);
        double b = Double.valueOf(values[1]);
        double ratio = a / b;

        log.info("[SCANOSS] Any Copyright declarations found for file '{}': {}", file.filename(), score);

        sensorContext.<Double>newMeasure()
                .forMetric(SCANOSS_QUALITY_SCORE)
                .on(file)
                .withValue(ratio)
                .save();
    }
}
