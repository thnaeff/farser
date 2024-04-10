package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class ComplexIfTestAstContext {

  public final Map<String, Object> variablesContent;
  public final Set<String> evaluatedExpressions;

  public ComplexIfTestAstContext(Consumer<Map<String, Object>> variablesContentSupplier) {
    this.variablesContent = new HashMap<>();
    // Apply setup
    variablesContentSupplier.accept(variablesContent);
    this.evaluatedExpressions = new LinkedHashSet<>();
  }


}
