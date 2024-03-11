package com.mmm.his.cer.utility.farser.lexer.tokentype;

import com.mmm.his.cer.utility.farser.lexer.CommonTokenType;
import com.mmm.his.cer.utility.farser.lexer.LexerTokenFactory;
import com.mmm.his.cer.utility.farser.lexer.TokenType;

/**
 * The factory which creates a {@link TestLexerToken}.
 *
 * @author a5rn0zz
 */
public class TestTokenFactory<T extends TokenType<?>>
    implements LexerTokenFactory<TestLexerToken<T>, T> {

  private final boolean skipSpaces;

  public TestTokenFactory() {
    this.skipSpaces = false;
  }

  public TestTokenFactory(boolean skipSpaces) {
    this.skipSpaces = skipSpaces;
  }

  @Override
  public TestLexerToken<T> create(T tokenType, String value) {
    if (this.skipSpaces && tokenType.getCommonTokenType().orElse(null) == CommonTokenType.SPACE) {
      // Ignore spaces
      return null;
    }

    return new TestLexerToken<>(tokenType, value);
  }


}
