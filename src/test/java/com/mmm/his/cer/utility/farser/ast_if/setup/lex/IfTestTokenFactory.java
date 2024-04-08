package com.mmm.his.cer.utility.farser.ast_if.setup.lex;

import com.mmm.his.cer.utility.farser.ast_if.setup.IfTestTokenType;
import com.mmm.his.cer.utility.farser.lexer.LexerTokenFactory;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class IfTestTokenFactory
    implements LexerTokenFactory<IfTestToken, IfTestTokenType> {

  @Override
  public IfTestToken create(IfTestTokenType tokenType, String value) {
    if (tokenType == IfTestTokenType.SPACE) {
      // Ignore spaces
      return null;
    }

    return new IfTestToken(tokenType, value);
  }
}
