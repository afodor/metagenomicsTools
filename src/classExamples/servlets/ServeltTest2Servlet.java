package classExamples.servlets;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ServeltTest2Servlet extends HttpServlet
{
	// not thread safe and broken
	int count =1;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		resp.getWriter().println("This is a test servlet from Anthony Fodor");
		resp.getWriter().println("This is invocation number " + count);
		count++;
	}
}
