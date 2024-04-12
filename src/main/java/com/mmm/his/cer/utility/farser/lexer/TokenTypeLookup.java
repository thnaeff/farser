package com.mmm.his.cer.utility.farser.lexer;

import com.mmm.his.cer.utility.farser.CommonTokenFlag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class which provides lookup maps for {@link TokenType}s.
 *
 * @author Thomas Naeff
 */
public class TokenTypeLookup {

  private static final Map<Class<?>, Map<String, TokenType<?>>> valueLookupMap = new HashMap<>();
  private static final Map<Class<?>, TokenType<?>> atomTokenLookupMap = new HashMap<>();
  private static final Map<Class<TokenType<?>>, Pattern> patternLookupMap = new HashMap<>();

  private static final Pattern wordCharPattern = Pattern.compile("\\w");


  private TokenTypeLookup() {
    // Constructor not needed. Only static methods.
  }

  /**
   * Retrieves the map with token values as keys and the tokens as values.<br />
   * This map can be used for easier lookup of tokens via value.<br />
   * The map is only created once for the given enum class and is retrieved from a cache for
   * subsequent lookups.
   *
   * @param enumClass The token enumeration class
   * @return An unmodifiable map with all the lookup entries
   * @throws IllegalArgumentException If the token type class is not an enumeration
   * @throws FarserException          If there are duplicate keys based on the token values
   */
  protected static <T extends TokenType<?>> Map<String, T> getValueLookupMap(Class<T> enumClass) {

    if (!enumClass.isEnum()) {
      throw new IllegalArgumentException(enumClass.getName() + " has to be an enumeration");
    }

    // If lookup already exists just do the lookup.
    if (valueLookupMap.containsKey(enumClass)) {
      // Safe to suppress. Map is created in this method for the given enum class.
      @SuppressWarnings("unchecked")
      Map<String, T> lookupMap = (Map<String, T>) valueLookupMap.get(enumClass);
      return lookupMap;
    }

    Map<String, T> lookupMap = buildValueLookupMap(enumClass);

    // Save to cast. Lookup happens above.
    @SuppressWarnings("unchecked")
    Map<String, TokenType<?>> lookupMapTmp = (Map<String, TokenType<?>>) lookupMap;

    // Save to cast. Lookup happens above.
    @SuppressWarnings("unchecked")
    Class<TokenType<?>> enumClassTmp = (Class<TokenType<?>>) enumClass;

    // Store as unmodifiable map because it should never change again.
    valueLookupMap.put(enumClassTmp, Collections.unmodifiableMap(lookupMapTmp));
    return lookupMap;
  }

  /**
   * Creates the map for token lookups via value.
   *
   * @param enumClass The {@link TokenType} enumeration class
   * @return The map with the values and tokens of the given enumeration class
   */
  private static <T extends TokenType<?>> Map<String, T> buildValueLookupMap(Class<T> enumClass) {

    // Build lookup map
    Map<String, T> lookupMap = new HashMap<>();
    for (T enumConst : enumClass.getEnumConstants()) {
      if (enumConst.isEqual(CommonTokenType.SPACE)) {
        // For legacy reasons, add a space manually. The 'SPACE' Javadoc used to state that no value
        // is needed for the 'SPACE' token.
        lookupMap.put(" ", enumConst);
        continue;
      }
      Optional<String> value = enumConst.getValue();
      if (value.isPresent()) {
        String key = value.get();
        if (lookupMap.containsKey(key)) {
          throw new FarserException(
              "Duplicate keys are not allowed. Key '"
                  + key
                  + "' alredy exists for "
                  + lookupMap.get(key).getClass().getName()
                  + "."
                  + enumConst.name());
        }
        lookupMap.put(key, enumConst);
      }
    }

    return lookupMap;
  }

