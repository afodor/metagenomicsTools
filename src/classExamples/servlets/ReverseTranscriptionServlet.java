package fodor.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Modified from RequestParamExample that ships with Apache 7.0
 */
public class ReverseTranscriptionServlet extends HttpServlet
{
		public static final String DNA_NAME = "DNA_NAME";
	
		private String reverseTranscribe(String s)
		{
			s=s.toUpperCase();
			StringBuffer buff = new StringBuffer();
			
			for( int x=0; x < s.length(); x++)
			{
				char c = s.charAt(x);
				
				if( c == 'A'  )
					buff.append("T");
				else if ( c== 'C')
					buff.append("G");
				else if ( c== 'G')
					buff.append("C");
				else if ( c == 'T')
					buff.append("A");
				else
					buff.append(c);
			}
			
			return buff.toString();
		}
		
	    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	    {
	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        out.println("<html>");
	        out.println("<head>");
	        out.println("<title>Reverse Transcription Example</title>");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<h3>Reverse transcription Example</h3>");
	        String dnaName= request.getParameter(DNA_NAME);
	        
	        if (dnaName!= null )
	        {
	        	out.println("DNA sequence:");
	        	out.println(":" + dnaName + "<br>");
	        	// todo: User here can enter characters that will break the HTML going back
	        	// need to filter out those dangerous characters
	            out.println("reversed :" + reverseTranscribe( dnaName) + "<br>");

	        } else 
	        {
	            out.println("No sequence. Please enter one");
	        }
	        out.println("<P>");
	        out.print("<form action=\"");
	        out.print("ReverseTranscriptionServlet\" ");
	        out.println("method=POST>");
	        out.println("dna sequence:");
	        out.println("<textarea rows=20 cols=40 name=\"" + DNA_NAME + "\"></textarea>");
	        out.println("<br>");
	        out.println("<input type=submit>");
	        out.println("</form>");
	        out.println("</body>");
	        out.println("</html>");
	    }

	    public void doPost(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException
	    {
	        doGet(request, res);
	    }
}
