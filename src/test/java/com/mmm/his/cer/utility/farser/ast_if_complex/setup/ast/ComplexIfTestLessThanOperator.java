package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class ComplexIfTestLessThanOperator<C> extends NonTerminal<C, Double> {

  @Override
  public Boolean evaluate(C context) {
    return left.evaluateAsDouble(context) < right.evaluateAsDouble(context);
  }

  @Override
  public String print() {
    return "LESS-THAN";
  }

  @Override
  public String toString() {
    return "LessThan{" + "left=" + left + ", right=" + right + '}';
  }
}
