package scripts.compareEngel.WuRelease;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import scripts.compareEngel.kraken.CompareKraken;
import scripts.pancreatitis.PivotOTUs;
import utils.ConfigReader;

public class PivotToSpreadsheet
{
	private static HashMap<String, HashMap<String, Integer>> getMapForLevel(String level) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = new LinkedHashMap<>();
		
		File aDir = new File(ConfigReader.getEngelCheckDir() + File.separator + "krakenCheck" );
		
		String[] files = aDir.list();
		
		for(String fileName: files)
		{
			if(fileName.endsWith("_reported.tsv"))
			{
				String sampleName = new StringTokenizer(fileName, "_").nextToken();
				
				if( map.containsKey(sampleName))
					throw new Exception("No");
				
				File file = new File(aDir.getAbsolutePath() + File.separator + fileName);
				
				HashMap<String, Integer> innerMap = CompareKraken.getExpectedAtLevel(file, "" + level.charAt(0));
				
				map.put(sampleName, innerMap);
			}
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( String level :  CompareKraken.LEVELS)
		{
			System.out.println(level);
			
			HashMap<String, HashMap<String, Integer>> map = getMapForLevel(level);
			
			File outFile = new File(ConfigReader.getEngelCheckDir() +File.separator + "krakenCheck" + File.separator + 
						"mikeRelease" + File.separator + "kraken_" + level + ".txt");
			
			PivotOTUs.writeResults(map, outFile.getAbsolutePath());
			
		}
	}
	
}
