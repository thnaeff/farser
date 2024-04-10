package com.mmm.his.cer.utility.farser.ast_if_complex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTree;
import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTreePrinter;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.ComplexIfTestTokenType;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast.ComplexIfTestAstContext;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.ast.ComplexIfTestAstNodeSupplier;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.lex.ComplexIfTestToken;
import com.mmm.his.cer.utility.farser.ast_if_complex.setup.lex.ComplexIfTestTokenFactory;
import com.mmm.his.cer.utility.farser.lexer.Lexer;
import java.util.List;
import org.junit.Test;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class ComplexIfTest {

  private static final ComplexIfTestTokenFactory factory = new ComplexIfTestTokenFactory();
  private static final NodeSupplier<ComplexIfTestToken, ComplexIfTestAstContext> defaultNodeSupplier =
      new ComplexIfTestAstNodeSupplier();

  @Test
  public void testIfThen() throws Exception {
    String input = "IF A THEN B";
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    // System.out.println(printed);
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
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    // System.out.println(printed);
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
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    // System.out.println(printed);
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



  @Test
  public void testIfThen_ComplexFormula() throws Exception {
    String input = "IF A > 5 THEN B";
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    String printed = AbstractSyntaxTreePrinter.printTree(ast);
    String[] lines = printed.split(System.lineSeparator());

    System.out.println(printed);
    assertThat(lines, is(new String[] {
        "THEN",
        "  IF",
        "    GREATER-THAN",
        "      A",
        "      5",
        "  B",
    }));

  }

}
