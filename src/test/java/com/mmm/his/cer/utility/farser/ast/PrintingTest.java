package com.mmm.his.cer.utility.farser.ast;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmm.his.cer.utility.farser.ast.AstPrinter.AstPrinterContext;
import com.mmm.his.cer.utility.farser.ast.AstTest.StringOperandSupplier;
import com.mmm.his.cer.utility.farser.ast.node.type.BooleanExpression;
import com.mmm.his.cer.utility.farser.ast.parser.DescentParser;
import com.mmm.his.cer.utility.farser.ast.setup.MaskedContext;
import com.mmm.his.cer.utility.farser.ast.setup.TestContext;
import com.mmm.his.cer.utility.farser.lexer.DrgFormulaLexer;
import com.mmm.his.cer.utility.farser.lexer.drg.DrgLexerToken;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 *
 *
 * @author Thomas Naeff
 *
 */
public class PrintingTest {

  @Test
  public void testPrintTree() throws Exception {

    List<DrgLexerToken> lexerTokens = DrgFormulaLexer.lex("(A & B | C) & D | (E & F)");
    DescentParser<MaskedContext<String>> parser = new DescentParser<>(lexerTokens.listIterator(),
        new StringOperandSupplier(), Collections.emptyMap());

    AbstractSyntaxTree<MaskedContext<String>> ast = parser.buildTree();

    String printed = AstPrinter.printTree(ast, PrintingTest::printNode);
    String[] lines = printed.split(System.lineSeparator());

    // System.out.println(printed);
    assertThat(lines, is(new String[] {
        "00OR",
        "01  AND",
        "02    OR",
        "03      AND",
        "04        A",
        "04        B",
        "03      C",
        "02    D",
        "01  AND",
        "02    E",
        "02    F"
    }));

  }

  @Test
  public void testPrintTreeWithPeek() throws Exception {

    List<DrgLexerToken> lexerTokens = DrgFormulaLexer.lex("(A & B | C) & D | (E & F)");
    DescentParser<MaskedContext<String>> parser = new DescentParser<>(lexerTokens.listIterator(),
        new StringOperandSupplier(), Collections.emptyMap());

    AbstractSyntaxTree<MaskedContext<String>> ast = parser.buildTree();

    String printed = AstPrinter.printTree(ast, PrintingTest::printNodeWithPeek);
    String[] lines = printed.split(System.lineSeparator());

    //    System.out.println(printed);
    assertThat(lines, is(new String[] {
        "00OR next=AND",
        "01  AND next=OR",
        "02    OR next=AND",
        "03      AND next=A",
        "04        A next=B",
        "04        B next=C",
        "03      C next=D",
        "02    D next=AND",
        "01  AND next=E",
        "02    E next=F",
        "02    F next=NONE",
        "02    ",
        "01  ",
        "00"
    }));

  }

  @Test
  public void testPrintTreeEvaluated() throws Exception {

    List<DrgLexerToken> lexerTokens = DrgFormulaLexer.lex("(A & B | C) & D | (E & F)");
    DescentParser<MaskedContext<String>> parser = new DescentParser<>(lexerTokens.listIterator(),
        new StringOperandSupplier(), Collections.emptyMap());

    AbstractSyntaxTree<MaskedContext<String>> ast = parser.buildTree();
    List<String> mask = Arrays.asList("A", "C");

    NodePrinterWithContextData<MaskedContext<String>> nodePrinter =
        new NodePrinterWithContextData<>(new TestContext<>(mask));

    String printed = AstPrinter.printTree(ast, nodePrinter::printNode);
    String[] lines = printed.split(System.lineSeparator());

    // System.out.println(printed);
    assertThat(lines, is(new String[] {
        "OR = false",
        "  AND = false",
        "    OR = true",
        "      AND = false",
        "        A = true",
        "        B = false",
        "      C = true",
        "    D = false",
        "  AND = false",
        "    E = false",
        "    F = false"
    }));

  }


  /********************************************************************************************************
   *
   *
   * @author Thomas Naeff
   *
   * @param <T>
   */
  private static class NodePrinterWithContextData<T> {

    private final T evaluationContext;

    private NodePrinterWithContextData(T printerContext) {
      this.evaluationContext = printerContext;
    }

    /**
     * Prints the output of a single node.
     *
     * @param node           The node to print
     * @param printerContext The printerContext for node evaluation. May be <code>null</code> to
     *                       skip evaluation
     * @return The node output
     */
    public String printNode(BooleanExpression<T> node, AstPrinterContext<?> printerContext) {
      StringBuilder sb = new StringBuilder();
      if (node != null) {
        sb.append(printerContext.prefix);
        sb.append(node.print());
        if (evaluationContext != null) {
          boolean result = node.evaluate(evaluationContext);
          sb.append(" = " + result);
        }
        sb.append(System.lineSeparator());
      }
      return sb.toString();
    }

  }


  public static String printNode(BooleanExpression<?> node, AstPrinterContext<?> printerContext) {
    StringBuilder sb = new StringBuilder();
    if (node != null) {
      // Print node depth to verify it
      sb.append(String.format("%02d", printerContext.depth));
      sb.append(printerContext.prefix);
      sb.append(node.print());
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }

  public static String printNodeWithPeek(BooleanExpression<?> node,
      AstPrinterContext<?> printerContext) {
    StringBuilder sb = new StringBuilder();
    // Print depth to ensure it does not get affected by the peek
    sb.append(String.format("%02d", printerContext.depth));
    sb.append(printerContext.prefix);
    if (node != null) {
      sb.append(node.print());
      sb.append(" next=");
      sb.append(printerContext.next == null ? "NONE" : printerContext.next.print());
    }
    sb.append(System.lineSeparator());
    return sb.toString();
  }

}
