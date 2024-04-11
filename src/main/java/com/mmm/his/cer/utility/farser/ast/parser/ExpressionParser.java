package com.mmm.his.cer.utility.farser.ast.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
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
          "[\\x00-\\x20]*");// Optional trailing "whitespace"

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

}
