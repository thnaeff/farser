package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.Expression;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.lex.ComplexIfTestToken;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class ComplexIfTestTerminalExpression implements Expression<ComplexIfTestAstContext, Object> {

  private final ComplexIfTestToken token;

  public ComplexIfTestTerminalExpression(ComplexIfTestToken token) {
    this.token = token;
  }

  @Override
  public Object evaluate(ComplexIfTestAstContext context) {
    // Record all the terminal nodes which get hit by the evaluation
    context.evaluatedExpressions.add(token.getValue());

    if (context.variablesContent.containsKey(token.getValue())) {
      return context.variablesContent.get(token.getValue());
    } else {
      return token.getValue();
    }
  }

  @Override
  public String print() {
    return token.value;
  }

  @Override
  public String toString() {
    return token.value;
  }

}
