package com.mmm.his.cer.utility.farser.ast_if.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.Expression;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;
import com.mmm.his.cer.utility.farser.ast_if.setup.lex.IfTestToken;
import com.mmm.his.cer.utility.farser.lexer.FarserException;

public class IfTestAstNodeSupplier
implements NodeSupplier<IfTestToken, IfTestAstContext> {

  @Override
  public Expression<IfTestAstContext, ?> createNode(final IfTestToken inToken) {
    return new IfTestTerminalNode(inToken);
  }


  @Override
  public NonTerminal<IfTestAstContext, ?> createNonTerminalNode(IfTestToken token) {

    switch (token.type) {
      case IF:
        return new IfTestIfOperator<>();
      case THEN:
        return new IfTestThenOperator<>();
      case ELSE:
        return new IfTestElseOperator<>();
      default:
        throw new FarserException("Operator type " + token + " not implemented");
    }

  }


}
