package com.mmm.his.cer.utility.farser.ast_if;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTree;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.ast.parser.ExpressionResult;
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
public class IfEvaluationTest {

  private static final IfTestTokenFactory factory = new IfTestTokenFactory();
  private static final NodeSupplier<IfTestToken, IfTestAstContext> defaultNodeSupplier =
      new IfTestAstNodeSupplier();

  @Test
  public void testIfThen_EvaluateThen() throws Exception {
    String input = "IF A THEN B";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    // System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<IfTestAstContext> evaluation = ast.evaluateExpression(
        new IfTestAstContext("A"));
    IfTestAstContext context = evaluation.getContext();

    // Should be 'true' because at least one if-statement evaluated to 'true'
    assertThat(evaluation.isMatched(), is(true));

    // It should evaluate the 'A' expression and then also go to 'B'
    assertThat(context.evaluatedExpressions, contains("A", "B"));

  }

  @Test
  public void testIfThen_EvaluateNone() throws Exception {
    String input = "IF A THEN B";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    // System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<IfTestAstContext> evaluation = ast.evaluateExpression(
        new IfTestAstContext("X"));
    IfTestAstContext context = evaluation.getContext();

    // Should be 'false' because none of the if-statements evaluated to 'true'
    assertThat(evaluation.isMatched(), is(false));

    // It should evaluate the 'A' expression, but since that does not result in 'true' it will do
    // nothing else.
    assertThat(context.evaluatedExpressions, contains("A"));

  }

  @Test
  public void testIfThenElse_EvaluateThen() throws Exception {
    String input = "IF A THEN B ELSE C";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    // System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<IfTestAstContext> evaluation = ast.evaluateExpression(
        new IfTestAstContext("A"));
    IfTestAstContext context = evaluation.getContext();

    // Should be 'true' because at least one if-statement evaluated to 'true'
    assertThat(evaluation.isMatched(), is(true));

    // It should evaluate the 'A' expression and then also go to 'B'
    assertThat(context.evaluatedExpressions, contains("A", "B"));
  }

  @Test
  public void testIfThenElse_EvaluateElse() throws Exception {
    String input = "IF A THEN B ELSE C";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    // System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<IfTestAstContext> evaluation = ast.evaluateExpression(
        new IfTestAstContext("X"));
    IfTestAstContext context = evaluation.getContext();

    // Should be 'false' because none of the if-statements evaluated to 'true'
    assertThat(evaluation.isMatched(), is(false));

    // It should evaluate the 'A' expression and then also go to 'C' since no if-statements match.
    assertThat(context.evaluatedExpressions, contains("A", "C"));
  }

  @Test
  public void testIfThenElseElseIf_EvaluateFirstIfAndThen() throws Exception {
    String input = "IF A THEN B ELSE IF C THEN D ELSE C";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    // System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<IfTestAstContext> evaluation = ast.evaluateExpression(
        new IfTestAstContext("A"));
    IfTestAstContext context = evaluation.getContext();

    // Should be 'true' because at least one if-statement evaluated to 'true'
    assertThat(evaluation.isMatched(), is(true));

    // It should evaluate the 'A' expression and then also go to 'B'
    assertThat(context.evaluatedExpressions, contains("A", "B"));

  }

  @Test
  public void testIfThenElseElseIf_EvaluateSecondIfAndThen() throws Exception {
    String input = "IF A THEN B ELSE IF C THEN D ELSE C";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    // System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<IfTestAstContext> evaluation = ast.evaluateExpression(
        new IfTestAstContext("C"));
    IfTestAstContext context = evaluation.getContext();

    // Should be 'true' because at least one if-statement evaluated to 'true'
    assertThat(evaluation.isMatched(), is(true));

    // It should evaluate the 'A' expression but continue to the 'C' expression and then also go to
    // 'D'
    assertThat(context.evaluatedExpressions, contains("A", "C", "D"));

  }

  @Test
  public void testIfThenElseElseIf_EvaluateElse() throws Exception {
    String input = "IF A THEN B ELSE IF C THEN D ELSE E";
    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    AstDescentParser<IfTestToken, IfTestTokenType, IfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<IfTestAstContext> ast = parser.buildTree();

    // System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<IfTestAstContext> evaluation = ast.evaluateExpression(
        new IfTestAstContext("X"));
    IfTestAstContext context = evaluation.getContext();

    // Should be 'false' because none of the if-statements evaluated to 'true'
    assertThat(evaluation.isMatched(), is(false));

    // It should evaluate the 'A' expression but continue to the 'C' expression and then also go to
    // 'E' since no if-statements match.
    assertThat(context.evaluatedExpressions, contains("A", "C", "E"));

  }


}
