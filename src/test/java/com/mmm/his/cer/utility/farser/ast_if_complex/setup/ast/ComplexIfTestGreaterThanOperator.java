package com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.Expression;
import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;

/**
 *
 *
 * @author Thomas Naeff
 *
 * @param <C>
 */
public class ComplexIfTestGreaterThanOperator<C> extends NonTerminal<C, Double> {

  @Override
  public Boolean evaluate(C context) {
    return Expression.handleEvaluation(left, node -> node.evaluateAsDouble(context)) > Expression
        .handleEvaluation(right, node -> node.evaluateAsDouble(context));
  }

  // TODO this operator knows that it needs a Double data type. Could it pass some conversion
  // function into the child nodes at creation time? Would that make sense, even with type erasure?

  @Override
  public String print() {
    return "GREATER-THAN";
  }

  @Override
  public String toString() {
    return "GreaterThan{" + "left=" + left + ", right=" + right + '}';
  }
}
