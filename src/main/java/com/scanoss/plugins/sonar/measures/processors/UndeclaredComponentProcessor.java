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
import com.scanoss.plugins.sonar.helpers.BomIssueHelper;
import com.scanoss.plugins.sonar.rules.ScanOSSUndeclaredComponentRuleDefinition;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
        if (matchType != MatchType.none && (scanData.getStatus() != StatusType.identified)) {
            NewIssue newIssue = sensorContext.newIssue()
                    .forRule(ScanOSSUndeclaredComponentRuleDefinition.ruleKey);

            List<NewIssueLocation> locations = new ArrayList<>();
            TextRange firstRange = null;

            // Check if it's a 100% match or specific lines
            if (scanData.getMatchType() == MatchType.file) {// Handle 100% match - select entire file
                // Handle 100% match - select entire file
                int totalLines = sonarFile.lines();
                TextPointer start = sonarFile.selectLine(1).start();
                TextPointer end = sonarFile.selectLine(totalLines).end();

                firstRange = sonarFile.newRange(start, end);  // Store the range

                NewIssueLocation location = newIssue.newLocation()
                        .on(sonarFile)
                        .message("Undeclared components: " + Arrays.toString(scanData.getPurls()))
                        .at(firstRange);

                locations.add(location);
            } else {
                // Handle specific line ranges (e.g. "7-9,37-74,91-98,120-145")
                String[] ranges = scanData.getLines().split(",");
                boolean isFirst = true;

                for (String range : ranges) {
                    String[] lines = range.split("-");
                    int startLine = Integer.parseInt(lines[0]);
                    int endLine = Integer.parseInt(lines[1]);

                    TextPointer start = sonarFile.selectLine(startLine).start();
                    TextPointer end = sonarFile.selectLine(endLine).end();

                    TextRange currentRange = sonarFile.newRange(start, end);
                    if (isFirst) {
                        firstRange = currentRange;  // Store the first range
                        isFirst = false;

                        NewIssueLocation location = newIssue.newLocation()
                                .on(sonarFile)
                                .message("Undeclared components: " + Arrays.toString(scanData.getPurls()))
                                .at(currentRange);
                        locations.add(location);
                        continue;
                    }

                    NewIssueLocation location = newIssue.newLocation()
                            .on(sonarFile)
                            .at(currentRange);
                    locations.add(location);

                }
            }

            // Set the first location as primary
            if (!locations.isEmpty()) {
                NewIssueLocation primaryLocation = locations.get(0);
                newIssue.at(primaryLocation);

                // Add remaining locations as secondary
                for (int i = 1; i < locations.size(); i++) {
                    newIssue.addLocation(locations.get(i));
                }

                // Add fix message as final location
                NewIssueLocation fixLocation = newIssue.newLocation()
                        .on(sonarFile)
                        .at(firstRange)  // Use the stored first range
                        .message(BomIssueHelper.createBomFixMessage(scanData.getPurls()));

                newIssue.addLocation(fixLocation);

                newIssue.save();
            }
        }
    }
}

