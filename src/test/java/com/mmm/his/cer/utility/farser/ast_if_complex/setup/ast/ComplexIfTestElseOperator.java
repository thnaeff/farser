package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.Expression;
import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;
import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminalType;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class ComplexIfTestElseOperator<C> extends NonTerminal<C, Boolean> {

  @Override
  public Boolean evaluate(C context) {
    // The left-side node is the if-then-expression.
    // The right-side node is the else-statement.
    boolean thenExpressionHasBeenUsed =
        Expression.handleEvaluation(left, node -> node.evaluate(context));
    if (!thenExpressionHasBeenUsed) {
      // The right-side is a statement. It does not produce a return value.
      // The 'Statement' implementation however always returns 'true' to signal that a code path was
      // hit and evaluated.
      return Expression.handleEvaluation(right, node -> node.evaluate(context));
    }
    // Return the result of the enclosed statement so that the parent node can process accordingly.
    return thenExpressionHasBeenUsed;
  }

  @Override
  public NonTerminalType getLeftType() {
    return NonTerminalType.EXPRESSION;
  }

  @Override
  public NonTerminalType getRightType() {
    return NonTerminalType.STATEMENT;
  }

  @Override
  public String print() {
    return "THEN-ELSE";
  }

  @Override
  public String toString() {
    return "Else{" + "left=" + left + ", right=" + right + "}";
  }
}
