package com.mmm.his.cer.utility.farser.ast_if_complex.setup.lex;

import com.mmm.his.cer.utility.farser.ast_if_complex.setup.ComplexIfTestTokenType;
import com.mmm.his.cer.utility.farser.lexer.LexerToken;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class ComplexIfTestToken implements LexerToken<ComplexIfTestTokenType> {

  public final ComplexIfTestTokenType type;
  public final String value;

  public ComplexIfTestToken(ComplexIfTestTokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public ComplexIfTestTokenType getType() {
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
