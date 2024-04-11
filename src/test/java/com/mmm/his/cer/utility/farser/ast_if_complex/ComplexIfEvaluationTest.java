package com.mmm.his.cer.utility.farser.ast_if_complex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTree;
import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTreePrinter;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.ast.parser.ExpressionResult;
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
public class ComplexIfEvaluationTest {

  private static final ComplexIfTestTokenFactory factory = new ComplexIfTestTokenFactory();
  private static final NodeSupplier<ComplexIfTestToken, ComplexIfTestAstContext> defaultNodeSupplier =
      new ComplexIfTestAstNodeSupplier();

  @Test
  public void testIfThen_EvaluatesToThen() throws Exception {
    String input = "IF A > 5 THEN B";
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<ComplexIfTestAstContext> evaluation = ast.evaluateExpression(
        new ComplexIfTestAstContext(vars -> {
          vars.put("A", 6); // Send in a higher value
        }));
    ComplexIfTestAstContext context = evaluation.getContext();

    assertThat(evaluation.isMatched(), is(true));

    // It should evaluate the 'A > 5' expression and then go to 'B'
    assertThat(context.evaluatedExpressions, contains("A", "5", "B"));

  }

  @Test
  public void testIfThen_EvaluationFails() throws Exception {
    String input = "IF A > 5.123 THEN B";
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<ComplexIfTestAstContext> evaluation = ast.evaluateExpression(
        new ComplexIfTestAstContext(vars -> {
          vars.put("A", 5.100); // Send in a lower value
        }));
    ComplexIfTestAstContext context = evaluation.getContext();

    assertThat(evaluation.isMatched(), is(false));

    // It should evaluate the 'A > 5.123' expression, but then not pass the evaluation.
    assertThat(context.evaluatedExpressions, contains("A", "5.123"));

  }

  @Test
  public void testIfThen_EvaluatesToElse() throws Exception {
    String input = "IF A > 5 THEN B ELSE C";
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<ComplexIfTestAstContext> evaluation = ast.evaluateExpression(
        new ComplexIfTestAstContext(vars -> {
          vars.put("A", 5); // Send in a lower value
          // vars.put("C", true); // TODO This is set to boolean because the else-operator
          // right-side
          // node requires that data type. However, the left-side and the
          // right-side node should be allowed to have different data types, so that one can return
          // a boolean evaluation result and the other one can return whatever else it has.
          // Or should we have a new node type? One that does not return any data? Because the
          // then/else content does not need to return data, the returned data is already now
          // ignored.
        }));
    ComplexIfTestAstContext context = evaluation.getContext();

    assertThat(evaluation.isMatched(), is(true));

    // It should evaluate the 'A > 5' expression, then go to the ELSE
    assertThat(context.evaluatedExpressions, contains("A", "5", "C"));

  }


  @Test
  public void testIfEquals_EvaluatesToThen() throws Exception {
    String input = "IF A = 5 THEN B"; // TODO A will be integer, the 5 here will be a string....?
    // what to do? will always be false. It should automatically convert to integer if not quoted.
    // If quoted, then it is a string.
    List<ComplexIfTestToken> tokens = Lexer.lex(ComplexIfTestTokenType.class, input, factory);

    AstDescentParser<ComplexIfTestToken, ComplexIfTestTokenType, ComplexIfTestAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier);
    AbstractSyntaxTree<ComplexIfTestAstContext> ast = parser.buildTree();

    System.out.println(AbstractSyntaxTreePrinter.printTree(ast));

    ExpressionResult<ComplexIfTestAstContext> evaluation = ast.evaluateExpression(
        new ComplexIfTestAstContext(vars -> {
          vars.put("A", 5); // Send in an equal
        }));
    ComplexIfTestAstContext context = evaluation.getContext();

    assertThat(evaluation.isMatched(), is(false));

    // It should evaluate the 'A = 5' expression and then go to 'B'
    assertThat(context.evaluatedExpressions, contains("A", "5", "B"));

  }

}
