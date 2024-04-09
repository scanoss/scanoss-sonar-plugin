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
        double a = Double.parseDouble(values[0]);
        double b = Double.parseDouble(values[1]);
        double ratio = a / b;

        log.info("[SCANOSS] Any Copyright declarations found for file '{}': {}", file.filename(), score);

        sensorContext.<Double>newMeasure()
                .forMetric(SCANOSS_QUALITY_SCORE)
                .on(file)
                .withValue(ratio)
                .save();
    }

}
