package scripts.topeVicki;

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
import java.util.Map;
import java.util.StringTokenizer;
import utils.ConfigReader;

public class MergeAtLevel
{
	/*
	public static void main(String[] args) throws Exception
	{
		//String [] levels = { "p", "c","o","f","g" };
		String [] levels = { "p" };
		
		
		for(String level : levels)
		{
			Map<String, Map<String,Integer>> map = mergeAtLevel(level);
			File rawFile = writeFile(map, level);
			
			/*
			File loggedFile = new File(ConfigReader.getTopeVickiDir() + File.separator + level + "_mergedLogged.txt");
			
			OtuWrapper wrapper = new OtuWrapper(rawFile);
			wrapper.writeNormalizedLoggedDataToFile(loggedFile);
			*/
	//	}
//	}
	
	public static Map<String, Map<String,Integer>> mergeAtLevel(String level ) throws Exception
	{
		System.out.println(level);
		Map<String, Map<String,Integer>> map = new HashMap<>();
		
		File file1 = new File(ConfigReader.getTopeVickiDir() + File.separator + "raw1.txt");
		File file2 = new File(ConfigReader.getTopeVickiDir() + File.separator + "raw2.txt");
		
		addToMap(map, file1, level,"_1");
		addToMap(map, file2, level,"_2");
		
		return map;
	}
	
	private static void ensureNoDuplciates(File file) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(file));
		
		String[] splits =  reader.readLine().split("\t");
		
		HashSet<String> set = new HashSet<>();
		
		for(String s : splits)
		{
			if(set.contains(s))
				throw new Exception("Duplicate " + s);
			
			set.add(s);
		}
		
		reader.close();
	}
	 
	public static File writeFile( Map<String, Map<String,Integer>>  map, String level)
		throws Exception
	{
		//System.out.println("Writing with " + map.size());
		File file = new File(
				ConfigReader.getTopeVickiDir() + File.separator + level + "_mergedRaw.txt"	);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write("sample\tsequenceDepth");
		
		HashSet<String> taxa = new HashSet<>();
		for(String s : map.keySet())
		{
			taxa.addAll(map.get(s).keySet());
		}
		
		List<String> taxaList =new ArrayList<>(taxa);
		Collections.sort(taxaList);
		
		for(String s : taxaList)
			writer.write("\t" + s);
		
		writer.write("\n");
		

		int numWritten = 0;
		for(String s : map.keySet())
		{
			Map<String, Integer> innerMap = map.get(s);
			
			int allCount = 0;
			
			for(String t : taxaList)
			{
				Integer count = innerMap.get(t);
				
				if( count == null)
					count =0;
				
				allCount = allCount + count;
			}
			
			//System.out.println(s + "  " + allCount + " " + (allCount > 0) );
			//if( allCount > 0 )
			{
				numWritten++;
				writer.write(s + "\t" + allCount);
				
				for(String t : taxaList)
				{
					Integer count = innerMap.get(t);
					
					if( count == null)
						count =0;
					
					writer.write("\t" + count);
				}
				
				writer.write("\n");
			}
		}
		
		writer.flush();  writer.close();
		System.out.println("Wrote " + numWritten);
		
		return file;
	}
	
	private static void addToMap(Map<String, Map<String,Integer>>  map, File file, String level,
			String sampleSuffix) throws Exception
	{
		ensureNoDuplciates(file);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		String[] topSplits =reader.readLine().split("\t");
		//System.out.println("TS0" + topSplits[0] + " TS1" + topSplits[1]);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
				
			String taxa = getTaxa(s, level);
			//String[] checkSplits = s.split("\t");
			//System.out.println(taxa + " " + checkSplits[checkSplits.length-1]);
			
			if( taxa != null)
			{
				String[] splits = s.split("\t");
				
				if( splits.length != topSplits.length)
					throw new Exception("No");
				
				for( int x=1; x < splits.length-1; x++)
				{
					Map<String,Integer> innerMap = map.get(topSplits[x]+ sampleSuffix);
					
					if( innerMap == null)
					{
						innerMap = new HashMap<>();
						map.put(topSplits[x] + sampleSuffix, innerMap);				
					}
					
					if( x== 1 && Integer.parseInt(splits[x])> 1 )
						System.out.println(taxa + " " +  splits[0] + " " + splits[x] + " sample " + topSplits[x]);

					Integer oldVal = innerMap.get(taxa);
					
					if( oldVal == null)
						oldVal = 0;
					
					innerMap.put(taxa, oldVal + Integer.parseInt(splits[x]));
					
					if( Integer.parseInt(splits[x]) > 10 )
					{
						//System.out.println("check " + x + " " +  topSplits[x] + " " + taxa + " " +  Integer.parseInt(splits[x]) );
					}
				}
			}
		}
		
		reader.close();
		System.out.println("Returning with " + map.size());
	}
	
	private static String getTaxa(String s, String level) throws Exception
	{
		String[] splits = s.split("\t");
		String last = splits[splits.length -1];
		
		StringTokenizer sToken = new StringTokenizer(last,";");
		
		while(sToken.hasMoreTokens())
		{
			String taxa =sToken.nextToken().trim();
			
			if( taxa.equals("Unassigned"))
				return null;
			
			if( taxa.startsWith(level + "__"))
			{
				taxa = taxa.replace(level + "__", "" ).trim();
				
				if( taxa.length() == 0 || taxa.equals("Unassigned") )
					return null;
				
				return taxa;
			}
		}
		
		throw new Exception("Could not find " + level + " " + last);
		//return null;
	}
}
