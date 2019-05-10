import java.util.HashMap;

public class TypeCheck {
	/** So we know what parameters we are looking for */
	private HashMap<String, ParseTreeNode> params;
	
	/**
	 * Checks to see if a node is currently a parameter of a function
	 * Evaluates the parameter's type as appropriate
	 * 
	 * @param node						 The node in question
	 * @throws IllegalQueryException     If the parameter already has a conflicting type
	 */
	private void checkParam(ParseTreeNode node) throws IllegalQueryException{
		
		if(node.getTokenType() != TokenType.NAME) 
			return;
		
		if(node.getNodeType() == NodeType.JOHN_CENA)
			return;
		
		if(params.containsKey(node.getToken().toString())) {
			ParseTreeNode param = params.get(node.getToken().toString());
			
			if(param.getNodeType() != NodeType.JOHN_CENA 
				&& param.getNodeType() != node.getNodeType()) 
				throw new IllegalQueryException("Multiple types for parameter " + param.getToken().toString());
			
			else 
				param.setNodeType(node.getNodeType());
		}
	}
	
	/** 
	 * Adds a parameter which we are now "looking for"
	 * 
	 * @param param     The parameter to be added to params
	 * @return          The previous entry in params or null if DNE
	 */
	private ParseTreeNode addParam(ParseTreeNode param) {
		String name = param.getToken().toString();
		if(params.containsKey(name)) {
			ParseTreeNode temp = params.get(name);
			params.remove(name);
			params.put(name, param);
			return temp;
		}
		else {
			params.put(name, param);
			return null;
		}
	}
	/**
	 * 
	 * Will throw an error if the types are not valid.
	 * 
	 * @param root    The root of the AST
	 */
	public void checkType(ParseTreeNode root) throws IllegalQueryException{
		params = new HashMap<String, ParseTreeNode>();
		applyTypes(root);
	}
	
	/**
	 * 
	 * Will throw an error if the types are not valid.
	 * Recursively traverses the tree making inferences to do this.
	 * 
	 * @param node     The current node being inspected
	 */
	private void applyTypes(ParseTreeNode node) throws IllegalQueryException {
		
		TokenType t = node.getTokenType();
		if(t == TokenType.NUM) 
			node.setNodeType(NodeType.NAT);
	
		else if(t == TokenType.TRUE || t == TokenType.FALSE)
			node.setNodeType(NodeType.BOOL);
		
		else if(t == TokenType.NAME)
			node.setNodeType(NodeType.JOHN_CENA);
	
		else if(t == TokenType.IF) {
			applyTypes(node.getLeft());
			applyTypes(node.getMiddle());
			applyTypes(node.getRight());
			
			if(node.getLeft().getNodeType() == NodeType.JOHN_CENA) {
				node.getLeft().setNodeType(NodeType.BOOL);
				checkParam(node.getLeft());
			}
			
			if(node.getLeft().getNodeType() != NodeType.BOOL)
				throw new IllegalQueryException("IF needs to take in a boolean!");
			
			if(node.getMiddle().getNodeType() != node.getRight().getNodeType())
				throw new IllegalQueryException("The types of THEN and ELSE must match!");
			
			node.setNodeType(node.getRight().getNodeType());
		}
		
		else if(t == TokenType.APP) {
			applyTypes(node.getLeft());
			applyTypes(node.getRight());
			
			if(node.getLeft().getTokenType() == TokenType.FUNC) {
				// John Cena?
				if(node.getLeft().getLeft().getNodeType() != node.getRight().getNodeType()
						&& node.getLeft().getLeft().getNodeType() != NodeType.JOHN_CENA
						&& node.getRight().getNodeType() != NodeType.JOHN_CENA)
					throw new IllegalQueryException("The parameter of a function must match the type of the argument given!");
				
				node.setNodeType(node.getLeft().getNodeType());
			}
			
			else {
				node.getLeft().setNodeType(NodeType.FUNC);
				node.setNodeType(NodeType.JOHN_CENA);
				checkParam(node.getLeft());
			}
		}
		
		else if(t == TokenType.FUNC) {
			applyTypes(node.getLeft());
			ParseTreeNode prevParam = addParam(node.getLeft());
			applyTypes(node.getRight());
			
			params.remove(node.getLeft().getToken().toString());
			if(prevParam != null)
				params.put(prevParam.getToken().toString(), prevParam);
			
			node.setNodeType(node.getRight().getNodeType());
		}
		
		else if(t == TokenType.PLUS) {
			applyTypes(node.getLeft());
			applyTypes(node.getRight());
			
			NodeType left = node.getLeft().getNodeType();
			NodeType right = node.getRight().getNodeType();
			
			if((left != NodeType.NAT && left != NodeType.JOHN_CENA) 
					|| (right != NodeType.NAT && right != NodeType.JOHN_CENA) )
				throw new IllegalQueryException("Illegal arguments for plus operator!");
			
			if(left == NodeType.JOHN_CENA) {
				node.getLeft().setNodeType(NodeType.NAT);
				checkParam(node.getLeft());
			}
			
			if(right == NodeType.JOHN_CENA) {
				node.getRight().setNodeType(NodeType.NAT);
				checkParam(node.getRight());
			}
				
			node.setNodeType(NodeType.NAT);
		}
		
		checkParam(node);
	}
}
