package com.mmm.his.cer.utility.farser.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.BooleanExpression;
import com.mmm.his.cer.utility.farser.ast.node.type.LtrExpressionIterator;
import com.mmm.his.cer.utility.farser.ast.node.type.NonTerminal;
import com.mmm.his.cer.utility.farser.ast.parser.ExpressionResult;

/**
 * Class that wraps a {@link BooleanExpression} and provides methods to evaluate it.
 *
 * @param <T> the type of context
 * @author Mike Funaro
 */
public class DrgSyntaxTree<T> extends NonTerminal<T> implements Iterable<BooleanExpression<T>> {

  private BooleanExpression<T> ast;

  public DrgSyntaxTree(BooleanExpression<T> ast) {
    this.ast = ast;
  }

  public void setAst(BooleanExpression<T> ast) {
    this.ast = ast;
  }

  @Override
  public boolean evaluate(T context) {
    return this.ast.evaluate(context);
  }

  /**
   * Evaluate an expression that was previously built by the parser.
   *
   * @param context the context object that will be used in the evaluation.
   * @return {@link ExpressionResult} ExpressionResult object which will have the data about the
   *         outcome of the evaluation.
   */
  public ExpressionResult<T> evaluateExpression(T context) {
    boolean evaluate = evaluate(context);
    return new ExpressionResult<>(evaluate, context);
  }

  @Override
  public LtrExpressionIterator<T> iterator() {
    return this.ast.iterator();
  }

  @Override
  public String print() {
    return this.ast.print();
  }

}
