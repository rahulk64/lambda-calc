	/**
	 * A token can be of the following types:
	 * NAME, LAMBDA, ARROW, LEFT PARENTHESIS, RIGHT PARENTHESIS
	 */
	public enum TokenType
	{
		/**
		 * A name is a function (our only type for now)
		 */
		NAME,
		
		/**
		 * Lambda grammar operators
		 */
		LAMBDA,
		
		ARROW, 
		
		/**
		 * Parenthesis: LEFT and RIGHT
		 */
		LEFT_PAREN, 
		
		RIGHT_PAREN,
		
		
		/***********************************************
		 * 
		 * Don't mind your casual sketchy tokens
		 * 
		 ***********************************************/
		
		
		/**
		 * Grammar operators
		 */
		FUNC,
		
		APP,
		
		/**
		 * Typed tokens
		 */
		
		IF,
		THEN,
		ELSE,
		
		PLUS,
		
		NUM,
		
		TRUE,
		FALSE,
		
		/**
		 * LEGEND
		 */
		JOHN_CENA
	
	
	}
	