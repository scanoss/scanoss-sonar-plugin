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
            log.debug("[SCANOSS] No licenses found for: {}", file.filename());
            return;
        }

        List<LicenseDetails> licenses = Arrays.asList(licenseDetails);
        if (log.isDebugEnabled()) {
            Gson gson = new Gson();
            log.debug("[SCANOSS] {}", gson.toJson(licenses));
        }
        boolean copyleft = licenses.stream().anyMatch(LicenseDetails::isCopyleft);
        log.info("[SCANOSS] Any Copyleft declaration found for file '{}': {}", file.filename(), (copyleft? "yes":"no"));
        sensorContext.<Integer>newMeasure()
                .forMetric(ScanOSSMetrics.COPYLEFT_LICENSE_COUNT)
                .on(file)
                .withValue(copyleft ? 1 : 0)
                .save();
    }

}
