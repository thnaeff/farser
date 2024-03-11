package com.mmm.his.cer.utility.farser.ast.node.type;

import com.mmm.his.cer.utility.farser.lexer.LexerToken;
import com.mmm.his.cer.utility.farser.lexer.TokenType;

public interface CompositeToken<L extends LexerToken<T>, T extends TokenType<?>>
extends LexerToken<T> {

  // private final L leftOperand;
  // private final L operator;
  // private final L rightOperand;
  //
  // protected CompositeToken(L leftOperand, L operator, L rightOperand) {
  // this.leftOperand = Objects.requireNonNull(leftOperand, "Left-hand operand may not be NULL");
  // this.operator = Objects.requireNonNull(operator, "Operator may not be NULL");
  // this.rightOperand = Objects.requireNonNull(rightOperand, "Right-hand operand may not be NULL");
  // }

  public L getLeftOperand();

  public L getOperator();

  public L getRightOperand();

  public static <L extends LexerToken<T>, T extends TokenType<?>> String value(L leftOperand,
      L operator, L rightOperand) {
    StringBuilder sb = new StringBuilder();
    sb.append(leftOperand.getValue());
    sb.append(" ");
    sb.append(operator.getValue());
    sb.append(" ");
    sb.append(rightOperand.getValue());
    return sb.toString();
  }

}
