
public class ParseTreeNode 
{
	// The node's left and right children
	private ParseTreeNode left, right, middle; //middle only used for if
	private Token token;
	private NodeType nodeType;
	
	/**
	 * Constructor for a node with two children.
	 * 
	 * @param t			Token to be held by the node
	 * @param left		Node's left child
	 * @param right		Node's right child
	 */
	public ParseTreeNode(Token t, ParseTreeNode left, ParseTreeNode right)
	{
		token = t;
		nodeType = null;
		this.left = left;
		this.right = right;
		this.middle = null;
	}
	
	public ParseTreeNode(Token t, ParseTreeNode left, ParseTreeNode middle, ParseTreeNode right) {
		assert t.getType() == TokenType.IF;
		
		token = t;
		this.left = left;
		this.right = right;
		this.middle = middle;
	}
	
	/**
	 * Constructor for a node with no children. 
	 * The left and right instance variables representing the children
	 * will be left null.
	 * 
	 * @param t 		Token to be held by the node
	 */
	public ParseTreeNode(Token t)
	{
		token = t;
	}
	
	/**
	 * A simple getter method to return the node's token value
	 * 
	 * @return 		The token held by the node.
	 */
	public Token getToken()
	{
		return token;
	}
	
	/**
	 * A simple getter method to determine the type of the token held by
	 * the node object.
	 * 
	 * @return 		The type of the token held by the parse tree node.
	 */
	public TokenType getTokenType()
	{
		return token.getType();
	}
	
	/**
	 * A simple getter method to determine the type of the node held by
	 * the node object.
	 * 
	 * @return 		The type of the token held by the parse tree node.
	 */
	public NodeType getNodeType()
	{
		return nodeType;
	}
	
	/**
	 * A simple setter method to determine the type of the node held by
	 * the node object.
	 * 
	 * @return 		The type of the token held by the parse tree node.
	 */
	public void setNodeType(NodeType n)
	{
		nodeType = n;
	}
	
	/**
	 * A simple getter method to retrieve the node's left child
	 * 
	 * @return 		The left child of the parse tree node
	 */
	public ParseTreeNode getLeft()
	{
		return left;
	}
	
	/**
	 * A simple getter method to retrieve the node's right child
	 * 
	 * @return 		The right child of the parse tree node
	 */
	public ParseTreeNode getRight()
	{
		return right;
	}
	
	public ParseTreeNode getMiddle()
	{
		if(getTokenType() == TokenType.IF)
			return middle;
		else return null;
	}
	
	/**
	 * Used by the WebQueryEngine to get the word held within a node
	 * of the parse tree.
	 */
	public String toString()
	{
		return token.toString();
	}
}
