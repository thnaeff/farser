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
   * Simply adds a null-check to the evaluation result returned from {@link #evaluate(Object)}.
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result. Never <code>null</code>.
   */
  default R evaluateAsNonNull(C context) {
    R evaluated = evaluate(context);
    if (evaluated == null) {
      throw new NullPointerException("The expression evaluation returned NULL. Not allowed.");
    }
    return evaluated;
  }

  /**
   * Evaluates the expression and returns the evaluation result converted into its specific type (if
   * applicable).<br>
   * If the evaluation return data is a {@link String}, it will do the following:
   * <ul>
   * <li>If the string is double-quoted with <code>"</code>, it returns the result as-is as
   * {@link String}</li>
   * <li>If the string is an integer, it parses and returns it as {@link Integer}</li>
   * <li>If the string is a double or float, it parses and returns it as {@link Double}</li>
   * </ul>
   *
   * @param context The context that will be used in the evaluation of the node.
   * @return The expression result in its specific type. Never <code>null</code>.
   */
  default Object evaluateTyped(C context) {
    R evaluated = evaluateAsNonNull(context);
    if (evaluated instanceof String) {
      String str = (String) evaluated;
      if (str.startsWith("\"") && str.endsWith("\"")) {
        return str;
      } else if (ExpressionParser.isInteger(str)) {
        return Integer.valueOf(str);
      } else if (ExpressionParser.isDoubleOrFloat(str)) {
        return Double.valueOf(str);
      }
    }
    return evaluated;
  }

  /**
   * Evaluates the result using {@link #evaluateAsNonNull(Object)}, then parses or casts the result
   * as {@link Integer} data type.
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
   * Evaluates the result using {@link #evaluateAsNonNull(Object)}, then parses or casts the result
   * as {@link Double} data type. {@link Integer} and {@link Float} also get converted to a
   * {@link Double}.
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
    } else if (evaluated instanceof Float) {
      return ((Float) evaluated).doubleValue();
    } else {
      // Fall-through -> let it fail if it does not match
      return (Double) evaluated;
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
