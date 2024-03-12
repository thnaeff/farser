package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.lexer.LexerTokenFactory;

public class ReIfElseTokenFactory implements LexerTokenFactory<ReIfElseToken, ReIfElseTokenType> {

  @Override
  public ReIfElseToken create(ReIfElseTokenType tokenType, String value) {
    if (tokenType == ReIfElseTokenType.SPACE) {
      // Ignore spaces
      return null;
    }

    return new ReIfElseToken(tokenType, value);
  }
}
