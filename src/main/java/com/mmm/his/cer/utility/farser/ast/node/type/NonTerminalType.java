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
  EXPRESSION(Expression.class),

  /**
   * A node which does not produce a return value from its evaluation (processing).<br>
   * Setting this type will result in calling {@link NodeSupplier#createStatement(LexerToken)}.
   */
  STATEMENT(Statement.class);

  private final Class<?> type;

  private <L extends LexerToken<?>, C> NonTerminalType(Class<?> type) {
    this.type = type;
  }

  public Class<?> getType() {
    return type;
  }

}
