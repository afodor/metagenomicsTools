package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

public class AbundOTUClustParser
{
	public static HashMap<String, String> getSequenceToOtuMap(String filepath)
		throws Exception
	{
		HashMap<String, String> returnMap = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				filepath)));
		
		String nextLine = reader.readLine();
		
		int index =0;
		while(nextLine != null)
		{
			if( ! nextLine.startsWith("#Consensus"))
				throw new Exception("Unexpected first line " + nextLine);
			
			StringTokenizer sToken = new StringTokenizer(nextLine, " \t=");
			sToken.nextToken();
			String otuName = sToken.nextToken();
			sToken.nextToken();  sToken.nextToken();
			/*int numToParse =*/ Integer.parseInt(sToken.nextToken());
			
			
			nextLine = reader.readLine();
			while( nextLine != null && ! nextLine.startsWith("#Consensus"))
			{
				sToken = new StringTokenizer(nextLine);
				sToken.nextToken();  sToken.nextToken();
				
				String seqId = sToken.nextToken();
				
				if( returnMap.containsKey(seqId))
					throw new Exception("Duplicat sequence " + seqId);
				
				returnMap.put( seqId, otuName );
				nextLine = reader.readLine();
				
				if( ++index % 1000000 == 0)
					System.out.println("Parsed " + index);
			}
			
		}
		
		reader.close();
		
		return returnMap;
	}
	
	public static HashMap<String, List<String>> getOTUToSeqIDMap( String filepath ) 
		throws Exception
	{
		HashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				filepath)));
		
		String nextLine = reader.readLine();
		
		int index =0;
		while(nextLine != null)
		{
			if( ! nextLine.startsWith("#Consensus"))
				throw new Exception("Unexpected first line " + nextLine);
			
			StringTokenizer sToken = new StringTokenizer(nextLine, " \t=");
			sToken.nextToken();
			String otuName = sToken.nextToken();
			sToken.nextToken();  sToken.nextToken();
			/*int numToParse =*/ Integer.parseInt(sToken.nextToken());
			
			if( map.containsKey(otuName) )
				throw new Exception("Duplicate name " + otuName);
			
			List<String> innerList = new ArrayList<String>();
			map.put(otuName, innerList);
			
			nextLine = reader.readLine();
			while( nextLine != null && ! nextLine.startsWith("#Consensus"))
			{
				sToken = new StringTokenizer(nextLine);
				sToken.nextToken();  sToken.nextToken();
				innerList.add(sToken.nextToken());
				nextLine = reader.readLine();
				
				if( ++index % 1000000 == 0)
					System.out.println("Parsed " + index);
			}
			
		}
		
		reader.close();
		return map;
	}
	
	
}
