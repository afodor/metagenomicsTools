package weka;

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

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class BringTwoARFIntoSameNamespace
{
	private static HashMap<String,Integer> getNumericAttributes( File file ) throws Exception
	{
		HashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int x=0;
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if( s.startsWith("@attribute") && s.endsWith("numeric"))
			{
				StringTokenizer sToken = new StringTokenizer(s);
				sToken.nextToken();
				
				String key = new String( sToken.nextToken());
				
				if( map.containsKey(key))
					throw new Exception("Duplicate key");
				
				map.put(new String(key),x);
				x++;
			}
		}
		reader.close();
		
		return map;
	}
	
	private static List<String> getMergedList(HashMap<String, Integer> map1, 
									HashMap<String, Integer> map2 )
	{
		HashSet<String> set = new HashSet<>(map1.keySet());
		set.addAll(map2.keySet());
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		return list;
	}
	
	private static HashMap<Integer, String> flipMap( HashMap<String, Integer> inMap ) throws Exception
	{
		HashMap<Integer, String> map = new HashMap<Integer,String>();
		
		for(String s : inMap.keySet())
		{
			Integer i = inMap.get(s);
			
			if( map.containsKey(i))
				throw new Exception("Logic error");
			
			map.put(i, s);
		}
		
		return map;
	}
	
	private static HashMap<String, Integer> getNewPositionMap(List<String> list ) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		for( int x=0;x < list.size(); x++)
		{
			if(map.containsKey(list.get(x)))
					throw new Exception("Logic error");
			
			map.put(list.get(x), x);
		}
		
		return map;
	}
	
	private static String getNewLine(String oldLine, HashMap<Integer, String> flipMap,
						HashMap<String, Integer> newPositionMap) throws Exception
	{
		double[] vals = new double[newPositionMap.size()];
		
		String[] splits = oldLine.split(",");
		
		if( splits.length -1 != flipMap.size())
			throw new Exception("Parsing error");
		
		for( int x=0; x < splits.length - 1; x++)
		{
			String key = flipMap.get(x);
			Integer newPosition = newPositionMap.get(key);
			
			if( newPosition == null)
				throw new Exception("Could not find " + key);
			
			vals[newPosition] = Double.parseDouble(splits[x]);
			
		}
		
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < vals.length; x++)
			buff.append(vals[x] + ",");
		
		buff.append(splits[splits.length-1]  + "\n");
		
		return buff.toString();
	}
	
	private static void writeMerged( File originalFile, File newFile, List<String> mergedList, 
						HashMap<String, Integer> map) throws Exception
	{
		HashMap<Integer, String> flipMap = flipMap(map);
		HashMap<String, Integer> positionMap = getNewPositionMap(mergedList);
		
		BufferedReader reader = new BufferedReader(new FileReader(originalFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
		
		writer.write(reader.readLine() + "\n");  // header comment line
		writer.write(reader.readLine() + "\n");  // @relation
		
		for( int x=0; x < mergedList.size(); x++)
			writer.write("@attribute " + mergedList.get(x) + " numeric\n");
		writer.write("\n\n");
		
		
		String nextLine = reader.readLine();
		
		while(! nextLine.startsWith("@data"))
			nextLine = reader.readLine();
		
		writer.write(nextLine + "\n");
		writer.write(reader.readLine() + "\n");  // %
		writer.write(reader.readLine() + "\n");  // number of instances
		writer.write(reader.readLine() + "\n");  //
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			writer.write(getNewLine(s, flipMap, positionMap));
		}
		
		reader.close();
		writer.flush(); writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
				File logNormalMetadata1 = new File(ConfigReader.getAdenomasReleaseDir() + File.separator + 
						"spreadsheets" + File.separator + 
						"pivoted_" +  NewRDPParserFileLine.TAXA_ARRAY[x] + 
						"LogNormalWithMetadata.arff");
				
				File newFile1 = new File(ConfigReader.getAdenomasReleaseDir() + File.separator + 
						"spreadsheets" + File.separator + 
						"pivoted_" +  NewRDPParserFileLine.TAXA_ARRAY[x] + 
						"LogNormalWithMetadataBigSpace.arff");
				
				HashMap<String, Integer> map1 = 
						getNumericAttributes(logNormalMetadata1);
				
				File logNormalMetadata2 = new File(ConfigReader.getTopeSep2015Dir() + File.separator +
						"spreadsheets" + File.separator + 
						NewRDPParserFileLine.TAXA_ARRAY[x]
								+ "asColumnsLogNormalPlusMetadataFilteredCaseControl.arff");
				
				HashMap<String, Integer> map2 = 
						getNumericAttributes(logNormalMetadata2);
				
				List<String> list = getMergedList(map1, map2);
				
				writeMerged(logNormalMetadata1, newFile1, list, map1);
		}
	}
}
