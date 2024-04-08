package com.mmm.his.cer.utility.farser.ast_if.setup.lex;

import com.mmm.his.cer.utility.farser.ast_if.setup.IfTestTokenType;
import com.mmm.his.cer.utility.farser.lexer.LexerToken;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class IfTestToken implements LexerToken<IfTestTokenType> {

  public final IfTestTokenType type;
  public final String value;

  public IfTestToken(IfTestTokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public IfTestTokenType getType() {
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
