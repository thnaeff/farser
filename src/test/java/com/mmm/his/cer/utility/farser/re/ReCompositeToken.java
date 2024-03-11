package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.ast.node.type.CompositeToken;
import java.util.Objects;

public class ReCompositeToken extends ReToken implements CompositeToken<ReToken, ReTokenType>
{

  private final ReToken leftOperand;
  private final ReToken operator;
  private final ReToken rightOperand;

  public ReCompositeToken(ReToken leftOperand, ReToken operator, ReToken rightOperand) {
    super(operator.getType(), CompositeToken.value(leftOperand, operator, rightOperand));
    this.leftOperand = Objects.requireNonNull(leftOperand, "Left-hand operand may not be NULL");
    this.operator = Objects.requireNonNull(operator, "Operator may not be NULL");
    this.rightOperand = Objects.requireNonNull(rightOperand, "Right-hand operand may not be NULL");
  }

  @Override
  public ReToken getLeftOperand() {
    return leftOperand;
  }

  @Override
  public ReToken getOperator() {
    return operator;
  }

  @Override
  public ReToken getRightOperand() {
    return rightOperand;
  }

  @Override
  public ReTokenType getType() {
    return type;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return type + ":" + value;
  }

}
