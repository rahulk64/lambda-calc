import java.util.HashMap;

public class QueryParser 
{
	private String stream;
	public HashMap<String, ParseTreeNode> map;
	
	/**
	 * Constructs a new QueryParserObject on a given query String.
	 * Trims the query String and makes it all lower case for parsing.
	 * 
	 * @param query					The query to be parsed
	 * @throw NullPointerException 	If the query parameter is null.
	 */
	public QueryParser(String query) throws NullPointerException, IllegalQueryException
	{
		map = new HashMap<String, ParseTreeNode>();
		stream = query;
		stream = stream.trim();
		//stream = stream.toLowerCase();
		//stream = removeInvalidChars(stream);
		
		if (stream.length() == 0)
			throw new IllegalQueryException ("Oh no! The given query was empty.");
	}
	
	/**
     * Returns the next Token object to be found in the current string being queried.
     * Used during the recursive descent parser to create a parse tree.
     * 
     * Assumption: 		The String stream has no white space at the beginning and has 
     * 					already been converted to all lower case.
     * 
     * @param stream	The current string being parsed to be queried
     * @return			The next Token object in the stream
     */
    public Token getToken()
    {	
    	if (stream == null || stream.length() == 0)
    		return null;
    	
    	char c = stream.charAt(0);
    	stream = stream.substring(1);
    	
    	Token token; // The token object to be returned
    	
    	if (c == '\\')
    		token  = new Token (TokenType.LAMBDA, c);
    	
    	else if (c == '-' && stream.length() > 0 && stream.charAt(0) == '>') {
    		stream = stream.substring(1);
    		token = new Token (TokenType.ARROW, "->");
    	}
    	
    	else if (c == '(')
    		token =  new Token (TokenType.LEFT_PAREN, c);
    	
    	else if (c == ')')
    		token = new Token (TokenType.RIGHT_PAREN, c);
    	
    	else if (c == 'i' && stream.length() > 0 && stream.charAt(0) == 'f') {
    		stream = stream.substring(1);
    		token = new Token(TokenType.IF, "if");
    	}
    	
    	else if (c == 't' && stream.length() > 2 && stream.substring(0, 3).equals("hen")) {
    		stream = stream.substring(3);
    		token = new Token(TokenType.THEN, "then");
    	}
    	
    	else if (c == 'e' && stream.length() > 2 && stream.substring(0, 3).equals("lse")) {
    		stream = stream.substring(3);
    		token = new Token(TokenType.ELSE, "else");
    	}
    	
    	else if (c == 'T' && stream.length() > 2 && stream.substring(0, 3).equals("rue")) {
    		stream = stream.substring(3);
    		token = new Token(TokenType.TRUE, "True");
    	}
    	
    	else if (c == 'F' && stream.length() > 3 && stream.substring(0, 4).equals("alse")) {
    		stream = stream.substring(4);
    		token = new Token(TokenType.FALSE, "False");
    	}
    	
    	else if (c == '+')
    		token = new Token(TokenType.PLUS, '+');
    	
    	else if (c == 'l' && stream.length() > 1 && stream.substring(0, 2).equals("et")) {
    		stream = stream.substring(2);
    		token = new Token(TokenType.LET, "let");
    	}
    	
    	else if (c == '=')
    		token = new Token(TokenType.EQUAL, '=');
    	
    	else
    	{
    		// We are building a name or number
    		StringBuilder word = new StringBuilder();
    		
    		word.append(c);

    		
			while (stream.length() > 0)
			{
				c = stream.charAt(0);
				stream = stream.substring(1);
				
				// If we hit an operator, a paren, or a blank space,
				// then we're done building the word and we break from loop
				if (c == '\\' || c == '-' || c == '>' || c == '(' || c == ')' || c == ' ')
				{
					stream = c + stream;
					break;
				}
				word.append(c);
			}
    		
    		if(isInteger(word.toString())) {
    			token = new Token(TokenType.NUM, word.toString());
    		} else {
    			token = new Token (TokenType.NAME, word.toString());
    		}
    	}
    	
    	// Ensures the stream maintains the property of being trimmed
    	stream = stream.trim();
    	
    	return token;
    }
    
