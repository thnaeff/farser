package com.mmm.his.cer.utility.farser.lexer.tokentype;


import com.mmm.his.cer.utility.farser.lexer.LexerToken;
import com.mmm.his.cer.utility.farser.lexer.TokenType;

/**
 * Token class that can be used to represent the pieces of a lexed string.
 *
 * @author a30w4zz
 */
public class TestLexerToken<T extends TokenType<?>> implements LexerToken<T> {

  public final T type;
  public final String value;

  /**
   * A new token with the value from the {@link TokenType}.
   *
   * @param type The token type
   */
  public TestLexerToken(T type) {
    this.type = type;
    this.value = type.getValue().orElse(null);

  }

  /**
   * A new token with a provided value.
   *
   * @param type  The token type
   * @param value The token value
   */
  public TestLexerToken(T type, String value) {
    this.type = type;
    this.value = value;

  }

  @Override
  public T getType() {
    return type;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return type.toString();
  }
}
