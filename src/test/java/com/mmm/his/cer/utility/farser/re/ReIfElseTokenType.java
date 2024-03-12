package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.lexer.CommonTokenType;
import com.mmm.his.cer.utility.farser.lexer.TokenType;
import java.util.Optional;

public enum ReIfElseTokenType implements TokenType<ReIfElseTokenType> {

  ATOM(null, CommonTokenType.ATOM),
  SPACE(" ", CommonTokenType.SPACE),

  IF("IF", CommonTokenType.LPAREN),
  THEN("THEN", CommonTokenType.AND),
  ELSE("ELSE", CommonTokenType.OR),
  // ELSEIF("ELSE IF"),
  ENDIF("ENDIF", CommonTokenType.RPAREN);

  private final String value;
  private final CommonTokenType commonType;

  ReIfElseTokenType(String value, CommonTokenType commonType) {
    this.value = value;
    this.commonType = commonType;
  }

  ReIfElseTokenType(String value) {
    this(value, null);
  }

  @Override
  public Optional<String> getValue() {
    return Optional.ofNullable(value);
  }

  @Override
  public Optional<CommonTokenType> getCommonTokenType() {
    return Optional.ofNullable(commonType);
  }
}
