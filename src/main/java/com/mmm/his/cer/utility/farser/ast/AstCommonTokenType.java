package com.mmm.his.cer.utility.farser.ast;

import com.mmm.his.cer.utility.farser.CommonTokenFlag;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.lexer.LexerToken;

/**
 * Common token types for AST ({@link AstDescentParser}) logic.
 *
 * @author Thomas Naeff
 *
 */
public enum AstCommonTokenType implements CommonTokenFlag {

  /**
   * A left parenthesis "(".
   *
   * @deprecated Use {@link #GROUP_START} instead.
   */
  @Deprecated
  LPAREN,

  /**
   * A right parenthesis ")".
   *
   * @deprecated Use {@link #GROUP_END} instead.
   */
  @Deprecated
  RPAREN,

  /**
   * The start of an (expression) group, which will be followed by a {@link #GROUP_END} at some
   * point. For example an opening parenthesis "(" or an if-block "IF".
   */
  GROUP_START,

  /**
   * The end of an (expression) group, which was preceded by a {@link #GROUP_START} at some point.
   * For example a closing parenthesis ")" or the end of an if-block "ENDIF".
   */
  GROUP_END,

  /**
   * A negation/not.
   *
   */
  NOT,

  /**
   * An unary operator like "IF", etc. with only a left-side expression.
   */
  UNARY,

  /**
   * An "AND" operator.<br>
   * <br>
   * This flag exists for backwards compatibility when a {@link NodeSupplier} is used without the
   * {@link NodeSupplier#createNonTerminalNode(LexerToken)} method being overridden.
   *
   * @deprecated Instead of using this flag, it is preferred to override
   *             {@link NodeSupplier#createNonTerminalNode(LexerToken)} and create the node
   *             instances based on your custom token types.
   */
  @Deprecated
  AND,

  /**
   * An "OR" operator.<br>
   * <br>
   * This flag exists for backwards compatibility when a {@link NodeSupplier} is used without the
   * {@link NodeSupplier#createNonTerminalNode(LexerToken)} method being overridden.
   *
   * @deprecated Instead of using this flag, it is preferred to override
   *             {@link NodeSupplier#createNonTerminalNode(LexerToken)} and create the node
   *             instances based on your custom token types.
   */
  @Deprecated
  OR;

}
