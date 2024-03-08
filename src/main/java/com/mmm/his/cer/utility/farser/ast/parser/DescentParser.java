package com.mmm.his.cer.utility.farser.ast.parser;

import com.mmm.his.cer.utility.farser.ast.node.type.NodeSupplier;
import com.mmm.his.cer.utility.farser.lexer.drg.DrgFormulaToken;
import com.mmm.his.cer.utility.farser.lexer.drg.DrgLexerToken;
import java.util.Iterator;
import java.util.Map;

/**
 * Recursive descent parser that will build an Abstract syntax tree from a DRG formula (list of
 * {@link DrgLexerToken} formula tokens).
 *
 * @author Mike Funaro
 *
 * @param <T> The type of the context used when evaluating the AST
 */
public class DescentParser<T> extends AstDescentParser<DrgLexerToken, DrgFormulaToken, T> {

  public DescentParser(Iterator<DrgLexerToken> tokenIterator,
      NodeSupplier<DrgLexerToken, T> defaultSupplier,
      Map<String, NodeSupplier<DrgLexerToken, T>> suppliers) {
    super(tokenIterator, defaultSupplier, suppliers);
  }

}
