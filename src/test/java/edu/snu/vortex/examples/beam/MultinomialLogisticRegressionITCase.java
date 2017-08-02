/*
 * Copyright (C) 2017 Seoul National University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.snu.vortex.examples.beam;

import edu.snu.vortex.client.JobLauncher;
import edu.snu.vortex.compiler.CompilerTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing Multinomial Logistic Regressions with JobLauncher.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JobLauncher.class)
public final class MultinomialLogisticRegressionITCase {
  private static final int TIMEOUT = 120000;
  private static final String mlr = "edu.snu.vortex.examples.beam.MultinomialLogisticRegression";
  private static final String input = CompilerTestUtil.rootDir + "/src/main/resources/sample_input_mlr";
  private static final String numFeatures = "100";
  private static final String numClasses = "5";
  private static final String numIteration = "3";
  private static final String dagDirectory = "./dag";

  public static ArgBuilder builder = new ArgBuilder()
      .addJobId(MultinomialLogisticRegressionITCase.class.getSimpleName())
      .addUserMain(mlr)
      .addUserArgs(input, numFeatures, numClasses, numIteration)
      .addDAGDirectory(dagDirectory);

  @Before
  public void setUp() throws Exception {
    builder = new ArgBuilder()
        .addUserMain(mlr)
        .addUserArgs(input, numFeatures, numClasses, numIteration)
        .addDAGDirectory(dagDirectory);
  }

  // Disabled for speed purposes. To be re-enabled through TODO #138.
//  @Test (timeout = TIMEOUT)
//  public void test() throws Exception {
//    JobLauncher.main(builder
//        .addJobId(MultinomialLogisticRegressionITCase.class.getSimpleName())
//        .build());
//  }

  @Test (timeout = TIMEOUT)
  public void testPado() throws Exception {
    JobLauncher.main(builder
        .addJobId(MultinomialLogisticRegressionITCase.class.getSimpleName() + "_pado")
        .addOptimizationPolicy("pado")
        .build());
  }
}
