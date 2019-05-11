import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QueryParserTest {

	@Test
	public void getTokensTest() throws NullPointerException, IllegalQueryException 
	{
		// Getting basic word token
		QueryParser parser = new QueryParser("word");
		String stream = "word";
		
		Token t = parser.getToken();
		
		assertEquals(stream, t.toString());
		
		// Should be able to ignore extra white space
		parser = new QueryParser("    word");
		stream = "word";
		
		t = parser.getToken();
		
		assertEquals(stream, t.toString());
		
		
		// Something a little more complex
		parser = new QueryParser("\\f -> ( \\g -> ( \\x -> ( f x ) ( g x ) ) )");
		
		assertEquals(TokenType.LAMBDA, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.ARROW, parser.getToken().getType());
		assertEquals(TokenType.LEFT_PAREN, parser.getToken().getType());
		assertEquals(TokenType.LAMBDA, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.ARROW, parser.getToken().getType());
		assertEquals(TokenType.LEFT_PAREN, parser.getToken().getType());
		assertEquals(TokenType.LAMBDA, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.ARROW, parser.getToken().getType());
		assertEquals(TokenType.LEFT_PAREN, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.RIGHT_PAREN, parser.getToken().getType());
		assertEquals(TokenType.LEFT_PAREN, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.RIGHT_PAREN, parser.getToken().getType());
		assertEquals(TokenType.RIGHT_PAREN, parser.getToken().getType());
		assertEquals(TokenType.RIGHT_PAREN, parser.getToken().getType());
		
		 // No more tokens should return null
		assertEquals(null, parser.getToken());
		
		//if statement
		parser = new QueryParser("if name then exp1 else exp2");
		assertEquals(TokenType.IF, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.THEN, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.ELSE, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		
		assertEquals(null, parser.getToken());
		
		//numbers
		parser = new QueryParser("42");
		assertEquals(TokenType.NUM, parser.getToken().getType());
		
		//more complex numbers
		parser = new QueryParser("exp 42 42t");
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.NUM, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		
		//true & false
		parser = new QueryParser("True False true false");
		assertEquals(TokenType.TRUE, parser.getToken().getType());
		assertEquals(TokenType.FALSE, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		
		//plus operator
		parser = new QueryParser("42 + 45");
		assertEquals(TokenType.NUM, parser.getToken().getType());
		assertEquals(TokenType.PLUS, parser.getToken().getType());
		assertEquals(TokenType.NUM, parser.getToken().getType());
		
		//let
		parser = new QueryParser("let name = 42");
		assertEquals(TokenType.LET, parser.getToken().getType());
		assertEquals(TokenType.NAME, parser.getToken().getType());
		assertEquals(TokenType.EQUAL, parser.getToken().getType());
		assertEquals(TokenType.NUM, parser.getToken().getType());
		
	}

	@Test
	public void QueryParserTest() throws NullPointerException, IllegalQueryException {
		// A basic word query
		QueryParser parser = new QueryParser("word");
		ParseTreeNode root = parser.parseQuery();
		
		assertEquals("word", root.getToken().toString());
		
		// Root should have no children
		assertEquals(null, root.getLeft());
		assertEquals(null, root.getRight());
		
		
		// Simple query with one lambda operator
		parser = new QueryParser("\\ word1 -> word2");
		root = parser.parseQuery();
		
		ParseTreeNode node1 = root.getLeft();
		ParseTreeNode node2 = root.getRight();
		
		assertEquals(TokenType.FUNC, root.getToken().getType());
		assertEquals("word1", node1.getToken().toString());
		assertEquals("word2", node2.getToken().toString());
		
		
		// More complex query: Using implicitSubTree() function
		parser = new QueryParser("\\ word1 -> word2 word3 word4");
		root = parser.parseQuery();
		
		node1 = root.getLeft();
		node2 = root.getRight();
		ParseTreeNode node3 = node2.getLeft();  //APP
		ParseTreeNode node4 = node2.getRight(); //4
		ParseTreeNode node5 = node3.getLeft();  //2
		ParseTreeNode node6 = node3.getRight(); //3
		
		assertEquals(TokenType.FUNC, root.getToken().getType());
		assertEquals("word1", node1.getToken().toString());
		assertEquals(TokenType.APP, node2.getToken().getType());
		assertEquals(TokenType.APP, node3.getToken().getType());
		assertEquals("word2", node5.getToken().toString());
		assertEquals("word3", node6.getToken().toString());
		assertEquals("word4", node4.getToken().toString());
		
		// A LEGEND AMONG US
		parser = new QueryParser("\\f -> ( \\g -> ( \\x ->  f x  ( g JOHN_CENA h ) ) )");
		
		root = parser.parseQuery(); 			// FUNC
		node1 = root.getLeft();					// f
		node2 = root.getRight();				// FUNC
		node3 = node2.getLeft();				// g
		node4 = node2.getRight();				// FUNC
		node5 = node4.getLeft();  				// x
		node6 = node4.getRight(); 				// APP
		ParseTreeNode node7 = node6.getLeft();  // APP
		ParseTreeNode node8 = node7.getLeft();  // f
		ParseTreeNode node9 = node7.getRight(); // x
		ParseTreeNode node10 = node6.getRight();// APP									
		ParseTreeNode node11 = node10.getLeft();// APP
		ParseTreeNode node12 = node10.getRight();// h
		ParseTreeNode node13 = node11.getLeft(); // g
		ParseTreeNode node14 = node11.getRight();// JOHN_CENA
		
		
		assertEquals(TokenType.FUNC, root.getToken().getType());
		assertEquals("f", node1.getToken().toString());
		assertEquals("g", node3.getToken().toString());
		assertEquals("x", node5.getToken().toString());
		assertEquals("f", node8.getToken().toString());
		assertEquals("x", node9.getToken().toString());
		assertEquals("g", node13.getToken().toString());
		assertEquals("h", node12.getToken().toString());
		assertEquals("JOHN_CENA", node14.getToken().toString());
		assertEquals(TokenType.FUNC, node2.getToken().getType());
		assertEquals(TokenType.FUNC, node4.getToken().getType());
		assertEquals(TokenType.APP, node6.getToken().getType());
		assertEquals(TokenType.APP, node7.getToken().getType());
		assertEquals(TokenType.APP, node10.getToken().getType());
		assertEquals(TokenType.APP, node11.getToken().getType());
		
		//Basic if statement
		parser = new QueryParser("if ( True ) then ( exp1 ) else ( exp2 )");
		
		root = parser.parseQuery();					//IF
		ParseTreeNode lchild = root.getLeft();		//name
		ParseTreeNode mchild = root.getMiddle();	//exp1
		ParseTreeNode rchild = root.getRight();		//exp2
		
		assertEquals(TokenType.IF, root.getToken().getType());
		assertEquals("True", lchild.getToken().toString());
		assertEquals("exp1", mchild.getToken().toString());
		assertEquals("exp2", rchild.getToken().toString());
		
		assertEquals(TokenType.TRUE, lchild.getToken().getType());
		assertEquals(TokenType.NAME, mchild.getToken().getType());
		assertEquals(TokenType.NAME, rchild.getToken().getType());
		
		
		//complex if statement with a number
		parser = new QueryParser("if ( name 42 ) then ( exp1 exp2 ) else ( \\x -> x )");
		
		root = parser.parseQuery();					//IF
		lchild = root.getLeft();					//name
		mchild = root.getMiddle();					//exp1
		rchild = root.getRight();					//exp2
		
		assertEquals(TokenType.IF, root.getToken().getType());
		assertEquals("APP", lchild.getToken().toString());
		assertEquals("name", lchild.getLeft().getToken().toString());
		assertEquals("42", lchild.getRight().getToken().toString());
		assertEquals("APP", mchild.getToken().toString());
		assertEquals("exp1", mchild.getLeft().getToken().toString());
		assertEquals("exp2", mchild.getRight().getToken().toString());
		assertEquals("FUNC", rchild.getToken().toString());
		assertEquals("x", rchild.getLeft().getToken().toString());
		assertEquals("x", rchild.getRight().getToken().toString());
		
		assertEquals(TokenType.APP, lchild.getToken().getType());
		assertEquals(TokenType.NAME, lchild.getLeft().getToken().getType());
		assertEquals(TokenType.NUM, lchild.getRight().getToken().getType());
		assertEquals(TokenType.APP, mchild.getToken().getType());
		assertEquals(TokenType.FUNC, rchild.getToken().getType());
		
		//plus operator
		parser = new QueryParser("42 + 45");
		
		root = parser.parseQuery();
		lchild = root.getLeft();
		rchild = root.getRight();
		
		assertEquals(TokenType.PLUS, root.getToken().getType());
		assertEquals("42", lchild.getToken().toString());
		assertEquals("45", rchild.getToken().toString());
		
		assertEquals(TokenType.NUM, lchild.getTokenType());
		assertEquals(TokenType.NUM, rchild.getTokenType());
		
		//let
		parser = new QueryParser("let name = 42");
		root = parser.parseQuery();
		
		assertEquals(null, root);
		assertEquals(1, parser.map.size());
		
		ParseTreeNode name = parser.map.get("name");
		assertEquals("42", name.toString());
		assertEquals(TokenType.NUM, name.getTokenType());
		
		//more complex let
		parser = new QueryParser("let exp = \\x -> ( x + 4 )");
		root = parser.parseQuery();
		assertEquals(null, root);
		assertEquals(1, parser.map.size());
		
		ParseTreeNode exp = parser.map.get("exp");
		assertNotNull(exp);
		assertEquals(TokenType.FUNC, exp.getTokenType());
		assertEquals(TokenType.NAME, exp.getLeft().getTokenType());
		assertEquals(TokenType.PLUS, exp.getRight().getTokenType());
		assertEquals(TokenType.NAME, exp.getRight().getLeft().getTokenType());
		assertEquals(TokenType.NUM, exp.getRight().getRight().getTokenType());
		
	}
}
