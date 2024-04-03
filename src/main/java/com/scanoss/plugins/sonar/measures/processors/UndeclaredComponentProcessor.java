package com.scanoss.plugins.sonar.measures.processors;

import com.scanoss.dto.ScanFileDetails;
import com.scanoss.plugins.sonar.rules.ScanossRuleDefinition;
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
     private final boolean process;

    public UndeclaredComponentProcessor(boolean process){
        this.process = process;
    }

    @Override
    public void processScanDetails(SensorContext sensorContext, InputFile sonarFile, ScanFileDetails scanData) {

        if(!this.process) return;

        LOGGER.info("[SCANOSS] Undeclared component processor", this.process);

        String matchId = scanData.getId();

        if(!matchId.equalsIgnoreCase("none")){
            NewIssue newIssue = sensorContext.newIssue()
                    .forRule(ScanossRuleDefinition.RULE_UNDECLARED_COMPONENT);

            NewIssueLocation primaryLocation = newIssue.newLocation()
                    .on(sonarFile)
                    .message("SCANOSS undeclared component: " + Arrays.toString(scanData.getPurls()));
            newIssue.at(primaryLocation);
            newIssue.save();
        }
    }

}