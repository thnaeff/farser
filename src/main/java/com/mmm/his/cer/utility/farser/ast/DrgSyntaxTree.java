package com.mmm.his.cer.utility.farser.ast;

import com.mmm.his.cer.utility.farser.ast.node.type.BooleanExpression;

/**
 * Class that wraps a {@link BooleanExpression} and provides methods to evaluate it.
 *
 * @param <T> the type of context
 * @author Mike Funaro
 * @deprecated Use {@link AbstractSyntaxTree} instead for a non-DRG specific named class version with the
 *             exact same functionality.
 */
@Deprecated
public class DrgSyntaxTree<T> extends AbstractSyntaxTree<T> {

  public DrgSyntaxTree(BooleanExpression<T> ast) {
    super(ast);
  }

}
