package com.scanoss.plugins.sonar.rules;

import org.sonar.api.server.rule.RuleDescriptionSection;

/**
 * Abstract base class for defining rules.
 * This class defines methods for retrieving rule descriptions and keys,
 * and also provides a method for creating rule description sections.
 */

public abstract class RuleBaseDefinition {

    /**
     * Get the root cause section of the rule description.
     *
     * @return the root cause section of the rule description
     */
    public abstract RuleDescriptionSection getRootCauseSection();

    /**
     * Get the how-to-fix section of the rule description.
     *
     * @return the how-to-fix section of the rule description
     */
    public abstract RuleDescriptionSection getHowToFix();

    /**
     * Get the how-to-fix section of the rule description.
     *
     * @return the how-to-fix section of the rule description
     */
    public abstract String getRuleKey();

    /**
     * Get the key name of the rule.
     *
     * @return the key name of the rule
     */
    public abstract String getKeyName();

    /**
     * Get the description of the rule.
     *
     * @return the description of the rule
     */
    public abstract String getDescription();


    /**
     * Helper method to create a rule description section.
     *
     * @param sectionKey   the key identifying the section
     * @param htmlContent  the HTML content of the section
     * @return a RuleDescriptionSection object representing the section
     */
    protected RuleDescriptionSection descriptionSection(String sectionKey, String htmlContent) {
        return RuleDescriptionSection.builder()
                .sectionKey(sectionKey)
                .htmlContent(htmlContent)
                .build();
    }
}
