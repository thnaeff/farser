package com.mmm.his.cer.utility.farser.ast.parser;

import com.mmm.his.cer.utility.farser.ast.node.type.Expression;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some parser tools for {@link Expression#evaluate(Object)} evaluation.
 *
 * @author Thomas Naeff
 *
 */
public final class ExpressionParser {

  private ExpressionParser() {
    // Private. Only static methods.
  }

  private static final String DIGITS = "(\\p{Digit}+)";
  private static final String HEX_DIGITS = "(\\p{XDigit}+)";
  // an exponent is 'e' or 'E' followed by an optionally
  // signed decimal integer.
  private static final String EXP = "[eE][+-]?" + DIGITS;

  /**
   * A regex to test if a string is a double or float. As documented by
   * {@link Double#valueOf(String)} and {@link Float#valueOf(String)}.
   */
  private static final String FP_REGEX =
      ("[\\x00-\\x20]*"
          + // Optional leading "whitespace"
          "[+-]?("
          + // Optional sign character
          "NaN|"
          + // "NaN" string
          "Infinity|"
          + // "Infinity" string

          // A decimal floating-point string representing a finite positive
          // number without a leading sign has at most five basic pieces:
          // Digits . Digits ExponentPart FloatTypeSuffix
          //
          // Since this method allows integer-only strings as input
          // in addition to strings of floating-point literals, the
          // two sub-patterns below are simplifications of the grammar
          // productions from section 3.10.2 of
          // The Java™ Language Specification.

          // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
          "((("
          + DIGITS
          + "(\\.)?("
          + DIGITS
          + "?)("
          + EXP
          + ")?)|"
          +

          // . Digits ExponentPart_opt FloatTypeSuffix_opt
          "(\\.("
          + DIGITS
          + ")("
          + EXP
          + ")?)|"
          +

          // Hexadecimal strings
          "(("
          +
          // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
          "(0[xX]"
          + HEX_DIGITS
          + "(\\.)?)|"
          +

          // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
          "(0[xX]"
          + HEX_DIGITS
          + "?(\\.)"
          + HEX_DIGITS
          + ")"
          +

          ")[pP][+-]?"
          + DIGITS
          + "))"
          +
          "[fFdD]?))"
          +
          "[\\x00-\\x20]*"); // Optional trailing "whitespace"

  private static final Pattern IS_DOUBLE_OR_FLOAT = Pattern.compile(FP_REGEX);

  /**
   * Regex to match an integer, as documented in {@link Integer#parseInt(String)}.<br>
   * <br>
   * [-+]? Matches the plus or minus character (Optional)<br>
   * \d+ Matches 1 or more digit characters<br>
   * Start and end metacharacters <code>^$</code> are optional, but included for when it is not used
   * in {@link Matcher#matches()}.
   *
   */
  private static final Pattern IS_INTEGER = Pattern.compile("^[-+]?\\d+$");

  /**
   * Checks if the string is a valid double or floating point number.<br>
   * Note: This also matches integers since those are also a valid double/float.
   *
   * @param str The string to check
   * @return Whether it is a valid double/float
   */
  public static boolean isDoubleOrFloat(String str) {
    return IS_DOUBLE_OR_FLOAT.matcher(str).matches();
  }

  /**
   * Checks if the string is a valid integer number, as documented in
   * {@link Integer#parseInt(String)}.
   *
   * @param str The string to check
   * @return Whether it is a valid integer
   */
  public static boolean isInteger(String str) {
    return IS_INTEGER.matcher(str).matches();
  }

  /**
   * Simply adds a null-check to the evaluation result returned from {@link #evaluate(Object)}.
   *
   * @param <C>     The type of context to pass in to the evaluation method.
   * @param <R>     The data type of the evaluation result
   * @param context The context that will be used in the evaluation of the node.
   * @param eval    The {@link Expression#evaluate(Object)} function
   * @return The expression result. Never <code>null</code>.
   */
  public static <C, R> R evaluateAsNonNull(C context, Function<C, R> eval) {
    R evaluated = eval.apply(context);
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
   * @param <C>     The type of context to pass in to the evaluation method.
   * @param <R>     The data type of the evaluation result
   * @param context The context that will be used in the evaluation of the node.
   * @param eval    The {@link Expression#evaluate(Object)} function
   * @return The expression result in its specific type. Never <code>null</code>.
   */
  public static <C, R> Object evaluateTyped(C context, Function<C, R> eval) {
    R evaluated = evaluateAsNonNull(context, eval);
    if (evaluated instanceof String) {
      String str = (String) evaluated;
      if (str.startsWith("\"") && str.endsWith("\"")) {
        // Trim quoutes
        str = str.substring(1, str.length() - 1);
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
   * @param <C>     The type of context to pass in to the evaluation method.
   * @param <R>     The data type of the evaluation result
   * @param context The context that will be used in the evaluation of the node.
   * @param eval    The {@link Expression#evaluate(Object)} function
   * @return The expression result. Never <code>null</code>.
   */
  public static <C, R> Integer evaluateAsInteger(C context, Function<C, R> eval) {
    R evaluated = evaluateAsNonNull(context, eval);
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
   * @param <C>     The type of context to pass in to the evaluation method.
   * @param <R>     The data type of the evaluation result
   * @param context The context that will be used in the evaluation of the node.
   * @param eval    The {@link Expression#evaluate(Object)} function
   * @return The expression result. Never <code>null</code>.
   */
  public static <C, R> Double evaluateAsDouble(C context, Function<C, R> eval) {
    R evaluated = evaluateAsNonNull(context, eval);
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

}
