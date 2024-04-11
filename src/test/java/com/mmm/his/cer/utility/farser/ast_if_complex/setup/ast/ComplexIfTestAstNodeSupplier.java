package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.operator.And;
import com.mmm.his.cer.utility.farser.ast.node.operator.Not;
import com.mmm.his.cer.utility.farser.ast.node.operator.Or;
import com.mmm.his.cer.utility.farser.ast.node.type.Expression;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;
import com.mmm.his.cer.utility.farser.ast.node.type.Statement;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.lex.ComplexIfTestToken;
import com.mmm.his.cer.utility.farser.lexer.FarserException;

public class ComplexIfTestAstNodeSupplier
implements NodeSupplier<ComplexIfTestToken, ComplexIfTestAstContext> {

  @Override
  public Expression<ComplexIfTestAstContext, ?> createNode(final ComplexIfTestToken inToken) {
    return new ComplexIfTestTerminalExpression(inToken);
  }

  @Override
  public Statement<ComplexIfTestAstContext> createStatement(ComplexIfTestToken token) {
    return new ComplexIfTestTerminalStatement(token);
  }


  @Override
  public NonTerminal<ComplexIfTestAstContext, ?> createNonTerminalNode(ComplexIfTestToken token) {

    switch (token.type) {
      case IF:
        return new ComplexIfTestIfOperator<>();
      case THEN:
        return new ComplexIfTestThenOperator<>();
      case ELSE:
        return new ComplexIfTestElseOperator<>();
      case GT:
        return new ComplexIfTestGreaterThanOperator<>();
      case LT:
        return new ComplexIfTestLessThanOperator<>();
      case EQUAL:
        return new ComplexIfTestEqualOperator<>();
      case AND:
        return new And<>();
      case OR:
        return new Or<>();
      case NOT:
        return new Not<>();
      default:
        throw new FarserException("Operator type " + token + " not implemented");
    }

  }


}
