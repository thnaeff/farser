package com.mmm.his.cer.utility.farser.ast.node.type;

import com.mmm.his.cer.utility.farser.ast.node.LtrExpressionIterator;
import com.mmm.his.cer.utility.farser.ast.parser.ExpressionParser;

/**
 * Interface for each "terminal" node of the AST to implement. This will allow the evaluation of the
 * entire expression tree through recursion.
 *
 * @param <C> The type of context to be used for the terminal node execution.
 * @param <R> The data type of the evaluation result
 *
 * @author Mike Funaro
 * @author Thomas Naeff
 */
public interface Expression<C, R> extends Iterable<Expression<C, ?>> {

  /**
   * Evaluate an expression returning its value.
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result.
   */
  R evaluate(C context);

  /**
   * Simply adds a null-check to the evaluation result returned from {@link #evaluate(Object)}.<br>
   * For details, see:
   * {@link ExpressionParser#evaluateAsNonNull(Object, java.util.function.Function)}
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result. Never <code>null</code>.
   */
  default R evaluateAsNonNull(C context) {
    return ExpressionParser.evaluateAsNonNull(context, this::evaluate);
  }

  /**
   * Evaluates the expression and returns the evaluation result converted into its specific type (if
   * applicable).<br>
   * For details, see: {@link ExpressionParser#evaluateTyped(Object, java.util.function.Function)}
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result in its specific type. Never <code>null</code>.
   */
  default Object evaluateTyped(C context) {
    return ExpressionParser.evaluateTyped(context, this::evaluate);
  }

  /**
   * Evaluates the result using {@link #evaluateAsNonNull(Object)}, then parses or casts the
   * result.<br>
   * For details, see:
   * {@link ExpressionParser#evaluateAsInteger(Object, java.util.function.Function)}
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result. Never <code>null</code>.
   */
  default Integer evaluateAsInteger(C context) {
    return ExpressionParser.evaluateAsInteger(context, this::evaluate);
  }

  /**
   * Evaluates the result using {@link #evaluateAsNonNull(Object)}, then parses or casts the
   * result<br>
   * For details, see:
   * {@link ExpressionParser#evaluateAsDouble(Object, java.util.function.Function)}
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result. Never <code>null</code>.
   */
  default Double evaluateAsDouble(C context) {
    return ExpressionParser.evaluateAsDouble(context, this::evaluate);
  }

  /**
   * Returns an iterator over the expression elements.<br>
   * <br>
   * For terminal nodes, <code>null</code> should not be returned but an empty iterator can be
   * returned (<code>return new ExpressionIterator<>()</code>, this default implementation).
   */
  @Override
  default LtrExpressionIterator<C> iterator() {
    return new LtrExpressionIterator<>();
  }

  /**
   * Returns a printable representation of the node.
   *
   * @return The printable form of this node
   */
  default String print() {
    return toString();
  }

}
