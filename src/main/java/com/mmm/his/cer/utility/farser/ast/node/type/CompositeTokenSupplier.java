package com.mmm.his.cer.utility.farser.ast.node.type;

import com.mmm.his.cer.utility.farser.ast.parser.AstDescentParser;
import com.mmm.his.cer.utility.farser.lexer.LexerToken;
import com.mmm.his.cer.utility.farser.lexer.TokenType;

/**
 * Interface for enabling the {@link AstDescentParser} to handle composite boolean expressions (e.g.
 * "A > 4") by creating a token representation of the required user type which then gets passed to
 * the {@link NodeSupplier} as usual.
 *
 * @param <T>
 * @param <R>
 *
 * @author Thomas Naeff
 */
public interface CompositeTokenSupplier<L extends LexerToken<T>, T extends TokenType<?>> {

  /**
   * Creates a composite token of the type required by the user type.
   */
  L createToken(L leftOperand, L operator, L rightOperand);
}