    /**
     * Creates a parse tree from the query string passed as parameter.
     * Uses the implicitParseTree method to call itself recursively.
     * Destroys the String stream in the process.
     * 
     * Assumption: 		The String stream has no white space at the beginning 
     * 					and has already been converted to all lower case.
     * 
     * 					LAMBDA functions must begin and end with parens!
     * 
     * @return			A ParseTreeNode object representing some sub-tree of 
     * 					the recursively constructed parse tree.
     * 
     * @throws IllegalQueryException		If the query parameter is invalid.
     */
    public ParseTreeNode parseQuery() throws IllegalQueryException
    {  	
    	ParseTreeNode prev = null, cur = null;
    	
    	while(stream.length() > 0) {
    		Token token = getToken();
    		TokenType type = token.getType();
    		
    		if(prev == null) { 			// first token in the expression
    			
    			if(type == TokenType.LAMBDA) {
    				// We got a lambda function!
    	    		ParseTreeNode left = new ParseTreeNode(getToken()); 	// Single argument
    	    		Token arrow = getToken();								// Arrow operator
    	    		ParseTreeNode right = parseQuery();						// Some expression
    	    		
    	    		Token FuncToken = new Token(TokenType.FUNC, "FUNC");
    	    		cur = new ParseTreeNode(FuncToken, left, right);
    	    		
    	    		return cur;
    			}
    				
    			else if(type == TokenType.NAME || type == TokenType.NUM
    					|| type == TokenType.TRUE || type == TokenType.FALSE) 
    				cur = new ParseTreeNode(token);
    			
    			else if(type == TokenType.LEFT_PAREN)
    				cur = parseQuery();
    			
    			else if(type == TokenType.RIGHT_PAREN)
    				throw new IllegalQueryException("Empty Expression!");
    			
    			else if(type == TokenType.PLUS) 
    				throw new IllegalQueryException("+ not allowed at beginning!");
    			
    			else if(type == TokenType.EQUAL) 
    				throw new IllegalQueryException("= not allowed at beginning!");
    			
    			else if(type == TokenType.IF) {
    				ParseTreeNode lif = parseQuery();
    				
    				getToken(); //THEN
    				
    				ParseTreeNode mthen = parseQuery();
    				
    				getToken(); //ELSE
    				
    				ParseTreeNode relse = parseQuery();
    				
    				Token IfToken = new Token(TokenType.IF, "IF");
    				cur = new ParseTreeNode(IfToken, lif, mthen, relse);
    				
    				return cur;
    			}
    			
    			else if(type == TokenType.LET) {
    				Token tok = getToken(); //name
    				if(tok.getType() != TokenType.NAME) {
    					throw new IllegalQueryException("Invalid Let Usage");
    				}
    				
    				String name = tok.toString();
    				
    				getToken(); // = 
    				
    				ParseTreeNode letexp = parseQuery();
    				
    				map.put(name, letexp);
    				
    				return null;
    			}
    		}
    		
    		else { 						// Not the first token in the expression
    			
    			if(type == TokenType.LAMBDA || type == TokenType.ARROW) 
    				throw new IllegalQueryException("Invalid Lambda function");
    			
    			else if(type == TokenType.NAME || type == TokenType.NUM
    					|| type == TokenType.TRUE || type == TokenType.FALSE) {
    				cur = new ParseTreeNode(token);
    				Token AppToken = new Token(TokenType.APP, "APP");
    				cur = new ParseTreeNode(AppToken, prev, cur);
    			}
    			
    			else if(type == TokenType.LEFT_PAREN) {
    				cur = parseQuery();
    				Token AppToken = new Token(TokenType.APP, "APP");
    				cur = new ParseTreeNode(AppToken, prev, cur);
    			}
    			
    			else if(type == TokenType.RIGHT_PAREN) {
    				break;
    			}
    			
    			else if(type == TokenType.THEN || type == TokenType.ELSE) {
    				break;
    			}
    			
    			else if (type == TokenType.IF) {
    				throw new IllegalQueryException("Invalid if expression");
    			}
    			
    			else if (type == TokenType.LET) {
    				throw new IllegalQueryException("Can't use \"let\" in middle of command");
    			}
    			
    			else if (type == TokenType.PLUS) {
    				Token PlusToken = new Token(TokenType.PLUS, "+");
    				ParseTreeNode remaining = parseQuery();
    				
    				cur = new ParseTreeNode(PlusToken, prev, remaining);
    			}
    			
    			else if(type == TokenType.EQUAL) {
    				throw new IllegalQueryException("Invalid use of = operator");
    			}
    			
    		}
    		
    		prev = cur;
    	}
    	
    	return cur;
    }
    
    /**
     * Determines whether or not the current place in the stream is an "Implicit AND Query."
     * Constructs an appropriate sub tree of the parse tree based on this possibility.
     * 
     * Assumption: 		The String stream has no white space at the beginning 
     * 					and has already been converted to all lower case.
     * 
     * @parameter		The last ParseTreeNode object to have been identified.
     * @return			A ParseTreeNode object representing some sub-tree of 
     * 					the recursively constructed parse tree based on the 
     * 					possibility of an implicit AND in the query. 
     * 
     * @throws IllegalQueryException 	The parseQuery method may determine the
     * 									query to be of invalid grammar.
     */
    private ParseTreeNode implicitSubTree(ParseTreeNode curNode) throws IllegalQueryException
    {
    	// Case 1: We've reached the end of the query; we're done!
    	if (stream.length() == 0)
    		return curNode;
    	
    	char c = stream.charAt(0);
    	
    	// Case 2: The next token will be a boolean operator or a close parens;
    	// This means there is no "implicit AND" structure.
    	if (c == ')')
			return curNode;
    	
    	// Case 3: The next token will be an open parens, a word, or a negation;
    	// This means there is an "implicit AND" structure.
		else
		{
			Token AppToken = new Token(TokenType.APP, "APP");
			return new ParseTreeNode(AppToken, curNode, parseQuery());
		}
    }
    
    /**
     * Identifies all the characters which are not operators or valid word characters,
     * replacing them with spaces.
     *  
     * @param query		The string being modified.
     * @return			A new String with the invalid characters replaced.
     */
    public String removeInvalidChars(String query)
    {
    	char[] chars = query.toCharArray();
    	
    	// Loop through query. If a character is not an operation 
    	// and is not part of a valid word, then it becomes a space.
    	for (int i = 0; i < chars.length; i++)
    	{
    		char c = chars[i];
    		if (c != '!' && c != '(' && c != ')' && c != '\'' && c != '"' && c != ' ' && c != '&' && c != '|')
    		{
    			// I defined a word to be any combination of alphanumeric characters, as well as the apostrophe.
    			if (!(c >= 'a' && c <= 'z') && !(c >= '0' && c <= '9') && (c != '\''))
    				chars[i] = ' ';
    		}
    	}
    	
    	return new String(chars);
    }
    
    public boolean isInteger(String s) {
    	try {
    		int i = Integer.parseInt(s);
    	} catch(NumberFormatException | NullPointerException e) {
    		return false;
    	}
    	
    	return true;
    }
}
