package com.scanoss.plugins.sonar.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RulesDefinition;
import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.*;

public class ScanOSSRuleDefinitions implements RulesDefinition {

    public static final String REPOSITORY = "SCANOSS";
    public static final String LANGUAGE = "text";
    public static final RuleKey RULE_UNDECLARED_COMPONENT = RuleKey.of(REPOSITORY, "Undeclared Component");

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPOSITORY, LANGUAGE).setName("SCANOSS Analyser");

        // TODO Change the fix section key description when both identify and ignore file can be send to the API
        repository.createRule(RULE_UNDECLARED_COMPONENT.rule())
                .setName("Undeclared OSS Component")
                .setHtmlDescription("Detects undeclared OSS components in source code")
                .addDescriptionSection(descriptionSection(INTRODUCTION_SECTION_KEY, "SCANOSS Undeclared Component"))
                .addDescriptionSection(descriptionSection(ROOT_CAUSE_SECTION_KEY, "The root cause of this issue is failure to declare components in the SBOM file."))
                .addDescriptionSection(descriptionSection(HOW_TO_FIX_SECTION_KEY,
                        "To resolve this issue, ensure that the missing components are declared in the SBOM file at the root of this repo."))

                // optional tags
                .setTags("scanoss", "oss-undeclared")

                // default severity when the rule is activated on a Quality profile. Default value is MAJOR.
                .setSeverity(Severity.MAJOR);

        // don't forget to call done() to finalize the definition
        repository.done();
    }

    private static RuleDescriptionSection descriptionSection(String sectionKey, String htmlContent) {
        return RuleDescriptionSection.builder()
                .sectionKey(sectionKey)
                .htmlContent(htmlContent)
                .build();
    }
}