  /**
   * Retrieves the {@link CommonTokenType#ATOM} from the provided enum class. Lookups are cached.
   *
   * @param <T>       The token type
   * @param enumClass The enum class which
   * @return The atom token. Never <code>null</code>.
   * @throws IllegalArgumentException If the class is not an enum
   * @throws FarserException          If none or multiple {@link CommonTokenType#ATOM} are defined
   *                                  in the enum and not just a single one
   */
  protected static <T extends TokenType<?>> T getAtomToken(Class<T> enumClass) {

    if (!enumClass.isEnum()) {
      throw new IllegalArgumentException(enumClass.getName() + " has to be an enumeration");
    }

    if (atomTokenLookupMap.containsKey(enumClass)) {
      // Safe to suppress. Map is created in this method for the given enum class.
      @SuppressWarnings("unchecked")
      T tmpToken = (T) atomTokenLookupMap.get(enumClass);
      return tmpToken;
    }

    // Create the cache and validate occurrences
    T atomToken = null;
    for (T token : enumClass.getEnumConstants()) {
      if (token.isEqual(CommonTokenType.ATOM)) {
        if (!atomTokenLookupMap.containsKey(enumClass)) {
          atomToken = token;
          atomTokenLookupMap.put(enumClass, token);
        } else {
          throw new FarserException("More than one token is marked as common type "
              + CommonTokenType.ATOM
              + " in "
              + enumClass);
        }
      }
    }

    if (atomToken == null) {
      throw new FarserException("No common type "
          + CommonTokenType.ATOM
          + " found in "
          + enumClass
          + ". This type is mandatory.");
    }

    return atomToken;
  }

  /**
   * Creates the map for token lookups via {@link CommonTokenType}.
   *
   * @param enumClass The {@link TokenType} enumeration
   * @throws FarserException If the enum class or lookup map have invalid content like mandatory
   *                         common token types missing etc.
   */
  private static void validateCommonTypeLookupMap(Class<? extends TokenType<?>> enumClass) {

    Set<CommonTokenFlag> existingCommonTypes = new HashSet<>();
    for (TokenType<?> enumConst : enumClass.getEnumConstants()) {
      enumConst.getCommonTokenType().ifPresent(existingCommonTypes::add);
    }

    // Check that all mandatory common types exist
    for (CommonTokenType commonType : CommonTokenType.values()) {
      if (commonType.isMandatory() && !existingCommonTypes.contains(commonType)) {
        throw new FarserException(commonType.getClass().getName()
            + "."
            + commonType.name()
            + " is mandatory. No token found in "
            + enumClass.getName()
            + " which is marked with this mandatory common type.");
      }
    }

  }

  /**
   * Creates a RegEx OR pattern ("A|B|C...") based on all the token values in the provided
   * {@link TokenType} class.<br />
   * Tokens with no value are ignored.<br />
   * The pattern is only created once for the given enum class and is retrieved from a cache for
   * subsequent lookups.
   *
   * @param enumClass The token type enumeration class
   * @return The RegEx pattern
   * @throws IllegalArgumentException If the token type class is not an enumeration
   */
  public static Pattern getPattern(Class<? extends TokenType<?>> enumClass) {

    if (!enumClass.isEnum()) {
      throw new IllegalArgumentException(enumClass.getName() + " has to be an enumeration");
    }

    // If lookup already exists just do the lookup.
    if (patternLookupMap.containsKey(enumClass)) {
      return patternLookupMap.get(enumClass);
    }

    // Check that all mandatory common types are used
    validateCommonTypeLookupMap(enumClass);

    Pattern pattern = buildPattern(enumClass);

    // Save to cast. Lookup happens above.
    @SuppressWarnings("unchecked")
    Class<TokenType<?>> enumClassTmp = (Class<TokenType<?>>) enumClass;

    patternLookupMap.put(enumClassTmp, pattern);
    return pattern;
  }

