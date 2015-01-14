

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;


public class PropertyExplorer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Console c = System.console();
//        if (c == null) {
//            System.err.println("No console.");
//            System.exit(1);
//        }

        int resultState = processRequests();
		
	}

	private static int processRequests() {
		boolean continueLoop = true;
		int resultState= 0;
		while(continueLoop)
        {
//        	String input = c.readLine("Enter your request: ");
        	
        	System.out.println("Enter your request (q -> quit): ");
        	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        	String input;
			try {
				input = bufferedReader.readLine();
				if(input.equals("q")) continueLoop = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return resultState;
	}
	
}
