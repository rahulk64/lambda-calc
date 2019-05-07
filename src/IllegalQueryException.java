
/**
 * @author GRANT SKAGGS
 * LAST EDIT: 12/10/2018
 * 
 * CS314H Programming Assignment 7 - WEB CRAWLER
 * 
 * Used by the QueryParser class to inform objects of the
 * WebQueryEngine class that the current query is invalid 
 *
 */
public class IllegalQueryException extends Exception 
{

	/**
	 * Default serialization number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A constructor for the case where a message is not given.
	 */
	public IllegalQueryException() { }
	
	/**
	 * Calls the superclass Exception's constructor to set the 
	 * object's message String.
	 * 
	 * @param message		A message to be displayed to the user in 
	 * 						the case of an illegal query error being thrown;
	 */
	public IllegalQueryException(String message)
	{
		super(message);
	}
	
	
}