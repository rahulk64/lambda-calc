import java.util.Scanner;

public class DEMO {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("READY PLAYER ONE");
		while(true) {
			String s = scan.nextLine();
			if(s.toLowerCase().equals("exit")) {
				System.out.print("Thanks for playing!");
				break;
			}
			
			// Parse that Query!
			try {
				
				QueryParser parser = new QueryParser(s);
				ParseTreeNode root = parser.parseQuery();
				
				TypeCheck tc = new TypeCheck();
				tc.checkType(root);
				System.out.println("YEET");
				
			} catch (IllegalQueryException e) {
				System.out.println("\n" + e.toString());
			}
			
			System.out.println("\nNew expression? ");
		}
		
	}
}
