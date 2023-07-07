
package com.scanoss.plugins.sonar.measures;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static com.scanoss.plugins.sonar.measures.ScanOSSMetrics.SCANOSS_QUALITY_SCORE;

/**
 * Compute Score Average
 * <p>
 * Computes the SCANOSS Score Average Measure.
 * </p>
 */
public class ComputeScoreAverage implements MeasureComputer {

  /**
   *  Creates a SCANOSS Average Score Measure Computer
   */
  public ComputeScoreAverage(){
    super();
  }
  /**
   * Defines SCANOSS Score measure
   * @param def Sonar Definition Context
   * @return new Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(SCANOSS_QUALITY_SCORE.key())
      .build();
  }

  /**
   * Computes average SCANOSS Score across the project
   * @param context Sonar Measure Computer context
   */
  @Override
  public void compute(MeasureComputerContext context) {
    // measure is already defined on files by {@link ScanOSSMetrics}
    // in scanner stack
    if (context.getComponent().getType() != Component.Type.FILE) {
      double sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(SCANOSS_QUALITY_SCORE.key())) {
        sum += child.getDoubleValue();
        count++;
      }
      double average = count == 0 ? 0 : sum / count;
      context.addMeasure(SCANOSS_QUALITY_SCORE.key(), average);
    }
  }
}
