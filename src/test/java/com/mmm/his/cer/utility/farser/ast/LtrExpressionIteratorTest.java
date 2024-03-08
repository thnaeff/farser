package com.mmm.his.cer.utility.farser.ast;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import com.mmm.his.cer.utility.farser.ast.AstTest.StringOperandSupplier;
import com.mmm.his.cer.utility.farser.ast.node.type.LtrExpressionIterator;
import com.mmm.his.cer.utility.farser.ast.parser.DescentParser;
import com.mmm.his.cer.utility.farser.ast.setup.MaskedContext;
import com.mmm.his.cer.utility.farser.lexer.DrgFormulaLexer;
import com.mmm.his.cer.utility.farser.lexer.drg.DrgLexerToken;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;

public class LtrExpressionIteratorTest {

  @Test
  public void testFullTreeIteration() throws Exception {

    List<DrgLexerToken> lexerTokens = DrgFormulaLexer.lex("(A & B | C) & D | (E & F)");
    DescentParser<MaskedContext<String>> parser = new DescentParser<>(lexerTokens.listIterator(),
        new StringOperandSupplier(), Collections.emptyMap());

    DrgSyntaxTree<MaskedContext<String>> ast = parser.buildExpressionTree();

    LtrExpressionIterator<MaskedContext<String>> iter = ast.iterator();

    assertThat(iter.hasNext(), is(true));
    assertThat(iter.getCurrentDepth(), is(0));

    // This printed order may look not right - see 'LtrExpressionIterator' javadoc about iteration
    // order and notation.
    assertThat(iter.next().print(), is("AND"));
    assertThat(iter.getCurrentDepth(), is(1));

    assertThat(iter.next().print(), is("OR"));
    assertThat(iter.getCurrentDepth(), is(2));

    assertThat(iter.next().print(), is("AND"));
    assertThat(iter.getCurrentDepth(), is(3));

    assertThat(iter.next().print(), is("A"));
    assertThat(iter.getCurrentDepth(), is(4));

    assertThat(iter.next().print(), is("B"));
    assertThat(iter.getCurrentDepth(), is(4));

    assertThat(iter.next().print(), is("C"));
    assertThat(iter.getCurrentDepth(), is(3));

    assertThat(iter.next().print(), is("D"));
    assertThat(iter.getCurrentDepth(), is(2));

    assertThat(iter.next().print(), is("AND"));
    assertThat(iter.getCurrentDepth(), is(1));

    assertThat(iter.next().print(), is("E"));
    assertThat(iter.getCurrentDepth(), is(2));

    assertThat(iter.next().print(), is("F"));
    assertThat(iter.getCurrentDepth(), is(2));

    assertThrows(NoSuchElementException.class, iter::next);
    assertThat(iter.hasNext(), is(false));

  }

  @Test
  public void testFullTreeIterationWithPeek() throws Exception {

    List<DrgLexerToken> lexerTokens = DrgFormulaLexer.lex("(A & B | C) & D | (E & F)");
    DescentParser<MaskedContext<String>> parser = new DescentParser<>(lexerTokens.listIterator(),
        new StringOperandSupplier(), Collections.emptyMap());

    DrgSyntaxTree<MaskedContext<String>> ast = parser.buildExpressionTree();

    LtrExpressionIterator<MaskedContext<String>> iter = ast.iterator();

    assertThat(iter.hasNext(), is(true));
    assertThat(iter.getCurrentDepth(), is(0));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.peek().print(), is("AND"));
    assertThat(iter.getCurrentDepth(), is(0));
    assertThat(iter.getPeekedDepth(), is(1));
    // Peek again, nothing should change
    assertThat(iter.peek().print(), is("AND"));
    assertThat(iter.getCurrentDepth(), is(0));
    assertThat(iter.getPeekedDepth(), is(1));

    // This printed order may look not right - see 'LtrExpressionIterator' javadoc about iteration
    // order and notation.
    assertThat(iter.next().print(), is("AND"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(1));
    assertThat(iter.peek().print(), is("OR"));
    assertThat(iter.getCurrentDepth(), is(1));
    assertThat(iter.getPeekedDepth(), is(2));

    assertThat(iter.next().print(), is("OR"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(2));
    assertThat(iter.peek().print(), is("AND"));
    assertThat(iter.getCurrentDepth(), is(2));
    assertThat(iter.getPeekedDepth(), is(3));

    assertThat(iter.next().print(), is("AND"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(3));
    assertThat(iter.peek().print(), is("A"));
    assertThat(iter.getCurrentDepth(), is(3));
    assertThat(iter.getPeekedDepth(), is(4));

    assertThat(iter.next().print(), is("A"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(4));
    assertThat(iter.peek().print(), is("B"));
    assertThat(iter.getCurrentDepth(), is(4));
    assertThat(iter.getPeekedDepth(), is(4));

    assertThat(iter.next().print(), is("B"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(4));
    assertThat(iter.peek().print(), is("C"));
    assertThat(iter.getCurrentDepth(), is(4));
    assertThat(iter.getPeekedDepth(), is(3));

    assertThat(iter.next().print(), is("C"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(3));
    assertThat(iter.peek().print(), is("D"));
    assertThat(iter.getCurrentDepth(), is(3));
    assertThat(iter.getPeekedDepth(), is(2));

    assertThat(iter.next().print(), is("D"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(2));
    assertThat(iter.peek().print(), is("AND"));
    assertThat(iter.getCurrentDepth(), is(2));
    assertThat(iter.getPeekedDepth(), is(1));

    assertThat(iter.next().print(), is("AND"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(1));
    assertThat(iter.peek().print(), is("E"));
    assertThat(iter.getCurrentDepth(), is(1));
    assertThat(iter.getPeekedDepth(), is(2));

    assertThat(iter.next().print(), is("E"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(2));
    assertThat(iter.peek().print(), is("F"));
    assertThat(iter.getCurrentDepth(), is(2));
    assertThat(iter.getPeekedDepth(), is(2));

    assertThat(iter.next().print(), is("F"));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));
    assertThat(iter.getCurrentDepth(), is(2));
    assertThrows(NoSuchElementException.class, iter::peek);
    assertThat(iter.getCurrentDepth(), is(0));
    assertThat(iter.getPeekedDepth(), is(LtrExpressionIterator.PEEKED_DEPTH_NONE));

    assertThrows(NoSuchElementException.class, iter::next);
    assertThat(iter.hasNext(), is(false));

  }

}
