package com.mmm.his.cer.utility.farser.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.BooleanExpression;
import com.mmm.his.cer.utility.farser.ast.node.type.LtrExpressionIterator;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A printer to output a {@link DrgSyntaxTree} in visual representation.
 *
 * @author Thomas Naeff
 *
 */
public final class AstPrinter {

  private AstPrinter() {
    // Private. Only static methods.
  }

  /**
   * Prints a simple tree representation.<br>
   * Uses {@link #printNodeSimple(BooleanExpression)}.
   *
   * @param ast The AST
   * @return The tree representation as string
   */
  public static String printTree(DrgSyntaxTree<?> ast) {
    return printTree(ast, AstPrinter::printNodeSimple);
  }


  public static <T> String printTree(DrgSyntaxTree<T> ast,
      Function<BooleanExpression<T>, String> printNode) {
    return printTree(ast, (node, next) -> printNode.apply(node));
  }

  /**
   * Prints a simple tree representation.
   *
   * @param ast       The AST
   * @param printNode The function which determines how to print a node. The
   *                  {@link BooleanExpression} function input may be <code>null</code> when the
   *                  printing is past the last node (to possibly finalize/close data structures).
   *                  See {@link #printNodeSimple(BooleanExpression)} for a simple starting point.
   * @return The tree representation as string
   */
  public static <T> String printTree(DrgSyntaxTree<T> ast,
      BiFunction<BooleanExpression<T>, AstPrinterContext<T>, String> printNode) {
    StringBuilder sb = new StringBuilder();
    int previousDepth = 0;
    int currentDepth = 0;

    LtrExpressionIterator<T> iter = ast.iterator();

    sb.append(printNode.apply(ast, createPrinterContext(iter, "", 0)));

    while (iter.hasNext()) {
      // Need to get next first, so that depth of new/current node is available.
      BooleanExpression<T> node = iter.next();
      previousDepth = currentDepth;
      currentDepth = iter.getCurrentDepth();

      String prefix = prefix(currentDepth);
      AstPrinterContext<T> context = createPrinterContext(iter, prefix, previousDepth);
      sb.append(printNode.apply(node, context));
    }

    // Call the printing again for each depth, from the current node all the way back up to the
    // root depth. This allows the printer to possibly close any nesting-dependent structures.
    int nextDepth = iter.getPeekedDepth();
    for (; currentDepth >= 0; currentDepth--) {
      String prefix = prefix(currentDepth);
      AstPrinterContext<T> context = new AstPrinterContext<>(prefix, currentDepth,
          AstPrintDirection.UP, null, nextDepth, AstPrintDirection.UP);
      sb.append(printNode.apply(null, context));
    }

    return sb.toString();
  }

  /**
   * Gathers information to create the {@link AstPrinterContext}.
   *
   * @param iter          The current iterator
   * @param prefix        The current prefix
   * @param previousDepth The depth of the previous node
   * @return The context object
   */
  private static <T> AstPrinterContext<T> createPrinterContext(LtrExpressionIterator<T> iter,
      String prefix, int previousDepth) {
    BooleanExpression<T> peeked = null;
    peeked = iter.hasNext() ? iter.peek() : null;

    int currentDepth = iter.getCurrentDepth();
    int peekedDepth = iter.getPeekedDepth();
    AstPrintDirection direction = AstPrintDirection.fromDepthDelta(currentDepth - previousDepth);
    AstPrintDirection peekedDirection =
        AstPrintDirection.fromDepthDelta(peekedDepth - currentDepth);

    return new AstPrinterContext<>(prefix, currentDepth, direction, peeked, peekedDepth,
        peekedDirection);
  }

  /**
   * Generates a prefix to structure each line of the tree.
   *
   * @param depth The prefix width
   * @return The prefix
   */
  private static String prefix(int depth) {
    StringBuilder sb = new StringBuilder();
    for (; depth > 0; depth--) {
      sb.append("  ");
    }
    return sb.toString();
  }


  /**
   * The simplest version of printing a node.<br>
   * This is a method that could be provided to {@link #printTree(DrgSyntaxTree, Function)}.
   *
   * @param node The node to print
   * @return The printed representation
   */
  public static String printNodeSimple(BooleanExpression<?> node) {
    return node.print();
  }


  /***********************************************************************************************************************
   *
   *
   * @author Thomas Naeff
   *
   * @param <T>
   */
  public static class AstPrinterContext<T> {

    /**
     * The tree depth of the current node. Either 0 (zero) or a positive value.
     */
    public final int depth;

    /**
     * The tree depth of the {@link #next} node. Either 0 (zero) or a positive value if
     * {@link #next} is not <code>null</code>, or {@value LtrExpressionIterator#PEEKED_DEPTH_NONE}
     * when {@link #next} is <code>null</code>.
     */
    public final int nextDepth;

    /**
     * The prefix for indentation. Never <code>null</code>.
     */
    public final String prefix;

    /**
     * The direction of the nesting - in relation to the previous node. Never <code>null</code>.
     */
    public final AstPrintDirection direction;

    /**
     * The next direction of the nesting - in relation to the current node. May be <code>null</code>
     * when the last node is reached.
     */
    public final AstPrintDirection nextDirection;

    /**
     * The next node (peek). May be <code>null</code> when the iteration/printing is at or past the
     * last node.
     */
    public final BooleanExpression<T> next;

    private AstPrinterContext(String prefix, int depth, AstPrintDirection direction,
        BooleanExpression<T> next, int nextDepth, AstPrintDirection nextDirection) {
      this.depth = depth;
      this.nextDepth = nextDepth;
      this.prefix = prefix;
      this.direction = direction;
      this.nextDirection = nextDirection;
      this.next = next;
    }

  }

  /**
   * An enumeration with a few flags that inform about the printing direction of one node in
   * relation to its previous node.
   *
   * @author Thomas Naeff
   *
   */
  public enum AstPrintDirection {
    /**
     * Travelling on same level in the tree (depth is the same as the previous node).
     */
    SAME,
    /**
     * Travelling up the tree (depth got reduced compared to the previous node).
     */
    UP,
    /**
     * Travelling down the tree (depth got increased compared to the previous node).
     */
    DOWN;

    /**
     * Returns the direction identifier based on the node depth delta (current/next node depth minus
     * previous node depth).
     *
     * @param depthDelta The depth delta
     * @return The direction
     */
    public static AstPrintDirection fromDepthDelta(int depthDelta) {
      if (depthDelta > 0) {
        return AstPrintDirection.DOWN;
      } else if (depthDelta < 0) {
        return AstPrintDirection.UP;
      }
      return AstPrintDirection.SAME;
    }
  }

}
