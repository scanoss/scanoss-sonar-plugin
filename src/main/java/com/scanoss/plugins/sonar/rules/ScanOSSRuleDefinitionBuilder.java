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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Implementation of RulesDefinition for defining rules related to ScanOSS.
 */
public class ScanOSSRuleDefinitionBuilder implements RulesDefinition{
    public static final String REPOSITORY = "SCANOSS";
    public static final String LANGUAGE = "text";
    private ArrayList<RuleBaseDefinition> rulesDefinitions;

    /**
     * Constructor for ScanOSSRuleDefinitionBuilder.
     * Initializes the rulesDefinitions list with default rule definitions.
     */
    public ScanOSSRuleDefinitionBuilder(){
       this.rulesDefinitions =  new ArrayList<RuleBaseDefinition>(){{
            add(new ScanOSSUndeclaredComponentRuleDefinition());
        }};
    }

    /**
     * Defines rules for ScanOSS repository.
     *
     * @param context the context to define rules in
     */
    @Override
    public void define(Context context){

        RulesDefinition.NewRepository repository = context.createRepository(REPOSITORY, LANGUAGE).setName("SCANOSS Analyser");

        RulesDefinitionAnnotationLoader rulesDefinitionAnnotationLoader = new RulesDefinitionAnnotationLoader();
        rulesDefinitionAnnotationLoader.load(repository, this.rulesDefinitions.stream()
                .map(Object::getClass)
                .toArray(Class<?>[]::new));


        this.rulesDefinitions.forEach(ruleBaseDefinition -> {
                Objects.requireNonNull(repository.rule(ruleBaseDefinition.getRuleKey()))
                        .addDescriptionSection(ruleBaseDefinition.getRootCauseSection())
                        .addDescriptionSection(ruleBaseDefinition.getHowToFix());
        });


        repository.done();
    }

}
