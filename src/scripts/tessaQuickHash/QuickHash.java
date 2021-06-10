package scripts.tessaQuickHash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class QuickHash
{
	public static void main(String[] args) throws Exception
	{
		parseFile();
	}
	
	// key is the patient identifier; values are lists of antibiotics
	private static HashMap<String, List<String>> parseFile() throws Exception
	{
		HashMap<String, List<String>>  map = new HashMap<String, List<String>>();
		
		BufferedReader reader = new BufferedReader(new FileReader("c:\\Temp\\apple.txt"));
		
		boolean isNewRecord = true;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken();
			
			if( isNewRecord )
			{
				Integer subjectRecord = Integer.parseInt(sToken.nextToken());
				
				if( sToken.hasMoreTokens())
					throw new Exception("Parsing error");
				
				System.out.println(subjectRecord);
				
				isNewRecord =false;
			}
			else
			{
				String nextToken = sToken.nextToken();
				
				if( nextToken.equals("break"))
					isNewRecord =true;
				
			}
		}
		
		
		reader.close();
		return map;
	}
}
