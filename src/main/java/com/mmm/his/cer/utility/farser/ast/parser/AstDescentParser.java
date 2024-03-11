package com.mmm.his.cer.utility.farser.ast.parser;

import com.mmm.his.cer.utility.farser.ast.AbstractSyntaxTree;
import com.mmm.his.cer.utility.farser.ast.DrgSyntaxTree;
import com.mmm.his.cer.utility.farser.ast.node.operator.And;
import com.mmm.his.cer.utility.farser.ast.node.operator.Not;
import com.mmm.his.cer.utility.farser.ast.node.operator.Or;
import com.mmm.his.cer.utility.farser.ast.node.type.BooleanExpression;
import com.mmm.his.cer.utility.farser.ast.node.type.CompositeTokenSupplier;
import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.lexer.CommonTokenType;
import com.mmm.his.cer.utility.farser.lexer.FarserException;
import com.mmm.his.cer.utility.farser.lexer.LexerToken;
import com.mmm.his.cer.utility.farser.lexer.TokenType;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

/**
 * Recursive descent parser that will build an Abstract syntax tree from a formula/script (list of
 * tokens).
 *
 * @author Mike Funaro
 * @author Thomas Naeff
 *
 * @param <C> The type of the context used when evaluating the AST
 */
public class AstDescentParser<L extends LexerToken<T>, T extends TokenType<?>, C> {

  private L currentToken;
  private Iterator<L> tokenIterator;
  private final NodeSupplier<L, C> defaultSupplier;
  private final Map<String, NodeSupplier<L, C>> suppliers;
  private CompositeTokenSupplier<L, T> compositeTokenSupplier = null;

  /**
   * Ctor.
   *
   * @param tokenIterator   list of tokens to parse into the Abstract syntax tree. May be
   *                        <code>null</code>.
   * @param defaultSupplier a factory which creates nodes for the tree. This supplier is used by
   *                        default when <code>suppliers</code> does not contain a node specific
   *                        supplier
   * @param suppliers       A map with node suppliers specific to certain tokens (token value as map
   *                        key). May be <code>null</code>.
   */
  public AstDescentParser(Iterator<L> tokenIterator,
      NodeSupplier<L, C> defaultSupplier,
      Map<String, NodeSupplier<L, C>> suppliers) {
    setTokenIterator(tokenIterator);
    if (defaultSupplier == null) {
      throw new FarserException(
          "Please provide at least a default supplier argument to "
              + DescentParser.class.getSimpleName()
              + " constructor");
    }
    this.defaultSupplier = defaultSupplier;

    // If there is no map, instantiate new map to avoid NPEs. If nothing is in the map the
    // defaultSupplier takes over.
    if (suppliers == null) {
      this.suppliers = Collections.emptyMap();
    } else {
      this.suppliers = suppliers;
    }
  }

  /**
   * Set a new tokenIterator so that we can build another AST using the same setup parser. Uses the
   * same {@link NodeSupplier}s which were set when the {@link AstDescentParser} was created.
   */
  public void setTokenIterator(Iterator<L> tokenIterator) {
    this.tokenIterator = tokenIterator;
    this.currentToken = tokenIterator != null ? tokenIterator.next() : null;
  }

  /**
   * Enables the parsing of composite expressions (e.g. "A > 5").
   *
   * @param compositeTokenSupplier The token supplier. May be <code>null</code> to disable composite
   *                               expression capabilities.
   */
  public void enableCompositeExpressions(CompositeTokenSupplier<L, T> compositeTokenSupplier) {
    this.compositeTokenSupplier = compositeTokenSupplier;
  }

  /**
   * Build the abstract syntax tree.
   *
   * @deprecated Call {@link #buildTree()} instead for a non-DRG specific named AST class version
   *             with the exact same functionality.
   */
  @Deprecated
  public DrgSyntaxTree<C> buildExpressionTree() {
    BooleanExpression<C> root = expression(null);
    return new DrgSyntaxTree<>(root);
  }

  /**
   * Build the abstract syntax tree from the provided formula/tokens.
   *
   * @param tokenIterator list of tokens to parse into the Abstract syntax tree.
   *
   * @deprecated Call {@link #buildTree(ListIterator)} instead for a non-DRG specific named AST
   *             class version with the exact same functionality.
   */
  @Deprecated
  public DrgSyntaxTree<C> buildExpressionTree(ListIterator<L> tokenIterator) {
    setTokenIterator(tokenIterator);
    BooleanExpression<C> root = expression(null);
    return new DrgSyntaxTree<>(root);
  }

  /**
   * Build the abstract syntax tree.
   */
  public AbstractSyntaxTree<C> buildTree() {
    BooleanExpression<C> root = expression(null);
    return new AbstractSyntaxTree<>(root);
  }

