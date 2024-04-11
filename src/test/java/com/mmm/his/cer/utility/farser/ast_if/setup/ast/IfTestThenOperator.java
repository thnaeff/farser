package com.mmm.his.cer.utility.farser.ast_if.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class IfTestThenOperator<C> extends NonTerminal<C, Boolean> {

  public enum ThenProcessingResult {
    EXPRESSION_CONSUMED,
    EXPRESSION_NOT_CONSUMED;
  }

  @Override
  public Boolean evaluate(C context) {
    // The left-side expression is the if-statement.
    // The right-side expression is the then-statement.
    boolean ifStatementResult = left.evaluate(context);
    if (ifStatementResult) {
      // The return value of the enclosing expression is not relevant.
      right.evaluate(context);
    }
    // Return the result of the if-statement so that the parent node can process accordingly.
    return ifStatementResult;
  }

  @Override
  public String print() {
    return "THEN";
  }

  @Override
  public String toString() {
    return "Then{" + "left(if)=" + left + ", right(then)=" + right + "}";
  }
}
