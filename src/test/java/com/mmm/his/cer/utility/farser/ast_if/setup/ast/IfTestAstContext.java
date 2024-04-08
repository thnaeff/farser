package com.mmm.his.cer.utility.farser.ast_if.setup.ast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class IfTestAstContext {

  public final Set<String> trueExpressions;
  public final Set<String> evaluatedExpressions;

  public IfTestAstContext(String... trueExpressions) {
    this.trueExpressions = new HashSet<>(Arrays.asList(trueExpressions));
    this.evaluatedExpressions = new HashSet<>();
  }


}
