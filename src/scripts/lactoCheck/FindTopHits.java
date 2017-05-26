package scripts.lactoCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

import parsers.HitScores;
import utils.ConfigReader;

public class FindTopHits
{
	private static HashMap<String, HashSet<Integer>> getAccessionToOTUID() throws Exception
	{
		HashMap<String, HashSet<Integer>> map = new HashMap<String, HashSet<Integer>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
				"gg_13_5_accessions.txt"
					)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != 3)
				throw new Exception("No");
			
			HashSet<Integer> innerSet = map.get(splits[2]);
			
			if(innerSet == null)
			{
				innerSet = new HashSet<Integer>();
				map.put(splits[2], innerSet);
			}
			
			innerSet.add(Integer.parseInt(splits[0]));
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashSet<Integer>> accessionToGreengenesMap =
				getAccessionToOTUID();
		
		HashMap<String, HitScores> topHitsMap = 
				HitScores.getTopHitsAsQueryMap(ConfigReader.getLactoCheckDir() + File.separator + 
						"otusToInersMatchingByBlast.txt");
		
		for(String s : topHitsMap.keySet())
		{
			HitScores hs = topHitsMap.get(s);
			
			if( hs.getPercentIdentity() > 99)
			{
				System.out.println(s + " " + hs.getQueryId() + " " + hs.getAlignmentLength() + " "+ 
						hs.getPercentIdentity());
				
				System.out.println(accessionToGreengenesMap.get(hs.getQueryId()));
			}
				
		}
	}
}
