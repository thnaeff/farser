package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.Statement;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.lex.ComplexIfTestToken;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class ComplexIfTestTerminalStatement implements Statement<ComplexIfTestAstContext> {

  private final ComplexIfTestToken token;

  public ComplexIfTestTerminalStatement(ComplexIfTestToken token) {
    this.token = token;
  }

  @Override
  public void process(ComplexIfTestAstContext context) {
    // Record all the terminal nodes which get hit by the evaluation
    context.evaluatedExpressions.add(token.getValue());
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
