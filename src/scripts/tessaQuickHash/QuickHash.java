package scripts.tessaQuickHash;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;


public class QuickHash
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, List<String>> map =  parseFile();
		writeResults(map);
	}
	
	private static void writeResults(HashMap<Integer, List<String>> map ) throws Exception
	{
		HashSet<String> antibiotcs = new HashSet<String>();
		
		for(Integer i : map.keySet())
			antibiotcs.addAll(map.get(i));
		
		List<String> sortedAntiB = new ArrayList<String>(antibiotcs);
		Collections.sort(sortedAntiB);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter( new File("c:\\Temp\\out.txt")));
		
		writer.write("subjectID");
		
		for(String s : sortedAntiB)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( Integer i  : map.keySet())
		{
			writer.write("" + i);
			
			List<String> innerList = map.get(i);
			
			for(String s: sortedAntiB)
			{
				if( innerList.contains(s))
					writer.write("\tyes");
				else
					writer.write("\tno");
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	// key is the patient identifier; values are lists of antibiotics
	private static HashMap<Integer, List<String>> parseFile() throws Exception
	{
		HashMap<Integer, List<String>>  map = new LinkedHashMap<Integer, List<String>>();
		
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
