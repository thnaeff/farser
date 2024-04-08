package com.mmm.his.cer.utility.farser.ast_if;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import com.mmm.his.cer.utility.farser.ast_complex.setup.ComplexTestTokenType;
import com.mmm.his.cer.utility.farser.ast_complex.setup.lex.ComplexTestToken;
import com.mmm.his.cer.utility.farser.ast_complex.setup.lex.ComplexTestTokenFactory;
import com.mmm.his.cer.utility.farser.lexer.Lexer;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ReScriptLexerTest {

  private static final ComplexTestTokenFactory factory = new ComplexTestTokenFactory();

  @Test
  public void simpleGreaterThan() throws Exception {
    String input = "IF A > 5 THEN RESULT = 10 ENDIF";

    List<ComplexTestToken> tokens = Lexer.lex(ComplexTestTokenType.class, input, factory);

    List<String> tokensAsString =
        tokens.stream().map(ComplexTestToken::toString).collect(Collectors.toList());
    assertThat(tokensAsString, contains("IF:IF", "ATOM:A", "GT:>", "ATOM:5", "THEN:THEN",
        "ATOM:RESULT", "EQUAL:=", "ATOM:10", "ENDIF:ENDIF"));
  }

  @Test
  public void twoWordTokenWithOneWordAmbiguity() throws Exception {
    // Token "IN TABLE" could also get recognized as "IN", but it should get resolved as "IN TABLE"
    String input = "IF A IN TABLE abc THEN RESULT = 10 ENDIF";

    List<ComplexTestToken> tokens = Lexer.lex(ComplexTestTokenType.class, input, factory);

    List<String> tokensAsString =
        tokens.stream().map(ComplexTestToken::toString).collect(Collectors.toList());
    assertThat(tokensAsString,
        contains("IF:IF", "ATOM:A", "IN_TABLE:IN TABLE", "ATOM:abc", "THEN:THEN",
            "ATOM:RESULT", "EQUAL:=", "ATOM:10", "ENDIF:ENDIF"));
  }

  @Test
  public void singleWordTokenWithTwoWordAmbiguity() throws Exception {
    // Token "IN" also exists as "IN TABLE", but it should get resolved as "IN"
    String input = "IF A IN abc THEN "
        + "RESULT = 10 "
        + "ENDIF";

    List<ComplexTestToken> tokens = Lexer.lex(ComplexTestTokenType.class, input, factory);

    List<String> tokensAsString =
        tokens.stream().map(ComplexTestToken::toString).collect(Collectors.toList());
    assertThat(tokensAsString,
        contains("IF:IF", "ATOM:A", "IN:IN", "ATOM:abc", "THEN:THEN",
            "ATOM:RESULT", "EQUAL:=", "ATOM:10", "ENDIF:ENDIF"));
  }

  @Test
  public void listWithParenthesis() throws Exception {
    // Token "IN" also exists as "IN TABLE", but it should get resolved as "IN"
    String input = "IF A IN (abc, def, ghi) THEN "
        + "RESULT = 10 "
        + "ENDIF";

    List<ComplexTestToken> tokens = Lexer.lex(ComplexTestTokenType.class, input, factory);

    List<String> tokensAsString =
        tokens.stream().map(ComplexTestToken::toString).collect(Collectors.toList());
    assertThat(tokensAsString,
        contains("IF:IF", "ATOM:A", "IN:IN",
            "LPAREN:(", "ATOM:abc", "COMMA:,", "ATOM:def", "COMMA:,", "ATOM:ghi", "RPAREN:)",
            "THEN:THEN", "ATOM:RESULT", "EQUAL:=", "ATOM:10", "ENDIF:ENDIF"));
  }

}
