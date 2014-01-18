package parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class TokensInFileParser
{
	private final BufferedReader reader;
	private StringTokenizer sToken;
	private final String delims;
	
	public TokensInFileParser(String filePath, String delims) throws Exception
	{
		this.delims = delims;
		this.reader = new BufferedReader(new FileReader(filePath));
		
		String nextLine = reader.readLine();
		
		while( nextLine.startsWith("#"))
			nextLine = reader.readLine();
		
		this.sToken = new StringTokenizer(nextLine,delims);
	}
	
	public String nextToken() throws Exception
	{
		if( sToken.hasMoreTokens())
			return sToken.nextToken().trim();
		
		while(true)
		{
			String nextLine = reader.readLine();
			//System.out.println(nextLine);
			
			while( nextLine != null && nextLine.startsWith("#"))
				nextLine = reader.readLine();
			
			if( nextLine == null)
			{
				reader.close();
				return null;
			}
				
			sToken = new StringTokenizer(nextLine,delims);
			
			if( sToken.hasMoreTokens())
				return sToken.nextToken().trim();
		}
	}
}
