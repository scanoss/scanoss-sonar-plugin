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
