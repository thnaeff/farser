package com.mmm.his.cer.utility.farser.ast;

import com.mmm.his.cer.utility.farser.CommonTokenFlag;
import java.util.Optional;

public enum AstTokenFlag implements CommonTokenFlag {

  /**
   * A left parenthesis "(".
   */
  LPAREN(),

  /**
   * A right parenthesis ")".
   */
  RPAREN(),

  /**
   * A negation/not.
   */
  NOT(),

  /**
   * An <code>AND</code> operator.
   */
  AND(AstSide.RIGHT),

  /**
   * An <code>OR</code> operator.
   */
  OR(AstSide.LEFT);

  private final AstSide side;

  AstTokenFlag(AstSide side) {
    this.side = side;
  }

  public boolean isSide(AstSide side) {
    return this.side == null ? false : this.side == side;
  }

  public static boolean isSide(CommonTokenFlag flag, AstSide side) {
    if (!(flag instanceof AstTokenFlag)) {
      return false;
    }
    return ((AstTokenFlag) flag).isSide(side);
  }

  AstTokenFlag() {
    this.side = null;
  }

  public Optional<AstSide> getSide() {
    return Optional.of(side);
  }

}
