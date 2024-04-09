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

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

/**
 * IDE Metadata plugin metrics definition.
 *
 */
public class ScanOSSMetrics implements Metrics {

    /**
     * Creates a SCANOSS Metrics instance
     */
    public ScanOSSMetrics(){
        super();
    }
    /**
     * Plugin Domain Name
     */
    public static final String DOMAIN_SCANOSS = "ScanOSS";

    /**
     * Copyleft license count metric
     */
    public static final Metric<Integer> COPYLEFT_LICENSE_COUNT = new Metric.Builder("copyleft_license_count", "Copyleft License Count", Metric.ValueType.INT)
            .setDescription("This metric counts the number of Copyleft license declarations found by SCANOSS")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(DOMAIN_SCANOSS)
            .create();

    /**
     * Vulnerability count metric
     */
    public static final Metric<Integer> VULNERABILITY_COUNT = new Metric.Builder("vulnerability_count", "Vulnerability Count", Metric.ValueType.INT)
            .setDescription("This metric counts the number of vulnerabilities found by SCANOSS")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(DOMAIN_SCANOSS)
            .create();

    /**
     * Copyleft license count metric
     */
    public static final Metric<Double> SCANOSS_QUALITY_SCORE = new Metric.Builder("scanoss_quality_score", "Quality Score", Metric.ValueType.FLOAT)
            .setDescription("Quality rating based on the SCANOSS Scanner")
            .setDirection(Metric.DIRECTION_BETTER)
            .setQualitative(Boolean.TRUE)
            .setDomain(DOMAIN_SCANOSS)
            .create();

    /**
     * Copyright count metric
     */
    public static final Metric<Integer> COPYRIGHT_COUNT = new Metric.Builder("copyright_count", "Copyright Declarations Count", Metric.ValueType.INT)
            .setDescription("This metric counts the number of Copyright declarations found by SCANOSS")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(DOMAIN_SCANOSS)
            .create();

    /**
     * Defines the plugin metrics.
     *
     * @return the list of this plugin metrics
     */
    public List<Metric> getMetrics() {
        return Arrays.asList(
                COPYLEFT_LICENSE_COUNT, COPYRIGHT_COUNT, VULNERABILITY_COUNT/*, SCANOSS_QUALITY_SCORE*/);
    }
}
