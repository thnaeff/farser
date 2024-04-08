package com.mmm.his.cer.utility.farser.ast_if.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.BooleanExpression;
import com.mmm.his.cer.utility.farser.ast_if.setup.lex.IfTestToken;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class IfTestTerminalNode implements BooleanExpression<IfTestAstContext> {

  private final IfTestToken token;

  public IfTestTerminalNode(IfTestToken token) {
    this.token = token;
  }

  @Override
  public Boolean evaluate(IfTestAstContext context) {
    // Record all the terminal nodes which get hit by the evaluation
    context.evaluatedExpressions.add(token.getValue());

    // A terminal node expression resolves to 'true' when the context contains the expression
    // variable. Since the test expressions are just a single character, the expression is just a
    // single token.
    return context.trueExpressions.contains(token.getValue());
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
