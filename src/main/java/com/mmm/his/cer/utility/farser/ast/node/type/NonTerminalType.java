package com.mmm.his.cer.utility.farser.ast.node.type;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public enum NonTerminalType {
  /**
   * A node which produces a return value from its evaluation.
   */
  EXPRESSION(Expression.class),

  /**
   * A node which does not produce a return value from its evaluation (processing).
   */
  STATEMENT(Statement.class);

  private final Class<?> type;

  private NonTerminalType(Class<?> type) {
    this.type = type;
  }

  public Class<?> getType() {
    return type;
  }

}
