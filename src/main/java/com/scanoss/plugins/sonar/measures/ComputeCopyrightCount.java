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
