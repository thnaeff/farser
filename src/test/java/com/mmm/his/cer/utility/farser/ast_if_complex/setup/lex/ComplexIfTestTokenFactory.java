package com.mmm.his.cer.utility.farser.ast_if_complex.setup.lex;

import com.mmm.his.cer.utility.farser.ast_if_complex.setup.ComplexIfTestTokenType;
import com.mmm.his.cer.utility.farser.lexer.LexerTokenFactory;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class ComplexIfTestTokenFactory
implements LexerTokenFactory<ComplexIfTestToken, ComplexIfTestTokenType> {

  @Override
  public ComplexIfTestToken create(ComplexIfTestTokenType tokenType, String value) {
    if (tokenType == ComplexIfTestTokenType.SPACE) {
      // Ignore spaces
      return null;
    }

    return new ComplexIfTestToken(tokenType, value);
  }
}
