package com.mmm.his.cer.utility.farser.ast.node.type;

import com.mmm.his.cer.utility.farser.ast.node.LtrExpressionIterator;
import com.mmm.his.cer.utility.farser.lexer.FarserException;
import java.util.function.Function;

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
   * Simply adds a null-check to the evaluation result returned from {@link #evaluate(Object)}.
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result. Never <code>null</code>.
   */
  default R evaluateAsNonNull(C context) {
    R evaluated = evaluate(context);

    if (evaluated == null) {
      throw new FarserException("The expression evaluation returned NULL. Not allowed.");
    }

    return evaluated;
  }

  /**
   * Evaluates the result using {@link #evaluateAsNonNull(Object)}, then converts (or casts) the
   * result to an {@link Integer} data type.
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result. Never <code>null</code>.
   */
  default Integer evaluateAsInteger(C context) {
    R evaluated = evaluateAsNonNull(context);
    if (evaluated instanceof String) {
      return Integer.parseInt((String) evaluated);
    } else {
      // Fall-through -> let it fail if it does not match
      return (Integer) evaluated;
    }
  }

  /**
   * Evaluates the result using {@link #evaluateAsNonNull(Object)}, then converts (or casts) the
   * result to a {@link Double} data type.
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result. Never <code>null</code>.
   */
  default Double evaluateAsDouble(C context) {
    R evaluated = evaluateAsNonNull(context);
    if (evaluated instanceof String) {
      return Double.parseDouble((String) evaluated);
    } else if (evaluated instanceof Integer) {
      return Double.valueOf((Integer) evaluated);
    } else {
      // Fall-through -> let it fail if it does not match
      return (Double) evaluated;
    }
  }

  /**
   * A little helper to call the evaluation function. This helper wraps the evaluation in a
   * try-catch for better error reporting on failed expressions.
   *
   * @param expression         The node to evaluate
   * @param evaluationFunction The evaluation function to call on the node
   * @return The evaluation return data
   */
  public static <C, R, E extends Expression<C, R>> R handleEvaluation(E expression,
      Function<E, R> evaluationFunction) {
    try {
      return evaluationFunction.apply(expression);
    } catch (Exception exc) {
      throw new FarserException("Failed to evaluate expression: " + expression, exc);
    }
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
