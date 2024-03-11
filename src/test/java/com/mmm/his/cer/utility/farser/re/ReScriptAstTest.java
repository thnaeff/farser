package com.mmm.his.cer.utility.farser.re;

import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTree;
import com.mmm.his.cer.utility.farser.ast.AstPrinter;
import com.mmm.his.cer.utility.farser.ast.node.type.BooleanExpression;
import com.mmm.his.cer.utility.farser.ast.node.type.CompositeTokenSupplier;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.lexer.Lexer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;

public class ReScriptAstTest {

  private static final ReTokenFactory factory = new ReTokenFactory();
  private static final Map<String, NodeSupplier<ReToken, ReAstContext>> tokenSpecificSuppliers =
      new HashMap<>();
  private static final NodeSupplier<ReToken, ReAstContext> defaultNodeSupplier =
      new DefaultNodeSupplier();
  private static final CompositeTokenSupplier<ReToken, ReTokenType> compositeTokenSupplier =
      new ReCompositeTokenSupplier();

  @Test
  public void simpleTwoExpressionsWithAnd() throws Exception {
    String input = "A > 5 & B = 2";
    List<ReToken> tokens = Lexer.lex(ReTokenType.class, input, factory);

    List<String> tokensAsString =
        tokens.stream().map(ReToken::toString).collect(Collectors.toList());
    System.out.println(tokensAsString);

    AstDescentParser<ReToken, ReTokenType, ReAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier, tokenSpecificSuppliers);
    parser.enableCompositeExpressions(compositeTokenSupplier);
    AbstractSyntaxTree<ReAstContext> ast = parser.buildTree();

    String printed = AstPrinter.printTree(ast);
    System.out.println(printed);

  }

  @Test
  public void twoWordTokenWithOneWordAmbiguity() throws Exception {
    // Token "IN TABLE" could also get recognized as "IN", but it should get resolved as "IN TABLE"
    String input = "IF A IN TABLE abc THEN RESULT = 10 END";
    List<ReToken> tokens = Lexer.lex(ReTokenType.class, input, factory);

    AstDescentParser<ReToken, ReTokenType, ReAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier, tokenSpecificSuppliers);
    AbstractSyntaxTree<ReAstContext> ast = parser.buildTree();

    String printed = AstPrinter.printTree(ast);
    System.out.println(printed);

  }

  @Test
  public void singleWordTokenWithTwoWordAmbiguity() throws Exception {
    // Token "IN" also exists as "IN TABLE", but it should get resolved as "IN"
    String input = "IF A IN abc THEN "
        + "RESULT = 10 "
        + "END";
    List<ReToken> tokens = Lexer.lex(ReTokenType.class, input, factory);

    AstDescentParser<ReToken, ReTokenType, ReAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier, tokenSpecificSuppliers);
    AbstractSyntaxTree<ReAstContext> ast = parser.buildTree();

    String printed = AstPrinter.printTree(ast);
    System.out.println(printed);

  }

  @Test
  public void listWithParenthesis() throws Exception {
    // Token "IN" also exists as "IN TABLE", but it should get resolved as "IN"
    String input = "IF A IN (abc, def, ghi) THEN "
        + "RESULT = 10 "
        + "END";
    List<ReToken> tokens = Lexer.lex(ReTokenType.class, input, factory);

    AstDescentParser<ReToken, ReTokenType, ReAstContext> parser =
        new AstDescentParser<>(tokens.iterator(), defaultNodeSupplier, tokenSpecificSuppliers);
    AbstractSyntaxTree<ReAstContext> ast = parser.buildTree();

    String printed = AstPrinter.printTree(ast);
    System.out.println(printed);

  }

  /**********************************************************************************************************
   *
   *
   * @author Thomas Naeff
   *
   */
  private static class ReAstContext {

  }

  /**********************************************************************************************************
   *
   *
   * @author Thomas Naeff
   *
   */
  public static class ReCompositeTokenSupplier
  implements CompositeTokenSupplier<ReToken, ReTokenType> {

    @Override
    public ReToken createToken(ReToken leftOperand, ReToken operator, ReToken rightOperand) {
      return new ReCompositeToken(leftOperand, operator, rightOperand);
    }

  }

  /**********************************************************************************************************
   *
   *
   * @author Thomas Naeff
   *
   */
  public static class DefaultNodeSupplier implements NodeSupplier<ReToken, ReAstContext> {

    @Override
    public BooleanExpression<ReAstContext> createNode(final ReToken inToken) {

      return new BooleanExpression<ReScriptAstTest.ReAstContext>() {
        private ReToken token = inToken;
        @Override
        public boolean evaluate(ReAstContext context) {
          // TODO something else - more logic
          return true;
        }

        @Override
        public String print() {
          return token.value;
        }

        @Override
        public String toString() {
          return token.value;
        }
      };
    }
  }

}
