/*
 * Author: Joseph Steiger
 * Date: 11/13/2020
 * Known Compatible Operating Systems: Windows 10
 * 
 * Purpose:
 * 		Demonstrate connecting to the Yubico Admin API
 * 
 * Outside Resources Required:
 * 		Active API token
 * 
 * References:
 * 		1) YubiEnterprise Delivery Public API: https://console.yubico.com/apidocs/
 * 		2) Executing Shell Commands with Java: https://stackabuse.com/executing-shell-commands-with-java/#:~:text=If%20you'd%20like%20to,%3A%5C%5CUsers%5C%5C%22))%3B%20%2F%2F.
 * 		3) YubiEnterprise Delivery: http://sfutdwiki.siteleaf.net/phase2/yubienterprise-delivery/
 * 
 * Helpful Links:
 * 		1) curl man page: https://curl.se/docs/manpage.html
 * 		2) Get Started with YubiEnterprise Delivery: https://www.youtube.com/watch?v=IHw5Qt-r-qM
 * 		
 */

import java.io.InputStreamReader; //used to process the output
import java.util.Scanner; //used to output recieved data

/******************************************************************************
 * Name: APIConnectionTest
 * Methods: main(String[])
 * Purpose:
 * 		Demo a connection between a program and the YubiEnterprise Delivery API
 * 
 ******************************************************************************/
public class APIConnectionTest {
	
	/*
	 * Name: main
	 * Parameters: args (String[])
	 * Purpose: 
	 * 		Connect to the YubiEnterprise Delivery API and display output
	 * 
	 */
	public static void main(String[] args) {
		
		//Wrap everything in a try/catch block in case anything fails
		try{
			//Create 'in' object to output the data to the screen
			Scanner in; 
			//Create a process obj. Run the exec(String) function to run a command on the command line
			//exec(String) runs the String passed into it as a command on the command line
			//The command we will be running is below
			//curl -H "Authorization: Bearer <TOKEN> https://api.console.yubico.com/v1/shipments
			//This specific example uses GET to obtain the list for all shipments and information regarding them.
/*
 *************
 * IMPORTANT *
 *************
 *When running this command for your self, replace the token with your own active token
 */
			Process process = Runtime.getRuntime().exec("curl -H \"Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwczovL2FwaS5jb25zb2xlLnl1Ymljby5jb20iLCJleHAiOjE2MzYyMTkxMjYsImlhdCI6MTYwNDY4MzEyNiwianRpIjoiWHNyMkJ6UXNNSlJhWjhpZGFqa1NYOSIsImxsdCI6dHJ1ZSwib3JnIjoic3RhdGUtZmFybS11cyIsIm9yZ3MiOm51bGwsInNjcCI6Im9yZy1hdWRpdG9yIiwic3ViIjoiamFzMTcxMDMwQHV0ZGFsbGFzLmVkdSJ9.w5luzjF1lvrkZw5PR6CKcJGFWt9TnPIkqoy2DnfNQGc\" https://api.console.yubico.com/v1/shipments");
			//Use the getInputStream() function to capture the output of the command
			//Convert that InputStream into an InputStreamReader
			//Finally convert the InputStreamReader into a Scanner and link it to 'in'
			in = new Scanner(new InputStreamReader(process.getInputStream()));
			//Print out all of the output to stdout
			while(in.hasNext()){
				System.out.println(in.next());
			}
			//Close the Scanner obj to prevent a memory leak
			in.close();
		} catch(Exception e){ //If any exception is thrown
			//Print out that it failed and the exceptions error message
			System.out.println("Failed. \n" + e.getMessage());
		}
	}

}
