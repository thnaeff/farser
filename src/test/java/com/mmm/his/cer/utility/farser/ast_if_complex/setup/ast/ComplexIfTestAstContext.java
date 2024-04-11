package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class ComplexIfTestAstContext {

  public final Map<String, Object> variablesContent;
  public final List<String> evaluatedExpressions;

  public ComplexIfTestAstContext(Consumer<Map<String, Object>> variablesContentSupplier) {
    this.variablesContent = new HashMap<>();
    // Apply setup
    variablesContentSupplier.accept(variablesContent);
    this.evaluatedExpressions = new ArrayList<>();
  }


}
