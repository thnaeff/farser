package com.mmm.his.cer.utility.farser.ast_if.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class IfTestElseOperator<C> extends NonTerminal<C, Boolean> {

  @Override
  public Boolean evaluate(C context) {
    // The left-side expression is the then-statement.
    // The right-side expression is the else-statement.
    boolean thenExpressionHasBeenUsed = left.evaluate(context);
    if (!thenExpressionHasBeenUsed) {
      // Return the result of the enclosed statement so that the parent node can process
      // accordingly.
      return right.evaluate(context);
    }
    // Return the result of the enclosed statement so that the parent node can process accordingly.
    return thenExpressionHasBeenUsed;
  }

  @Override
  public String print() {
    return "ELSE";
  }

  @Override
  public String toString() {
    return "Else{" + "left=" + left + ", right=" + right + "}";
  }
}
