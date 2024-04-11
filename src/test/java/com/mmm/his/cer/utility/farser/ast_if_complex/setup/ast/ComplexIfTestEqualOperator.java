package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class ComplexIfTestEqualOperator<C> extends NonTerminal<C, Object> {

  @Override
  public Boolean evaluate(C context) {
    Object leftResult = left.evaluateTyped(context);
    Object rightResult = right.evaluateTyped(context);
    // NPE save 'equals'
    return leftResult == null ? leftResult == rightResult : leftResult.equals(rightResult);
  }

  @Override
  public String print() {
    return "EQUAL";
  }

  @Override
  public String toString() {
    return "Equal{" + "left=" + left + ", right=" + right + '}';
  }
}
