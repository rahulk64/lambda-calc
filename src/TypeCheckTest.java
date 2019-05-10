import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TypeCheckTest {

	@Test
	void test() throws NullPointerException, IllegalQueryException {
		
		// A basic word query
		QueryParser parser = new QueryParser("42 + 42");
		ParseTreeNode root = parser.parseQuery();
		
		TypeCheck tc = new TypeCheck();
		tc.checkType(root);
		
		assertEquals(root.getNodeType(), NodeType.NAT);
		assertEquals(root.getLeft().getNodeType(), NodeType.NAT);
		assertEquals(root.getRight().getNodeType(), NodeType.NAT);
		
		// Inference of parameter type
		parser = new QueryParser("(\\ f -> if ( f ) then ( 0 ) else ( 1 ) ) ");
		root = parser.parseQuery();	
		tc.checkType(root);
		
		assertEquals(root.getNodeType(), NodeType.NAT);
		assertEquals(root.getLeft().getNodeType(), NodeType.BOOL);
		
		
		// John Cena!
		parser = new QueryParser("( \\ f -> ( \\ g -> ( f + g ) ) ) a b ");
		root = parser.parseQuery();
		tc.checkType(root);
		
		
		
	}

}
