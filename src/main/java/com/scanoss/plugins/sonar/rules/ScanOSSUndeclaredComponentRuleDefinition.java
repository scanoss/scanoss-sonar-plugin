package com.scanoss.plugins.sonar.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.*;

/**
 * Rule definition for detecting undeclared OSS components in the source code.
 */
@Rule(key = ScanOSSUndeclaredComponentRuleDefinition.rkey , name = ScanOSSUndeclaredComponentRuleDefinition.keyName, description = ScanOSSUndeclaredComponentRuleDefinition.description, tags = { "scanoss" , "undeclared" }, priority = Priority.MAJOR)
public class ScanOSSUndeclaredComponentRuleDefinition extends RuleBaseDefinition {

    public static final String keyName =  "Undeclared Component";
    public static final String rkey =  "UndeclaredComponent";
    public static final String description = "Detects undeclared OSS components in source code";
    public static final RuleKey ruleKey = RuleKey.of(ScanOSSRuleDefinitionBuilder.REPOSITORY, ScanOSSUndeclaredComponentRuleDefinition.rkey);

    /**
     * {@inheritDoc}
     */
    @Override
    public  RuleDescriptionSection getRootCauseSection() {
        return this.descriptionSection(ROOT_CAUSE_SECTION_KEY, "The root cause of this issue is failure to declare components in the SBOM file.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RuleDescriptionSection getHowToFix() {
       return  descriptionSection(HOW_TO_FIX_SECTION_KEY,"To resolve this issue, ensure that the missing components are declared in the SBOM file at the root of this repo.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRuleKey(){
        return ScanOSSUndeclaredComponentRuleDefinition.rkey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKeyName() {
        return ScanOSSUndeclaredComponentRuleDefinition.keyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return ScanOSSUndeclaredComponentRuleDefinition.description;
    }

}