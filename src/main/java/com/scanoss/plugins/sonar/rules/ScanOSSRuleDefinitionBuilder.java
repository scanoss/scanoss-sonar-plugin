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
