// SPDX-License-Identifier: MIT
/*
 * Copyright (c) 2024, SCANOSS
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
