package com.mmm.his.cer.utility.farser.parser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.mmm.his.cer.utility.farser.ast.parser.ExpressionParser;
import org.junit.Test;

/**
 * Testing of {@link ExpressionParser} functionality.
 *
 * @author Thomas Naeff
 *
 */
public class ExpressionParserTest {


  @Test
  public void testIsDoubleOrFloat() throws Exception {

    assertTrue(ExpressionParser.isDoubleOrFloat("1"));
    assertTrue(ExpressionParser.isDoubleOrFloat("1.1"));
    assertTrue(ExpressionParser.isDoubleOrFloat("1."));
    assertTrue(ExpressionParser.isDoubleOrFloat(".0"));
    assertTrue(ExpressionParser.isDoubleOrFloat("1.123456789012345678901234567890"));
    assertTrue(ExpressionParser.isDoubleOrFloat("1.1e12"));

    assertFalse(ExpressionParser.isDoubleOrFloat("."));
    assertFalse(ExpressionParser.isDoubleOrFloat("abc"));
    assertFalse(ExpressionParser.isDoubleOrFloat("e"));

  }

  @Test
  public void testIsInteger() throws Exception {

    assertTrue(ExpressionParser.isInteger("1"));
    assertTrue(ExpressionParser.isInteger("01"));
    assertTrue(ExpressionParser.isInteger("100"));

    assertTrue(ExpressionParser.isInteger("+1"));
    assertTrue(ExpressionParser.isInteger("-1"));

    assertFalse(ExpressionParser.isInteger("1.1"));
    assertFalse(ExpressionParser.isInteger("1e10"));

  }

  @Test
  public void evaluateAsNonNull() throws Exception {

    assertThat(ExpressionParser.evaluateAsNonNull(null, ctx -> "a"), is("a"));
    assertThat(ExpressionParser.evaluateAsNonNull(null, ctx -> ""), is(""));

    NullPointerException exc = assertThrows(NullPointerException.class,
        () -> ExpressionParser.evaluateAsNonNull(null, ctx -> null));
    assertThat(exc.getMessage(), is("The expression evaluation returned NULL. Not allowed."));

  }

  @Test
  public void evaluateTyped() throws Exception {
    Object eval = null;

    eval = ExpressionParser.evaluateTyped(null, ctx -> "a");
    assertThat(eval, is("a"));
    assertThat(eval, is(instanceOf(String.class)));

    eval = ExpressionParser.evaluateTyped(null, ctx -> "\"1\"");
    assertThat(eval, is("1"));
    assertThat(eval, is(instanceOf(String.class)));

    eval = ExpressionParser.evaluateTyped(null, ctx -> "1");
    assertThat(eval, is(1));
    assertThat(eval, is(instanceOf(Integer.class)));

    eval = ExpressionParser.evaluateTyped(null, ctx -> "1.1");
    assertThat(eval, is(1.1));
    assertThat(eval, is(instanceOf(Double.class)));

    eval = ExpressionParser.evaluateTyped(null, ctx -> (float) 1.1);
    assertThat(eval, is(1.1f));
    assertThat(eval, is(instanceOf(Float.class)));

  }

  @Test
  public void evaluateAsInteger() throws Exception {

    assertThat(ExpressionParser.evaluateAsInteger(null, ctx -> "1"), is(1));

    NumberFormatException exc1 = assertThrows(NumberFormatException.class,
        () -> ExpressionParser.evaluateAsInteger(null, ctx -> "1.1"));
    assertThat(exc1.getMessage(), is("For input string: \"1.1\""));

    NumberFormatException exc2 = assertThrows(NumberFormatException.class,
        () -> ExpressionParser.evaluateAsInteger(null, ctx -> ""));
    assertThat(exc2.getMessage(), is("For input string: \"\""));

    NumberFormatException exc3 = assertThrows(NumberFormatException.class,
        () -> ExpressionParser.evaluateAsInteger(null, ctx -> "a"));
    assertThat(exc3.getMessage(), is("For input string: \"a\""));

  }

  @Test
  public void evaluateAsDouble() throws Exception {

    assertThat(ExpressionParser.evaluateAsDouble(null, ctx -> "1"), is(1.0));
    assertThat(ExpressionParser.evaluateAsDouble(null, ctx -> "1.1"), is(1.1));

    NumberFormatException exc1 = assertThrows(NumberFormatException.class,
        () -> ExpressionParser.evaluateAsDouble(null, ctx -> ""));
    assertThat(exc1.getMessage(), is("empty String"));

    NumberFormatException exc2 = assertThrows(NumberFormatException.class,
        () -> ExpressionParser.evaluateAsDouble(null, ctx -> "a"));
    assertThat(exc2.getMessage(), is("For input string: \"a\""));

  }

}
