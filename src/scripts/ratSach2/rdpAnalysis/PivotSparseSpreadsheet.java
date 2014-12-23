package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import parsers.PivotOTUs;
import utils.ConfigReader;

public class PivotSparseSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			HashMap<String, HashMap<String, Integer>> map = 
			getMap(ConfigReader.getRachSachReanalysisDir()+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + NewRDPParserFileLine.TAXA_ARRAY[x] + 
					".txt.gz");
			
			File outFile = new File(ConfigReader.getRachSachReanalysisDir()+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + NewRDPParserFileLine.TAXA_ARRAY[x] + 
					"_AsColumns.txt");
			
			PivotOTUs.writeResults(map, outFile.getAbsolutePath());
			
			HashSet<String> excludedSamples = new HashSet<String>();
			
			// remove the controls --- 
			excludedSamples.add("sample0153"); excludedSamples.add("sample0154");
			OtuWrapper wrapper = new OtuWrapper(outFile, excludedSamples, new HashSet<String>());
			
			
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "sparseThreeColumn_" + NewRDPParserFileLine.TAXA_ARRAY[x] + 
					"_AsColumnsLogNormalized.txt");
		}
		
	}
	
	/*
	 * Pivot file in the format 
	 * /home/Anthony/bigdisk/rdpResults/US-1481989     Bacteroidaceae  6325680
	   /home/Anthony/bigdisk/rdpResults/US-1481989     Lachnospiraceae 1117978
       /home/Anthony/bigdisk/rdpResults/US-1481989     Eubacteriaceae  646
       /home/Anthony/bigdisk/rdpResults/US-1481989     Ruminococcaceae 286023
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
