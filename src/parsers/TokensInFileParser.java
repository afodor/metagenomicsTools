package parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class TokensInFileParser
{
	private BufferedReader reader;
	private StringTokenizer sToken;
	
	public TokensInFileParser(String filePath) throws Exception
	{
		this.reader = new BufferedReader(new FileReader(filePath));
		
		String nextLine = reader.readLine();
		
		while( nextLine.startsWith("#"))
			nextLine = reader.readLine();
		
		this.sToken = new StringTokenizer(nextLine);
	}
	
	public String nextToken() throws Exception
	{
		if( sToken.hasMoreTokens())
			return sToken.nextToken();
		
		while(true)
		{
			String nextLine = reader.readLine();
			
			while( nextLine != null && nextLine.startsWith("#"))
				nextLine = reader.readLine();
			
			if( nextLine == null)
			{
				reader.close();
				return null;
			}
				
			sToken = new StringTokenizer(nextLine);
			
			if( sToken.hasMoreTokens())
				return sToken.nextToken();
		}
	}
}
