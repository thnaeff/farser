package com.mmm.his.cer.utility.farser.ast_if.setup.ast;

import com.mmm.his.cer.utility.farser.ast.node.LtrExpressionIterator;
import com.mmm.his.cer.utility.farser.ast.node.type.BooleanNonTerminal;
import com.mmm.his.cer.utility.farser.ast.node.type.Expression;

public class IfTestIfOperator<C> extends BooleanNonTerminal<C> {

  @Override
  public void setLeft(Expression<C, Boolean> left) {
    super.setLeft(left);
  }

  @Override
  public void setRight(Expression<C, Boolean> right) {
    throw new UnsupportedOperationException("Can only set the left-side child for IF nodes");
  }

  @Override
  public Boolean evaluate(C context) {
    // Only the left-side expression is populated, which is the content of the if-statement.
    // Return the evaluation result of the if-statement.
    return left.evaluate(context);
  }

  @Override
  public LtrExpressionIterator<C> iterator() {
    return new LtrExpressionIterator<>(left);
  }

  @Override
  public String print() {
    return "IF";
  }

  @Override
  public String toString() {
    return "If{" + "left=" + left + "}";
  }
}
