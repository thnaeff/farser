package com.mmm.his.cer.utility.farser.lexer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmm.his.cer.utility.farser.lexer.tokentype.MultiWordTestToken;
import com.mmm.his.cer.utility.farser.lexer.tokentype.TestLexerToken;
import com.mmm.his.cer.utility.farser.lexer.tokentype.TestTokenFactory;
import java.util.List;
import org.junit.Test;

public class MultiWordTokenTest {

  private static final LexerTokenFactory<TestLexerToken<MultiWordTestToken>, MultiWordTestToken> factory =
      new TestTokenFactory<>(true);

  @Test
  public void testMixedOrdersOfMultiAndSingleWords() throws Exception {
    String input = "ABC & DEF | ABC DEF & DEF & ABC";
    List<TestLexerToken<MultiWordTestToken>> lex =
        Lexer.lex(MultiWordTestToken.class, input, factory);

    System.out.println();
    System.out.println("Input: "
        + input);
    System.out.println("Lexed: "
        + lex);

    // Rather than checking equality on lists, make sure values from Lex are what we expect

    int index = 0;

    assertThat(lex.get(index).getType(), is(MultiWordTestToken.SINGLE_WORD_ABC));
    assertThat(lex.get(index).getValue(), is("ABC"));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.AND));
    assertThat(lex.get(index).getValue(),
        is(MultiWordTestToken.AND.getValue().orElseThrow(Exception::new)));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.SINGLE_WORD_DEF));
    assertThat(lex.get(index).getValue(), is("DEF"));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.OR));
    assertThat(lex.get(index).getValue(),
        is(MultiWordTestToken.OR.getValue().orElseThrow(Exception::new)));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.MULTI_WORD));
    assertThat(lex.get(index).getValue(), is("ABC DEF"));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.AND));
    assertThat(lex.get(index).getValue(),
        is(MultiWordTestToken.AND.getValue().orElseThrow(Exception::new)));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.SINGLE_WORD_DEF));
    assertThat(lex.get(index).getValue(), is("DEF"));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.AND));
    assertThat(lex.get(index).getValue(),
        is(MultiWordTestToken.AND.getValue().orElseThrow(Exception::new)));

    index++;
    assertThat(lex.get(index).getType(), is(MultiWordTestToken.SINGLE_WORD_ABC));
    assertThat(lex.get(index).getValue(), is("ABC"));

    assertThat(lex.size(), is(index + 1));
  }

}
