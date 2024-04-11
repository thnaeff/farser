package com.mmm.his.cer.utility.farser.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.mmm.his.cer.utility.farser.ast.parser.ExpressionParser;
import org.junit.Test;

/**
 *
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


}
