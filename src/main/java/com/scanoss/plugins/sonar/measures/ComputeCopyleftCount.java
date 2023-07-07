package com.scanoss.plugins.sonar.measures;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static com.scanoss.plugins.sonar.measures.ScanOSSMetrics.COPYLEFT_LICENSE_COUNT;

/**
 * Compute Copyleft Count
 * <p>
 * Computes the Copyleft Count Measure
 * </p>
 */
public class ComputeCopyleftCount implements MeasureComputer {

  /**
   * Creates a Copyleft Measure Computer
   */
  public ComputeCopyleftCount(){
    super();
  }

  /**
   * Defines Copyleft measure
   * @param def Sonar Definition Context
   * @return new Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(COPYLEFT_LICENSE_COUNT.key())
      .build();
  }

  /**
   * Computes Copyleft count
   * @param context Sonar Measure Computer context
   */
  @Override
  public void compute(MeasureComputerContext context) {
    // measure is already defined on files by {@link ScanOSSMetrics}
    // in scanner stack
    if (context.getComponent().getType() != Component.Type.FILE) {
      int sum = 0;
      for (Measure child : context.getChildrenMeasures(COPYLEFT_LICENSE_COUNT.key())) {
        sum += child.getIntValue();
      }
      context.addMeasure(COPYLEFT_LICENSE_COUNT.key(), sum);
    }
  }
}
