package com.scanoss.plugins.sonar;

import com.scanoss.plugins.sonar.measures.*;
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
     * @return the list of extensions for this plugin
     */

    @Override
    public void define(Context context) {
        context.addExtensions(ScanOSSProperties.getProperties());
        context.addExtension(ScanOSSMetrics.class);
        context.addExtension(ScanOSSSensor.class);
        context.addExtension(ComputeCopyleftCount.class);
        context.addExtension(ComputeVulnerabilityCount.class);
        context.addExtension(ComputeScanossScoreAverage.class);
    }

}
