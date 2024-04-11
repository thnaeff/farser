package com.mmm.his.cer.utility.farser.ast.node.type;

/**
 * A statement, which processes something and produces a side-effect (e.g. the code in an THEN/ELSE
 * block, updates the context) but does not return a value.
 *
 * @author Thomas Naeff
 *
 */
public interface Statement<C> extends BooleanExpression<C> {

  /**
   * Evaluates the statement.<br>
   * <b>Do not implement/override this method. Implement {@link #process(Object)} instead.</b>
   *
   * @return A statement does not produce a return value from its {@link #process(Object)} method.
   *         However, it still implements the common {@link Expression} type for convenience and
   *         returns <code>true</code> to indicate to the parent that a code path got hit and got
   *         evaluated/processed.
   */
  @Override
  default Boolean evaluate(C context) {
    // An expression evaluates to a value. A statement does something.
    // -> This statement does not have a return value, it just processes side-effects.
    process(context);
    return true;
  }

  /**
   * Processes this statement.
   *
   * @param context The context that will be used in the evaluation of the node.
   */
  void process(C context);

}
