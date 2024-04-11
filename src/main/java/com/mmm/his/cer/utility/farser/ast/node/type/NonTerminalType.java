package com.mmm.his.cer.utility.farser.ast.node.type;

import com.mmm.his.cer.utility.farser.lexer.LexerToken;

/**
 * A type to be returned in {@link NonTerminal#getLeftType()}/{@link NonTerminal#getRightType()} to
 * determine which {@link NodeSupplier} method is called to create a new node.
 *
 * @author Thomas Naeff
 *
 */
public enum NonTerminalType {
  /**
   * A node which produces a return value from its evaluation.<br>
   * Setting this type will result in calling {@link NodeSupplier#createNode(LexerToken)}.
   */
  EXPRESSION,

  /**
   * A node which does not produce a return value from its evaluation (processing).<br>
   * Setting this type will result in calling {@link NodeSupplier#createStatement(LexerToken)}.
   */
  STATEMENT;
}
