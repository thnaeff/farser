package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class ComplexIfTestGreaterThanOperator<C> extends NonTerminal<C, Integer> {

  @Override
  public Boolean evaluate(C context) {
    return left.evaluateAsDouble(context) > right.evaluateAsDouble(context);
  }

  @Override
  public String print() {
    return "GREATER-THAN";
  }

  @Override
  public String toString() {
    return "GreaterThan{" + "left=" + left + ", right=" + right + '}';
  }
}
