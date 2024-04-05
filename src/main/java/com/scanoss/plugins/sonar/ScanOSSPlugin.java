package com.scanoss.plugins.sonar;

import com.scanoss.plugins.sonar.measures.*;
import com.scanoss.plugins.sonar.measures.processors.UndeclaredComponentProcessor;
import com.scanoss.plugins.sonar.rules.ScanOSSRuleDefinitions;
import com.scanoss.plugins.sonar.settings.ScanOSSProperties;
import org.sonar.api.Plugin;

/**
 * SCANOSS plugin definition.
 *
 * @author scanoss-ap
 * @version 1.0
 */
public class ScanOSSPlugin implements Plugin {

    /**
     * Defines the plugin extensions: metrics, sensor and dashboard widget.
     *
     * @param context Plugin's context
     */
    @Override
    public void define(Context context) {
        context.addExtensions(ScanOSSProperties.getProperties());
        context.addExtension(ScanOSSMetrics.class);
        context.addExtension(ScanOSSSensor.class);
        context.addExtension(ComputeCopyleftCount.class);
        context.addExtension(ComputeCopyrightCount.class);
        context.addExtension(ComputeVulnerabilityCount.class);
        //context.addExtension(ComputeScoreAverage.class);


        context.addExtensions(ScanOSSRuleDefinitions.class, UndeclaredComponentProcessor.class);
    }

}
