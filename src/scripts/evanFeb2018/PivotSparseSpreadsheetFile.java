package scripts.evanFeb2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import parsers.PivotOTUs;
import utils.ConfigReader;

public class PivotSparseSpreadsheetFile
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			HashMap<String, HashMap<String, Integer>> map = 
			getMap(ConfigReader.getEvanFeb2018Dir()
					+ File.separator + "spreadsheets" + File.separator + NewRDPParserFileLine.TAXA_ARRAY[x] + 
					"_SparseThreeCol.txt");
			
			File pivotedFile = 
					new File(
							ConfigReader.getEvanFeb2018Dir()
							+ File.separator + "spreadsheets" + 
							File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			PivotOTUs.writeResults(map, pivotedFile.getAbsolutePath());
			OtuWrapper wrapper = new OtuWrapper(pivotedFile);
			wrapper.writeNormalizedLoggedDataToFile(ConfigReader.getEvanFeb2018Dir()
					+ File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
			
			wrapper.writeNormalizedDataToFile(new File(ConfigReader.getEvanFeb2018Dir()
					+ File.separator + "spreadsheets" + 
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsNorm.txt"));
			
			
		}
	}
	
	/*
	 * Pivot file in the format 
	 * 
	 * 
       sample1 Eubacteriaceae  646
       sample2 Ruminococcaceae 286023
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
			String sample = sToken.nextToken().replace(".fastatoRDP.txt.gz", "");
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
