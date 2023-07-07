package com.scanoss.plugins.sonar.measures.processors;

import com.scanoss.dto.ScanFileDetails;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

/**
 * Measure Processor interface
 */
public interface MeasureProcessor {

    /**
     * Method definition for extracting and saving result details
     * @param sensorContext Sonar Sensor Context
     * @param file Project file
     * @param scanData File's scan result
     */
    public void processScanDetails(SensorContext sensorContext, InputFile file, ScanFileDetails scanData);
}
