package com.scanoss.plugins.sonar.measures;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static com.scanoss.plugins.sonar.measures.ScanOSSMetrics.COPYLEFT_LICENSE_COUNT;

public class ComputeCopyleftCount implements MeasureComputer {

  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(COPYLEFT_LICENSE_COUNT.key())
      .build();
  }

  @Override
  public void compute(MeasureComputerContext context) {
    // measure is already defined on files by {@link SetSizeOnFilesSensor}
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
