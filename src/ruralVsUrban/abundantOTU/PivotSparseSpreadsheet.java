package ruralVsUrban.abundantOTU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.OtuWrapper;
import parsers.PivotOTUs;
import utils.ConfigReader;

public class PivotSparseSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = 
		getMap(ConfigReader.getChinaDir() + File.separator + "abundantOTU" + File.separator + 
				"sparseForwardThreeFileColumn.txt");
		
		String outPath = ConfigReader.getChinaDir() + 
				File.separator + "abundantOTU" + File.separator + 
				"abundantOTUForwardTaxaAsColumns.txt";
		
		PivotOTUs.writeResults(map, outPath);
		
		OtuWrapper wrapper = new OtuWrapper(outPath);
		wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getChinaDir() + 
				File.separator + "abundantOTU" + File.separator + 
				"abundantOTUForwardTaxaAsColumnsLogNormal.txt");
	}
	
	/*
	 * Pivot file in the format 
	 * sampleID1     Bacteroidaceae  6325680
	   sampleID2   Lachnospiraceae 1117978
 */
	public static HashMap<String, HashMap<String, Integer>> getMap(String filePath) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = 
			new HashMap<String, HashMap<String,Integer>>();
		
		BufferedReader reader = filePath.toLowerCase().endsWith("gz") ? 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( filePath)))) : 
				new BufferedReader(new FileReader(new File(filePath
				)));
				
		String nextLine = reader.readLine();
		
		int numDone =0;
		while(nextLine != null)
		{
			StringTokenizer sToken = new StringTokenizer(nextLine, "\t");
			String sample = sToken.nextToken();
			String taxa= sToken.nextToken();
			int count = Integer.parseInt(sToken.nextToken());
			
			if( sToken.hasMoreTokens())
				throw new Exception("No");
			
			HashMap<String, Integer> innerMap = map.get(sample);
			if( innerMap == null)
			{
				innerMap = new HashMap<String, Integer>();
				map.put(sample, innerMap);
			}
			
			if( innerMap.containsKey(taxa))
				throw new Exception("parsing error " + taxa);
			
			innerMap.put(taxa, count);
			nextLine = reader.readLine();
			
			numDone++;
			
			if( numDone % 1000000 == 0 )
				System.out.println(numDone);
		}
		
		return map;
	}
}
