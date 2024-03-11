package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.lexer.CommonTokenType;
import com.mmm.his.cer.utility.farser.lexer.TokenType;
import java.util.Optional;

public enum ReTokenType implements TokenType<ReTokenType> {

  ATOM(null, CommonTokenType.ATOM),
  SPACE(" ", CommonTokenType.SPACE),
  LPAREN("(", CommonTokenType.LPAREN),
  RPAREN(")", CommonTokenType.RPAREN),
  COMMA(","),

  IF("IF"),
  THEN("THEN"),
  ELSE("ELSE"),
  END("END"),

  GT(">"),
  LT("<"),
  EQUAL("="),
  AND("&", CommonTokenType.AND),
  OR("|", CommonTokenType.OR);

  // IN("IN"),
  // IN_TABLE("IN TABLE");

  private final String value;
  private final CommonTokenType commonType;

  ReTokenType(String value, CommonTokenType commonType) {
    this.value = value;
    this.commonType = commonType;
  }

  ReTokenType(String value) {
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
