package scripts.compareGrant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class QiimeRDPParse
{
	public static void main(String[] args) throws Exception
	{	
		checkAllFiles();
	}
	
	private static void checkAllFiles() throws Exception
	{
		HashMap<String, HashMap<String,Double>>  expectedMap = getExpectedMap();
		
		String[] files = new File( ConfigReader.getGrantCheckDir()).list();
		
		for(String s : files)
		{
			if( s.startsWith("s10_2019Jun05_otuCount_") && s.endsWith(".tsv"))
			{
				String sampleID = s.replace("s10_2019Jun05_otuCount_", "").replace(".tsv", "");
				System.out.println("Starting " + sampleID);
				
				HashMap<String, Double> innerMap = expectedMap.get(sampleID);
				
				if( innerMap == null )
					throw new Exception("Could not find " +sampleID );

				for(String s2 : innerMap.keySet())
					System.out.println(s2);
				
				BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getGrantCheckDir() + File.separator + s	)));
				
				for(String s2= reader.readLine(); s2 != null; s2= reader.readLine())
				{
					StringTokenizer sToken = new StringTokenizer(s2, "\t");
					
					if( sToken.countTokens() != 2)
						throw new Exception("Expecting two tokens");
					
					String key = sToken.nextToken();
					
					if( key.toLowerCase().indexOf("unclassified")== -1)
					{
						Double val = Double.parseDouble(sToken.nextToken());
						Double foundCount = innerMap.get(key);
						
						if( foundCount == null )
							throw new Exception("Could not find " + key +  " " + val);
						//else
							//System.out.println("found " + key);
						
						if( Math.abs(foundCount - val) > 0.001)
							throw new Exception("Failed to match " + foundCount + " " + val );
					}
					
				}
				
				reader.close();
				System.out.println("\t\tPASS " + sampleID + "\n\n\n");
			}
		}
	}
	
	// outer key is the sample 
	private static HashMap<String, HashMap<String,Double>> getExpectedMap() throws Exception
	{
		HashMap<String, HashMap<String,Double>>  map =new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getGrantCheckDir() + File.separator + "otu_table_L6.txt")));
		
		reader.readLine();
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
		{
			HashMap<String,Double> innerMap =new LinkedHashMap<>();
			
			map.put(topSplits[x], innerMap);
		}
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String taxa = convertToTree( splits[0]);
			
			for( int x=1; x < splits.length; x++)
			{
				HashMap<String, Double> innerMap = map.get(topSplits[x]);
				
				if( innerMap.containsKey(taxa))
					throw new Exception("Duplicate" );
				
				innerMap.put(taxa, Double.parseDouble(splits[x]));
			}
		}
		
		return map;
	}
	
	private static String convertToTree(String s)
	{
		s = s.replaceAll(";", "|");
		s = s.replace("D_0__Bacteria|", "").replace("D_0__Archaea|", "");
		
		String[] vals = { "", "phylum", "class", "order", "family", "genus" };
		
		for( int x=1; x<=5; x++)
			s = s.replace("D_" + x, vals[x]);
		
		return s;
	}
	
}
