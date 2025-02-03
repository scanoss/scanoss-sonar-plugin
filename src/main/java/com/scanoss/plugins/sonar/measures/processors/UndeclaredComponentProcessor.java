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

import com.scanoss.dto.ScanFileDetails;
import com.scanoss.dto.enums.MatchType;
import com.scanoss.dto.enums.StatusType;
import com.scanoss.plugins.sonar.rules.ScanOSSUndeclaredComponentRuleDefinition;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import java.util.Arrays;


/**
 * Generates issues on all java files at line 1. This rule
 * must be activated in the Quality profile.
 */
public class UndeclaredComponentProcessor implements MeasureProcessor {

    private static final Logger LOGGER = Loggers.get(UndeclaredComponentProcessor.class);

    @Override
    public void processScanDetails(SensorContext sensorContext, InputFile sonarFile, ScanFileDetails scanData) {

        LOGGER.info("[SCANOSS] Undeclared component processor");

        MatchType matchType = scanData.getMatchType();
        if(matchType != MatchType.none && (scanData.getStatus() != StatusType.identified)){
            NewIssue newIssue = sensorContext.newIssue()
                    .forRule(ScanOSSUndeclaredComponentRuleDefinition.ruleKey);

            NewIssueLocation primaryLocation = newIssue.newLocation()
                    .on(sonarFile)
                    .message("SCANOSS Undeclared Component: " + Arrays.toString(scanData.getPurls()));
            newIssue.at(primaryLocation);
            newIssue.save();
        }
    }

}