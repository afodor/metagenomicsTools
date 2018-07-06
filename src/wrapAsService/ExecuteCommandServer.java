package wrapAsService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ExecuteCommandServer
{
	public static final int PORT = 3491;
	
	public static void main(String[] args) throws Exception
	{
		ServerSocket sSocket = new ServerSocket(PORT);
		
		while(true)
		{
			Socket aSocket =  sSocket.accept();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(aSocket.getInputStream()));
			
			String aLine = null;
			
			 while ((aLine= in.readLine()) != null) {
				 System.out.println(aLine);

			    }
			 
			 System.out.println("Closing");
			 in.close();
			 aSocket.close();
			
		}
	}
}
