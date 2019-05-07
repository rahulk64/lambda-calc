
/**
 * @author GRANT SKAGGS
 * LAST EDIT: 12/10/2018
 * 
 * CS314H Programming Assignment 7 - WEB CRAWLER
 * 
 * Used by the WebQueryEngine to represent a token during the parsing of query strings.
 *
 */
public class Token 
{
	
	// Each token has a type and a string representation
	private TokenType type;
	private String tokenString;
	
	// If the string represntation of token is a word,
	// then this constructor will be called.
	public Token(TokenType t, String word)
	{
		type = t;
		tokenString = word;
	}
	
	// If the token is an operator, then the string represen-
	// tation of the token will be a single character.
	public Token(TokenType t, Character oper)
	{
		type = t;
		tokenString = oper.toString();
	}
	
	// Simple getter methods
	public TokenType getType()
	{
		return type;
	}
	
	@Override
	// Returns the string representation of the token
	public String toString()
	{
		return tokenString;
	}
}

