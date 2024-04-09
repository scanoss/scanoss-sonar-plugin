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
package com.scanoss.plugins.sonar;

import com.scanoss.plugins.sonar.measures.*;
import com.scanoss.plugins.sonar.measures.processors.UndeclaredComponentProcessor;
import com.scanoss.plugins.sonar.rules.ScanOSSRuleDefinitionBuilder;
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


        context.addExtensions(ScanOSSRuleDefinitionBuilder.class, UndeclaredComponentProcessor.class);
    }

}
