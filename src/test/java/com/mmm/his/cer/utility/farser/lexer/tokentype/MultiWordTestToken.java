package com.mmm.his.cer.utility.farser.lexer.tokentype;

import com.mmm.his.cer.utility.farser.lexer.CommonTokenType;
import com.mmm.his.cer.utility.farser.lexer.TokenType;
import java.util.Optional;

/**
 * A set of tokens with multiple words (separated by space) which overlap and also appear in other
 * tokens.
 *
 */
public enum MultiWordTestToken implements TokenType<MultiWordTestToken> {

  ATOM(
      null,
      CommonTokenType.ATOM),

  /**
   *
   */
  SPACE(
      " ",
      CommonTokenType.SPACE),

  // Just some operands to work with
  AND("&"),
  OR("|"),

  /**
   * "ABC" also appears in {@link #MULTI_WORD}.
   */
  SINGLE_WORD_ABC("ABC"),

  // The order of the single vs the multi word token should not matter. It should correctly always
  // pick the "most accurate" (longest matching) one.

  /**
   * Contains the tokens {@link #SINGLE_WORD_ABC} and {@link #SINGLE_WORD_DEF}, but this combination
   * forms its own token and should be recognized as such.
   */
  MULTI_WORD("ABC DEF"),

  /**
   * "DEF" also appears in {@link #MULTI_WORD}.
   */
  SINGLE_WORD_DEF("DEF");

  private final Optional<String> value;
  private final Optional<CommonTokenType> commonType;

  MultiWordTestToken(String value, CommonTokenType commonType) {
    this.value = Optional.ofNullable(value);
    this.commonType = Optional.ofNullable(commonType);

  }

  MultiWordTestToken(String value) {
    this(value, null);

  }

  @Override
  public Optional<String> getValue() {
    return value;
  }

  @Override
  public Optional<CommonTokenType> getCommonTokenType() {
    return commonType;
  }


}
