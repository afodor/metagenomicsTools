package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

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
	
	public static void abundantOTUToSparseThreeColumn(String abundantOTUClust, String outFile,
			HashMap<String, String> sequenceToSampleMap) throws Exception
	{
		File in = new File(abundantOTUClust);
		
		BufferedReader reader = 
			abundantOTUClust.toLowerCase().endsWith("gz") ?
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( in) ) ))
				:
					new BufferedReader(new FileReader(in));

		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
				
		String nextLine = reader.readLine();
				
		while(nextLine != null)
		{
			if( ! nextLine.startsWith("#Consensus"))
				throw new Exception("Unexpected first line " + nextLine);
	
			HashMap<String, Integer> countMap = new HashMap<String, Integer>();
					
			StringTokenizer sToken = new StringTokenizer(nextLine, " \t=");
			sToken.nextToken();
			String otuName = sToken.nextToken();
			System.out.println(otuName);
			sToken.nextToken();  sToken.nextToken();
			Integer.parseInt(sToken.nextToken());
					
			nextLine = reader.readLine();
			while( nextLine != null && ! nextLine.startsWith("#Consensus"))
			{
				sToken = new StringTokenizer(nextLine);
				sToken.nextToken();  sToken.nextToken();
				String seqID = sToken.nextToken();
				
				String sampleId = sequenceToSampleMap.get(seqID);
				
				if( sampleId == null)
					throw new Exception("could not find " + sampleId);
				
				Integer count = countMap.get(sampleId);
				
				if( count == null)
					count =0;
				
				count++;
				
				countMap.put(sampleId, count);
				
				nextLine = reader.readLine();
			}
			
			for(String s : countMap.keySet())
			{
				writer.write(s + "\t" + otuName + "\t" + countMap.get(s) + "\n");
			}
			
			writer.flush();
		}
			
	writer.flush(); writer.close();
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
