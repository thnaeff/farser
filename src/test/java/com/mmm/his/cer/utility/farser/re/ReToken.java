package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.lexer.LexerToken;

public class ReToken implements LexerToken<ReTokenType> {

  public final ReTokenType type;
  public final String value;

  public ReToken(ReTokenType type, String value) {
    this.type = type;
    this.value = value;
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
