package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;
import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminalType;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class ComplexIfTestThenOperator<C> extends NonTerminal<C, Boolean> {

  public enum ThenProcessingResult {
    EXPRESSION_CONSUMED,
    EXPRESSION_NOT_CONSUMED;
  }

  @Override
  public Boolean evaluate(C context) {
    // The left-side node is the if-expression.
    // The right-side node is the then-statement.
    boolean ifStatementResult = left.evaluate(context);
    if (ifStatementResult) {
      // The right-side is a statement. It does not produce a return value.
      // The 'Statement' implementation however always returns 'true' to signal that a code path was
      // hit and evaluated.
      return right.evaluate(context);
    }
    // Return the result of the if-statement so that the parent node can process accordingly.
    return ifStatementResult;
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
    return "IF-THEN";
  }

  @Override
  public String toString() {
    return "Then{" + "left(if)=" + left + ", right(then)=" + right + "}";
  }
}
