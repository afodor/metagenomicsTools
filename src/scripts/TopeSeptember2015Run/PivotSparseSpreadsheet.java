package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.NewRDPParserFileLine;
import parsers.PivotOTUs;
import utils.ConfigReader;

public class PivotSparseSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{

			HashMap<String, HashMap<String, Integer>> map = 
			getMap(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			PivotOTUs.writeResults(map, ConfigReader.getTopeSep2015Dir() + File.separator +
					"spreadsheets" + File.separator 
								+NewRDPParserFileLine.TAXA_ARRAY[x] + "_asColumns.txt");	
		}
		
	}
	
	/*
	 * Pivot file in the format 
	 * /home/Anthony/bigdisk/rdpResults/US-1481989     Bacteroidaceae  6325680
	   /home/Anthony/bigdisk/rdpResults/US-1481989     Lachnospiraceae 1117978
       /home/Anthony/bigdisk/rdpResults/US-1481989     Eubacteriaceae  646
       /home/Anthony/bigdisk/rdpResults/US-1481989     Ruminococcaceae 286023
 */
	public static HashMap<String, HashMap<String, Integer>> getMap(String level) throws Exception
	{
		File spreadsheetsDir = new File(ConfigReader.getTopeSep2015Dir() + File.separator + 
				"spreadsheets");
		
		HashMap<String, HashMap<String, Integer>> map = 
				new HashMap<String, HashMap<String,Integer>>();
		
		int numDone =0 ;
		
		for(String s : spreadsheetsDir.list())
			if( s.indexOf(level) != -1)
			{
				
				String filePath = spreadsheetsDir.getAbsolutePath()+ File.separator + 
							s;
				
				if(numDone % 1000 == 0 )
					System.out.println(filePath + " " + numDone);
				
				numDone++;

				BufferedReader reader = filePath.toLowerCase().endsWith("gz") ? 
						new BufferedReader(new InputStreamReader( 
								new GZIPInputStream( new FileInputStream( filePath)))) : 
						new BufferedReader(new FileReader(new File(filePath
						)));
						
				String nextLine = reader.readLine();
				
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
					
					Integer oldCount = innerMap.get(taxa);
					
					if( oldCount != null)
						count = count + oldCount;
					
					innerMap.put(taxa, count);
					nextLine = reader.readLine();
				}
				
				reader.close();
			}
		
		return map;
		
	}
}