  /**
   * Build the abstract syntax tree from the provided formula/tokens.
   *
   * @param tokenIterator list of tokens to parse into the Abstract syntax tree.
   */
  public AbstractSyntaxTree<C> buildTree(ListIterator<L> tokenIterator) {
    setTokenIterator(tokenIterator);
    BooleanExpression<C> root = expression(null);
    return new AbstractSyntaxTree<>(root);
  }

  /**
   * Expression method which will build the OR after parsing a term.
   */
  private BooleanExpression<C> expression(BooleanExpression<C> root) {
    root = term(root);
    while (currentToken.getType().isEqual(CommonTokenType.OR)) {
      this.eat(CommonTokenType.OR); // Move iterator if 'OR'
      Or<C> or = new Or<>();
      or.setLeft(root);
      root = term(root);
      or.setRight(root);
      root = or;
    }
    return root;
  }

  /**
   * Term method which will build the AND after parsing the factors or operands.
   */
  private BooleanExpression<C> term(BooleanExpression<C> root) {
    root = factor(root);
    while (currentToken.getType().isEqual(CommonTokenType.AND)) {
      this.eat(CommonTokenType.AND); // Move iterator if 'AND'
      And<C> and = new And<>();
      and.setLeft(root);
      root = factor(root);
      and.setRight(root);
      root = and;
    }
    return root;
  }

  /**
   * Method to build a composite boolean expression (e.g. "A > 2"). Falls back to
   * {@link #atom(LexerToken)} if it is determined that the tokens do not form a composite
   * expression.
   */
  private BooleanExpression<C> composite() {
    L leftOperand = currentToken;
    eat(CommonTokenType.ATOM); // Move iterator past the left-side operand

    // ATOM follows a common type -> not a composite expression
    if (currentToken.getType().getCommonTokenType().isPresent()) {
      return atom(leftOperand);
    }

    // Non-common type expected
    if (currentToken.getType().getCommonTokenType().isPresent()) {
      throw new FarserException("Expression malformed on token "
          + currentToken
          + ". Left operand "
          + CommonTokenType.ATOM
          + " token "
          + leftOperand
          + " must follow a non-common token type operator to form a composite expression");
    }

    L operator = currentToken;
    this.eat(); // Move the iterator past the operator of any (non-common) type
    L rightOperand = currentToken;
    L composite = this.compositeTokenSupplier.createToken(leftOperand, operator, rightOperand);
    return atom(composite);
  }

  /**
   * Method to build the current token as ATOM node.
   */
  private BooleanExpression<C> atom(L atomToken) {
    NodeSupplier<L, C> nodeSupplier = suppliers.getOrDefault(
        atomToken.getValue(), defaultSupplier);
    return nodeSupplier.createNode(atomToken);
  }

  /**
   * Factor out a single operand.
   */
  private BooleanExpression<C> factor(BooleanExpression<C> root) {
    TokenType<?> tokenType = currentToken.getType();
    // Get common type for generic checking. Ok to return 'null', it is only used in NPE safe logic
    // below.
    CommonTokenType commonType = tokenType.getCommonTokenType().orElse(null);
    if (commonType == CommonTokenType.ATOM) {
      if (this.compositeTokenSupplier == null) {
        // Shortcut if no composite tokens are expected
        root = atom(currentToken);
      } else {
        root = this.composite();
      }
      this.eat(CommonTokenType.ATOM); // Move iterator if 'ATOM'
    } else if (commonType == CommonTokenType.LPAREN) {
      this.eat(CommonTokenType.LPAREN); // Move iterator if 'LPAREN'
      root = this.expression(root);
      this.eat(CommonTokenType.RPAREN);
    } else if (commonType == CommonTokenType.NOT) {
      this.eat(CommonTokenType.NOT); // Move iterator if 'NOT'
      Not<C> not = new Not<>();
      root = factor(root);
      not.setChild(root);
      root = not;
    } else {
      throw new FarserException("Expression malformed on token " + currentToken);
    }

    return root;
  }

  /**
   * Move the iterator forward if the current token matches the one passed in.
   *
   * @param type the type of the token to eat.
   */
  private boolean eat(CommonTokenType type) {
    if (currentToken.getType().isEqual(type) && this.tokenIterator.hasNext()) {
      currentToken = this.tokenIterator.next();
    }
    return this.tokenIterator.hasNext();
  }

  /**
   * Move the iterator forward, if there are more elements.
   *
   * @return <code>true</code> if iteration moved forward, <code>false</code> if iteration reached
   *         the end
   */
  private boolean eat() {
    if (this.tokenIterator.hasNext()) {
      currentToken = this.tokenIterator.next();
      return true;
    }
    return false;
  }

}
