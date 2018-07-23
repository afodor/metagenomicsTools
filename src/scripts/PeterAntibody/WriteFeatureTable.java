package scripts.PeterAntibody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WriteFeatureTable
{
	public static final String[] AMINO_ACID_CHARS=
		{"A"	,"C",	"D",	"E",	"F",	"G", 	
				"H",	"I",	"L",	"K",	"M"	, "N",	"P",	"Q",	"R",	"S"	, "T",	"V"
				,"W"	,"Y"};
	
	public static final String[] CHAINS=  {"LC", "HC" };
	
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String,Double> map = buildMap();
		writeFeatureTable(map);
	}
	
	private static HashMap<String,Double> buildMap() throws Exception
	{
		HashMap<String,Double> map = new HashMap<>();
		
		
		for( String c : CHAINS)
			for( int x=1; x <=6; x++)
			{
				 addToMap(c, x, map);
			}
		
		return map;
	}
	
	private static List<String> extractPositions(HashMap<String,Double> map) throws Exception
	{
		HashSet<String> set =new HashSet<>();
		
		for(String s : map.keySet())
		{
			String[] splits =s.split("_");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			set.add(splits[1]);
		}
		
		List<String> list =new ArrayList<>(set);
		Collections.sort(list);
		
		return list;
	}
	

	private static List<String> extractClassifications(HashMap<String,Double> map) throws Exception
	{
		HashSet<String> set =new HashSet<>();
		
		for(String s : map.keySet())
		{
			String[] splits =s.split("_");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			set.add(splits[0]);
		}
		
		List<String> list =new ArrayList<>(set);
		Collections.sort(list);
		
		return list;
	}
	private static void writeFeatureTable(HashMap<String,Double> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"combinedFeatureTable.txt")));
		
		writer.write("classification\tposition");
		
		List<String> positions= extractPositions(map);
		
		for(String s : positions)
			for( int x=0; x < AMINO_ACID_CHARS.length; x++)
				writer.write("\t" + s + "_" +  AMINO_ACID_CHARS[x] );
		
		writer.write("\n");
		
		List<String> classifications = extractClassifications(map);
		
		for(String c : classifications)
		{

			writer.write(c );
			
			for(String p : positions)
			{
				for(String s : positions)	
					for( String aa : AMINO_ACID_CHARS)
					{
						String key = c + "_" + p + "_"+ aa;
						
						if( !map.containsKey(key))
							writer.write("\tNA");
						else
							writer.write("\t" + map.get(key));
					}	
			}
			
			writer.write("\n");
			
		}
		
		writer.flush();  writer.close();
	}
	
	
	// outer key is classification_position_aaChar
	private static void addToMap(String hcLc, int num,HashMap<String,Double> map) throws Exception
	{
		String chotString = "Chotia";
		
		if( hcLc.equals("HC"))
			chotString = "Chothia";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				hcLc + "_STATS_"  + chotString + "_" + num + ".txt" )));
		
		String firstLine = reader.readLine();
		String[] firstSplits = firstLine.split("\t");
		
		StringTokenizer sToken = new StringTokenizer( firstSplits[0].trim().replace(" " , "-"));
		
		String classificationKey = sToken.nextToken();
		System.out.println(classificationKey);
		
		for(String s= reader.readLine() ; s != null && s.trim().length()>0; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			int startPos = 23;
			String position = splits[startPos];
			
			//System.out.println(position);
			
			for( int x=0; x < AMINO_ACID_CHARS.length; x++)
			{
				double val = Double.parseDouble(splits[startPos + x + 1]);
				String key = classificationKey + "_" + position + "_" + AMINO_ACID_CHARS[x];
				
				//System.out.println(key);
				if( map.containsKey(key))
					throw new Exception("No " + key);
				
				map.put(key, val);
			}
			
		}
		
		reader.close();
	}
}
