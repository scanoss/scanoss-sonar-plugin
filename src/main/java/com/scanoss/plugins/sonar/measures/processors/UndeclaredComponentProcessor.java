package com.scanoss.plugins.sonar.measures.processors;

import com.scanoss.dto.ScanFileDetails;
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

        String matchId = scanData.getId();

        if(!matchId.equalsIgnoreCase("none")){
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