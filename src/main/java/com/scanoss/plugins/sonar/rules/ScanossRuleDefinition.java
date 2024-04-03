package com.scanoss.plugins.sonar.rules;


import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RulesDefinition;
import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.*;

public class ScanossRuleDefinition implements RulesDefinition {

    public static final String REPOSITORY = "Scanoss Repository";
    public static final String LANGUAGE = "text";
    public static final RuleKey RULE_UNDECLARED_COMPONENT = RuleKey.of(REPOSITORY, "Component Undeclared");

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPOSITORY, LANGUAGE).setName("SCANOSS Analyzer");

        // TODO Change the fix section key description when both identify and ignore file can be send to the API
        NewRule x1Rule = repository.createRule(RULE_UNDECLARED_COMPONENT.rule())
                .setName("Undeclared Component")
                .setHtmlDescription("Detects undeclared components in source code")
                .addDescriptionSection(descriptionSection(INTRODUCTION_SECTION_KEY, "SCANOSS Undeclared Component", null))
                .addDescriptionSection(descriptionSection(ROOT_CAUSE_SECTION_KEY, "The root cause of this issue is not declaring components on SBOM identify file.", null))
                .addDescriptionSection(descriptionSection(HOW_TO_FIX_SECTION_KEY,
                        "To fix an issue reported by this rule you must declare your components on the SBOM identify file", null))

                // optional tags
                .setTags("scanoss", "undeclared")

                // default severity when the rule is activated on a Quality profile. Default value is MAJOR.
                .setSeverity(Severity.MAJOR);

      //  x1Rule.setDebtRemediationFunction(x1Rule.debtRemediationFunctions().linearWithOffset("1h", "30min"));

        // don't forget to call done() to finalize the definition
        repository.done();
    }

    private static RuleDescriptionSection descriptionSection(String sectionKey, String htmlContent, org.sonar.api.server.rule.Context context) {
        return RuleDescriptionSection.builder()
                .sectionKey(sectionKey)
                .htmlContent(htmlContent)
                //Optional context - can be any framework or component for which you want to create detailed description
                .context(context)
                .build();
    }
}