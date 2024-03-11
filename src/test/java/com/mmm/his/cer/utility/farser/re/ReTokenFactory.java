package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.lexer.LexerTokenFactory;

public class ReTokenFactory implements LexerTokenFactory<ReToken, ReTokenType> {

  @Override
  public ReToken create(ReTokenType tokenType, String value) {
    if (tokenType == ReTokenType.SPACE) {
      // Ignore spaces
      return null;
    }

    return new ReToken(tokenType, value);
  }
}
