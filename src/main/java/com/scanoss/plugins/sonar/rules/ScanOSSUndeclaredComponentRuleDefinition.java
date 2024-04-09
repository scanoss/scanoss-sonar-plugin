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