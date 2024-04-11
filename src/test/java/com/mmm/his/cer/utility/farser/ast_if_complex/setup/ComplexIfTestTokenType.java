package com.mmm.his.cer.utility.farser.ast_if_complex.setup;

import com.mmm.his.cer.utility.farser.CommonTokenFlag;
import com.mmm.his.cer.utility.farser.ast.AstCommonTokenType;
import com.mmm.his.cer.utility.farser.ast.AstTokenType;
import com.mmm.his.cer.utility.farser.lexer.CommonTokenType;
import com.mmm.his.cer.utility.farser.lexer.TokenType;
import java.util.Optional;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public enum ComplexIfTestTokenType
implements
TokenType<ComplexIfTestTokenType>,
AstTokenType<ComplexIfTestTokenType> {

  ATOM(null, CommonTokenType.ATOM),
  SPACE(" ", CommonTokenType.SPACE),
  LPAREN("(", AstCommonTokenType.GROUP_START),
  RPAREN(")", AstCommonTokenType.GROUP_END),
  NOT("!", AstCommonTokenType.NOT),

  IF("IF", AstCommonTokenType.GROUP_START),
  THEN("THEN", 5),
  ELSE("ELSE", 6),
  ENDIF("ENDIF", AstCommonTokenType.GROUP_END),

  // Operator precedence: Lower value = stronger bond
  GT(">", 1),
  LT("<", 1),
  EQUAL("=", 2),
  AND("&", 3),
  OR("|", 4);

  private final String value;
  private final CommonTokenFlag commonType;
  private final int operatorPrecedence;

  ComplexIfTestTokenType(String value, CommonTokenFlag commonType) {
    this.value = value;
    this.commonType = commonType;
    this.operatorPrecedence = AstTokenType.NOT_AN_OPERATOR;
  }

  ComplexIfTestTokenType(String value, int operatorPrecedence, CommonTokenFlag commonType) {
    this.value = value;
    this.commonType = commonType;
    this.operatorPrecedence = operatorPrecedence;
  }

  ComplexIfTestTokenType(String value, int operatorPrecedence) {
    this.value = value;
    this.commonType = null;
    this.operatorPrecedence = operatorPrecedence;
  }

  ComplexIfTestTokenType(String value) {
    this(value, null);
  }

  @Override
  public Optional<String> getValue() {
    return Optional.ofNullable(value);
  }

  @Override
  public Optional<CommonTokenFlag> getCommonTokenType() {
    return Optional.ofNullable(commonType);
  }

  @Override
  public int getOperatorPrecedence() {
    return operatorPrecedence;
  }
}
