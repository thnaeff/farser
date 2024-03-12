# Implement your own Lexer

## 1. Implement a TokenType

The tokens of a formula/script which are recognized as specific type. 
The `ATOM` token is a special type: Anything that is not recognized will result in this type.

```java
public enum MyOwnTokenType implements TokenType<MyOwnTokenType> {

  ATOM(null, CommonTokenType.ATOM),
  SPACE(" ", CommonTokenType.SPACE),
  LPAREN("(", CommonTokenType.LPAREN),
  RPAREN(")", CommonTokenType.RPAREN),

  AND("&&"),
  OR("||");

  private final String value;
  private final CommonTokenType commonType;

  MyOwnTokenType(String value, CommonTokenType commonType) {
    this.value = value;
    this.commonType = commonType;
  }

  MyOwnTokenType(String value) {
    this(value, null);
  }

  @Override
  public Optional<String> getValue() {
    return Optional.ofNullable(value);
  }

  @Override
  public Optional<CommonTokenType> getCommonTokenType() {
    return Optional.ofNullable(commonType);
  }
}
```


## 2. Implement a LexerToken

This class represents an instance of each token - with *type* and *value* of the token. The *value* in 
this token class is generally the same as the value of the `TokenType` enum. Only for `ATOM` 
tokens the value will be something different. For `ATOM` tokens, the *value* will be whatever the 
value from the formula/script is.

Depending on the `LexerTokenFactory` implementation, this token class can also contain other 
information. The `LexerTokenFactory` can produce any `LexerToken` needed for your purpose.

It is recommended that this implementation is immutable.


```java
public class MyOwnToken implements LexerToken<MyOwnTokenType> {

  public final MyOwnTokenType type;
  public final String value;

  public MyOwnToken(MyOwnTokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public MyOwnTokenType getType() {
    return type;
  }

  @Override
  public String getValue() {
    return value;
  }
}
```


## 3. Implement a LexerTokenFactory

This factory provides a chance to bring some logic into the token creation. The example below is 
very basic and most likely the standard use case.

It is recommended that this implementation is state-less and immutable.

```java
public class MyOwnTokenFactory implements LexerTokenFactory<MyOwnToken, MyOwnTokenType> {

  @Override
  public MyOwnToken create(MyOwnTokenType tokenType, String value) {
    if (tokenType == MyOwnTokenType.SPACE) {
      // Ignore spaces
      return null;
    }
    
    return new MyOwnToken(tokenType, value);
  }
}
```


## 4. Use it


```java
MyOwnTokenFactory factory = new MyOwnTokenFactory();

String input = "A || B && C";

List<MyOwnToken> tokens = Lexer.lex(MyOwnTokenType.class, input, factory);
```


