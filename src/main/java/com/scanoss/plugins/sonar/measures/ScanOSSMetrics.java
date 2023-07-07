package com.scanoss.plugins.sonar.measures;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

/**
 * IDE Metadata plugin metrics definition.
 *
 * @author scanoss-ap
 * @version 1.0
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