  /**
   * Builds the pattern based off the given {@link TokenType} enumeration class.<br />
   * The pattern is build based on all token values in the enumeration excluding ones marked with
   * {@link CommonTokenType#ATOM} and {@link CommonTokenType#SPACE}.
   *
   * @param enumClass The enumeration class
   * @return The RegEx pattern
   */
  private static Pattern buildPattern(Class<? extends TokenType<?>> enumClass) {

    List<String> tokens = new ArrayList<>();
    List<String> separators = new ArrayList<>();
    for (TokenType<?> tokenType : enumClass.getEnumConstants()) {
      Optional<CommonTokenFlag> commonType = tokenType.getCommonTokenType();
      if (commonType.isPresent()) {
        CommonTokenFlag commonFlag = commonType.get();
        if (commonFlag == CommonTokenType.SPACE) {
          // For legacy reasons (where the javadoc states that the space type does not need a
          // value), add a space.
          separators.add(" ");
        } else if (commonFlag == CommonTokenType.SEPARATOR_CHARACTER) {
          // Only all non-null values
          tokenType.getValue().ifPresent(separators::add);
        } else if (commonFlag != CommonTokenType.ATOM) {
          // Only the ones which are not ATOMs or SPACE/SEPARATOR and are non-null
          tokenType.getValue().ifPresent(tokens::add);
        }
      } else {
        // Only all non-null values without any common type
        tokenType.getValue().ifPresent(tokens::add);
      }
    }


    StringJoiner fullPattern = new StringJoiner("|");

    // Set the separator patterns as very first symbol token element for most importance.
    String separatorPattern = buildSeparatorPattern(separators);
    if (!separatorPattern.isEmpty()) {
      fullPattern.add(separatorPattern);
    }

    String tokenPattern = buildTokenPattern(tokens);
    if (!tokenPattern.isEmpty()) {
      fullPattern.add(tokenPattern);
    }

    return Pattern.compile(fullPattern.toString(), Pattern.CASE_INSENSITIVE);
  }

  /**
   * Builds the regex pattern for all the tokens (the non-ATOM and non-SEPARATOR tokens).
   *
   * @param tokens The tokens for which to build the pattern
   * @return The pattern
   */
  private static String buildTokenPattern(List<String> tokens) {
    // Sort by longest token first (for highest precedence), shortest last.
    Collections.sort(tokens, Collections.reverseOrder(Comparable::compareTo));

    // Joiner for all tokens which are symbols
    StringJoiner sjSymbol = new StringJoiner("|", "(", ")");
    // Joiner for all tokens which contain characters. They need word boundaries.
    StringJoiner sjCharacters = new StringJoiner("|", "\\b(", ")\\b");
    int symbolCount = 0;
    int charCount = 0;

    for (String token : tokens) {
      if (containsWordChar(token)) {
        // A token with word characters
        sjCharacters.add(Pattern.quote(token));
        charCount++;
      } else {
        // A token without word characters
        sjSymbol.add(Pattern.quote(token));
        symbolCount++;
      }
    }

    StringJoiner sjPattern = new StringJoiner("|");
    if (symbolCount > 0) {
      sjPattern.add(sjSymbol.toString());
    }
    if (charCount > 0) {
      sjPattern.add(sjCharacters.toString());
    }

    return sjPattern.toString();
  }

  /**
   *
   *
   * @param separators
   * @return
   */
  private static String buildSeparatorPattern(List<String> separators) {
    StringJoiner sjPattern = new StringJoiner("|");

    for (String separator : separators) {
      StringBuilder sb = new StringBuilder();
      sb.append("(?:[");
      sb.append(separator);
      sb.append("])+");
      sjPattern.add(sb);
    }

    return sjPattern.toString();
  }

  /**
   * Trims the separator token - which may be a repetition of the same separator character (e.g.
   * ";;;" or " "). The result is a single-character separator (the first character only).<br>
   * This is the counterpart to {@link #buildSeparatorPattern(List)}.
   *
   * @param str The separator token string to trim. May be an empty string.
   * @return A single character separator token, or an empty string.
   */
  public static String trimSeparatorToken(String str) {
    return str.isEmpty() ? "" : str.substring(0, 1);
  }

  /**
   * Checks if the given string contains word characters.
   *
   * @param str The string to check
   * @return Whether or not the string contains word characters
   */
  private static boolean containsWordChar(String str) {
    Matcher matcher = wordCharPattern.matcher(str);
    // Find the first occurrence of the pattern
    return matcher.find();
  }

}
