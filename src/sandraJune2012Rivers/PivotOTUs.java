package sandraJune2012Rivers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;

import utils.ConfigReader;

public class PivotOTUs
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>>  map = getMap(
				ConfigReader.getSandraRiverJune2012Dir()  
				+ File.separator +"sandra_Otu_ToSampleFilePrimerTrimmed.txt");

		writeResults(map,ConfigReader.getSandraRiverJune2012Dir()  
					+File.separator + "sandra_OTU_AsColumns.txt", new MyOTUComparator(),true);
			
		for( int x=1; x <6; x++ )
		{
			 map = getMap(
					 ConfigReader.getSandraRiverJune2012Dir()  
						+ File.separator +"sandra_" + NewRDPParserFileLine.TAXA_ARRAY[x] +"_ToSampleFilePrimerTrimmed.txt");
			 
			writeResults(map,ConfigReader.getSandraRiverJune2012Dir()  
					+ File.separator +"sandra_" + NewRDPParserFileLine.TAXA_ARRAY[x]  + "_AsColumns.txt", new MyRDPComparator(),false);
		}
	}
	
	private static List<String> getOTUSAtThreshold(
			HashMap<String, HashMap<String, Integer>>  map,
									int threshold, Comparator<String> comparator) throws Exception
	{
		
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		
		for( String s: map.keySet() )
		{
			HashMap<String, Integer> innerMap = map.get(s);
				
			for(String possibleOtu : innerMap.keySet())
			{
				Integer oldCount = countMap.get(possibleOtu);
					
				if(oldCount == null)
						oldCount = 0;
					
				oldCount += innerMap.get(possibleOtu);
					
				countMap.put(possibleOtu, oldCount);
			}
		}
			
		List<String> otuList= new ArrayList<String>();
		
		for( String s : countMap.keySet() )
			if( countMap.get(s) >= threshold )
				otuList.add(s);
		
		Collections.sort(otuList, comparator);
		
		return otuList;
	
	}
	
	private static class MyOTUComparator implements Comparator<String>
	{
		@Override
		public int compare(String arg0, String arg1)
		{
			int int1= Integer.parseInt(arg0.replaceAll("Consensus", ""));
			int int2= Integer.parseInt(arg1.replaceAll("Consensus", ""));
			
			return int1-int2;
		}
	}
	
	private static class MyRDPComparator implements Comparator<String>
	{
		@Override
		public int compare(String arg0, String arg1)
		{
			return arg0.compareTo(arg1);
		}
	}
	
	public static void writeResults(HashMap<String, HashMap<String, Integer>>  map, String filepath, Comparator<String> comparator,
			boolean prefixConsensus) 
							throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			filepath)));
		
		writer.write("sample");
		List<String> otuList = getOTUSAtThreshold(map, 0, comparator);
		
		for( String s : otuList)
			writer.write("\t" + (prefixConsensus ? "Consensus" : "" )  + s);
		
		writer.write("\n");
		
		List<String> keys = new ArrayList<String>(map.keySet());
		
		for( String s : keys)
		{
			//String expandedString = PivotRDPs.getExpandedString( s);
			//writer.write( expandedString );
			writer.write(s );
				
			HashMap<String, Integer> innerMap = map.get(s);
				
			for( String otu : otuList)
			{
				Integer aVal = innerMap.get(otu);
					
				if(aVal== null)
					aVal = 0;
					
				writer.write("\t" + aVal );
			}
				
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	public static HashMap<String, HashMap<String, Integer>> getMap(String filePath) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = 
			new HashMap<String, HashMap<String,Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath
				)));
			
		reader.readLine();
			
		String nextLine = reader.readLine();
		
		int total =0;
		while(nextLine != null)
		{
			StringTokenizer sToken = new StringTokenizer(nextLine, "\t");
			sToken.nextToken();
			String sample = sToken.nextToken();
			String otu = sToken.nextToken();
			
			if( sToken.hasMoreTokens())
				throw new Exception("No " + nextLine);
			
			HashMap<String, Integer> innerMap = map.get(sample);
			if( innerMap == null)
			{
				innerMap = new HashMap<String, Integer>();
				map.put(sample, innerMap);
			}
			
			Integer aValue = innerMap.get(otu);
			
			if( aValue == null)
				aValue =0;
			
			aValue++;
			
			innerMap.put(otu, aValue);
			nextLine = reader.readLine();
			total++;
			
			if( total % 100000==0)
				System.out.println(total);
		}
		
		return map;
	}
}
