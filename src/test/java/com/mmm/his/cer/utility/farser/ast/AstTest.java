package com.mmm.his.cer.utility.farser.ast;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.ast.parser.DescentParser;
import com.mmm.his.cer.utility.farser.ast.setup.MaskedContext;
import com.mmm.his.cer.utility.farser.ast.setup.StringOperandSupplier;
import com.mmm.his.cer.utility.farser.ast.setup.TestTokenFactoryWithAllTokens;
import com.mmm.his.cer.utility.farser.lexer.CommonTokenType;
import com.mmm.his.cer.utility.farser.lexer.DrgFormulaLexer;
import com.mmm.his.cer.utility.farser.lexer.Lexer;
import com.mmm.his.cer.utility.farser.lexer.drg.DrgFormulaToken;
import com.mmm.his.cer.utility.farser.lexer.drg.DrgLexerToken;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class AstTest {


  private static final NodeSupplier<DrgLexerToken, MaskedContext<String>> defaultNodeSupplier =
      new StringOperandSupplier();

  @Test
  public void testStartWithNegation() {

    List<DrgLexerToken> lexerTokens = DrgFormulaLexer.lex("~A | B");
    DescentParser<MaskedContext<String>> parser = new DescentParser<>(lexerTokens.listIterator(),
        new StringOperandSupplier(), Collections.emptyMap());

    AbstractSyntaxTree<MaskedContext<String>> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    // System.out.println(printed);
    assertThat(lines, is(new String[] {
        "OR",
        "  NOT",
        "    A",
        "  B"
    }));

  }

  @Test
  public void testWithSpaceTokensIncluded() {
    // A factory which creates tokens of all types and does not skip SPACE tokens for example.
    // To ensure the AST parser does not require filterd tokens.
    TestTokenFactoryWithAllTokens factory = new TestTokenFactoryWithAllTokens();

    String input = "A & B | C";
    List<DrgLexerToken> tokens = Lexer.lex(DrgFormulaToken.class, input, factory);

    // Ensure that the precondition is met
    boolean hasSpaceToken = tokens.stream()
        .filter(token -> token.getCommonType().orElse(null) == CommonTokenType.SPACE)
        .findAny()
        .isPresent();
    assertTrue(hasSpaceToken);

    AstDescentParser<DrgLexerToken, DrgFormulaToken, MaskedContext<String>> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<MaskedContext<String>> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    System.out.println(printed);
    assertThat(lines, is(new String[] {
        "OR",
        "  AND",
        "    A",
        "    B",
        "  C"
    }));

  }

}
