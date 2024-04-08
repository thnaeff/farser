package com.mmm.his.cer.utility.farser.ast_if;

import com.mmm.his.cer.utility.farser.ast_if.setup.IfTestTokenType;
import com.mmm.his.cer.utility.farser.ast_if.setup.lex.IfTestToken;
import com.mmm.his.cer.utility.farser.ast_if.setup.lex.IfTestTokenFactory;
import com.mmm.his.cer.utility.farser.lexer.Lexer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class ReScriptIntoPojoTest {

  private static final IfTestTokenFactory factory = new IfTestTokenFactory();


  @Test
  public void convertToSomeJsonRepresentation() throws Exception {
    String input = "IF A > 5 THEN RESULT = 10 ELSE ETWAS = 11 ENDIF";

    List<IfTestToken> tokens = Lexer.lex(IfTestTokenType.class, input, factory);

    Content content = collect(tokens.iterator());

    System.out.println(content);

  }

  private IfTestToken lastToken = null;

  private Content collect(Iterator<IfTestToken> iter) {
    Content content = new Content();

    if ((lastToken != null) && (lastToken.getType() == IfTestTokenType.ELSE)) {
      content.formula.add(lastToken);
      lastToken = null;
      System.out.println("content: " + content);
    }

    while (iter.hasNext()) {
      IfTestToken token = iter.next();
      System.out.print("processing: " + token);
      if (token.getType() == IfTestTokenType.IF) {
        System.out.println(" -> skip");
        continue;
      } else if (token.getType() == IfTestTokenType.THEN) {
        System.out.println(" -> collect");
        Content then = collect(iter);
        System.out.println("then: " + then);
        content.then = then;
        System.out.println("content: " + content);
      } else if (token.getType() == IfTestTokenType.ELSE) {
        System.out.println(" -> return");
        lastToken = token;
        return content;
      } else if ((lastToken != null) && (lastToken.getType() == IfTestTokenType.ELSE)) {
        Content orElse = collect(iter);
        System.out.println("orElse: " + orElse);
        content.orElse = orElse;
        System.out.println("content: " + content);
      } else if (token.getType() == IfTestTokenType.ENDIF) {
        System.out.println(" -> return");
        return content;
      } else {
        System.out.println(" -> add");
        content.formula.add(token);
        System.out.println("content: " + content);
      }
    }
    return content;
  }


  private static class Content {

    List<IfTestToken> formula = new ArrayList<>();
    Content then = null;
    Content orElse = null;

    @Override
    public String toString() {
      String formulaString =
          formula.stream().map(token -> token.getValue()).collect(Collectors.joining(" "));

      StringBuilder sb = new StringBuilder();
      sb.append("IF ");
      sb.append(formulaString);
      if (then != null) {
        sb.append(" THEN ");
        sb.append(then);
      }
      if (orElse != null) {
        sb.append(" ELSE ");
        sb.append(orElse);
      }

      return sb.toString();
    }
  }

}
