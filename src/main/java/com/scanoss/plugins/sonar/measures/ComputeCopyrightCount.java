package com.scanoss.plugins.sonar.measures;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static com.scanoss.plugins.sonar.measures.ScanOSSMetrics.COPYRIGHT_COUNT;

/**
 * Compute Copyright Count
 * <p>
 * Computes the Copyright Count Measure. A file is marked as copyrighted if
 * at least one copyright declaration is found in any file matches
 * </p>
 */
public class ComputeCopyrightCount implements MeasureComputer {

  /**
   * Creates a Copyright Measure Computer
   */
  public ComputeCopyrightCount(){
    super();
  }

  /**
   * Defines Copyrights measure
   * @param def Sonar Definition Context
   * @return new Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(COPYRIGHT_COUNT.key())
      .build();
  }

  /**
   * Computes Copyrights count
   * @param context Sonar Measure Computer context
   */
  @Override
  public void compute(MeasureComputerContext context) {
    // measure is already defined on files by {@link ScanOSSMetrics}
    if (context.getComponent().getType() != Component.Type.FILE) {
      int sum = 0;
      for (Measure child : context.getChildrenMeasures(COPYRIGHT_COUNT.key())) {
        sum += child.getIntValue();
      }
      context.addMeasure(COPYRIGHT_COUNT.key(), sum);
    }
  }
}
