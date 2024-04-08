package com.mmm.his.cer.utility.farser.ast_if.setup;

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
public enum IfTestTokenType
implements
TokenType<IfTestTokenType>,
AstTokenType<IfTestTokenType> {

  ATOM(null, CommonTokenType.ATOM),
  SPACE(" ", CommonTokenType.SPACE),
  LPAREN("(", AstCommonTokenType.LPAREN),
  RPAREN(")", AstCommonTokenType.RPAREN),

  IF("IF", AstCommonTokenType.UNARY),
  THEN("THEN", 1),
  ELSE("ELSE", 2),
  // ELSEIF("ELSE IF", 3),
  ENDIF("ENDIF", 4);

  private final String value;
  private final CommonTokenFlag commonType;
  private final int operatorPrecedence;

  IfTestTokenType(String value, CommonTokenFlag commonType) {
    this.value = value;
    this.commonType = commonType;
    this.operatorPrecedence = AstTokenType.NOT_AN_OPERATOR;
  }

  IfTestTokenType(String value, int operatorPrecedence, CommonTokenFlag commonType) {
    this.value = value;
    this.commonType = commonType;
    this.operatorPrecedence = operatorPrecedence;
  }

  IfTestTokenType(String value, int operatorPrecedence) {
    this.value = value;
    this.commonType = null;
    this.operatorPrecedence = operatorPrecedence;
  }

  IfTestTokenType(String value) {
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
