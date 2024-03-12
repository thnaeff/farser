package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.lexer.LexerToken;

public class ReIfElseToken implements LexerToken<ReIfElseTokenType> {

  public final ReIfElseTokenType type;
  public final String value;

  public ReIfElseToken(ReIfElseTokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public ReIfElseTokenType getType() {
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
