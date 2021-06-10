package scripts.tessaQuickHash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class QuickHash
{
	public static void main(String[] args) throws Exception
	{
		System.out.println(parseFile());
	}
	
	
	// key is the patient identifier; values are lists of antibiotics
	private static HashMap<Integer, List<String>> parseFile() throws Exception
	{
		HashMap<Integer, List<String>>  map = new HashMap<Integer, List<String>>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader("c:\\Temp\\apple.txt"));
		
		boolean isNewRecord = true;
		
		List<String> innerList = null;
		Integer subjectRecord = null;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken();
			
			if( isNewRecord )
			{
				subjectRecord = Integer.parseInt(sToken.nextToken());
				
				if( sToken.hasMoreTokens())
					throw new Exception("Parsing error");
				
				//System.out.println(subjectRecord);
				
				isNewRecord =false;
				innerList = new ArrayList<String>();
			}
			else
			{
				String nextToken = sToken.nextToken();
				
				if( nextToken.equals("break"))
				{
					isNewRecord =true;
					map.put(subjectRecord, innerList);
				}
				else
				{
					innerList.add(nextToken);
				}
			}
		}
	
		reader.close();
		return map;
	}
}
