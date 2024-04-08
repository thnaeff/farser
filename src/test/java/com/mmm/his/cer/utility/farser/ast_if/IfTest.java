package com.mmm.his.cer.utility.farser.ast_if;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTree;
import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTreePrinter;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.ast_if.setup.IfTestTokenType;
import com.mmm.his.cer.utility.farser.ast_if.setup.ast.IfTestAstContext;
import com.mmm.his.cer.utility.farser.ast_if.setup.ast.IfTestAstNodeSupplier;
import com.mmm.his.cer.utility.farser.ast_if.setup.lex.IfTestToken;
import com.mmm.his.cer.utility.farser.ast_if.setup.lex.IfTestTokenFactory;
import com.mmm.his.cer.utility.farser.lexer.Lexer;
import java.util.List;
import org.junit.Test;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class IfTest {

  private static final IfTestTokenFactory factory = new IfTestTokenFactory();
  private static final NodeSupplier<IfTestToken, IfTestAstContext> defaultNodeSupplier =
      new IfTestAstNodeSupplier();

  @Test
  public void testIfThen() throws Exception {
    String input = "IF A THEN B";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    System.out.println(printed);
    assertThat(lines, is(new String[] {
        "THEN",
        "  IF",
        "    A",
        "  B",
    }));

  }

  @Test
  public void testIfThenElse() throws Exception {
    String input = "IF A THEN B ELSE C";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    System.out.println(printed);
    assertThat(lines, is(new String[] {
        "ELSE",
        "  THEN",
        "    IF",
        "      A",
        "    B",
        "  C",
    }));

  }

  @Test
  public void testIfThenElseElseIf() throws Exception {
    String input = "IF A THEN B ELSE IF C THEN D ELSE C";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    System.out.println(printed);
    assertThat(lines, is(new String[] {
        "ELSE",
        "  ELSE",
        "    THEN",
        "      IF",
        "        A",
        "      B",
        "    THEN",
        "      IF",
        "        C",
        "      D",
        "  C",
    }));

  }


}
