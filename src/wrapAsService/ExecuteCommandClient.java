package wrapAsService;

import java.io.PrintWriter;
import java.net.Socket;

public class ExecuteCommandClient
{
	public static void main(String[] args) throws Exception
	{
		Socket aSocket = new Socket("127.0.0.1", ExecuteCommandServer.PORT);
		
		PrintWriter out = new PrintWriter(aSocket.getOutputStream(), true);
		
		out.write("Hello there " + System.currentTimeMillis());
		out.flush();  out.close(); aSocket.close();
	}
}
