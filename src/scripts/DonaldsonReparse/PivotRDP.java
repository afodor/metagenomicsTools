package scripts.DonaldsonReparse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import parsers.PivotOTUs;

public class PivotRDP
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];

			System.out.println(level);
			
			HashMap<String, HashMap<String, Integer>> map = 
			getMap(level);
			
			File outFile = new File("C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\rdpOut"+ File.separator + level + "asColumns.txt");
			
			PivotOTUs.writeResults(map, outFile.getAbsolutePath());
			
			OtuWrapper wrapper = new OtuWrapper(outFile);
			
			wrapper.writeNormalizedLoggedDataToFile("C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\rdpOut"+ File.separator 
						+ level + "asColumnsLogNorm.txt");
		}
	}
	
	public static HashMap<String, HashMap<String, Integer>> getMap( String level) throws Exception
	{
		HashMap<String, HashMap<String, Integer>> map = 
			new HashMap<String, HashMap<String,Integer>>();
		
		List<NewRDPParserFileLine> list = NewRDPParserFileLine.getRdpListSingleThread(
				"C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\rdpOut.txt");
		
		
		for(NewRDPParserFileLine rdpLine : list)
		{
			StringTokenizer sToken = new StringTokenizer(rdpLine.getSequenceId(), "_");
			sToken.nextToken();
			String key = sToken.nextToken();
			
			HashMap<String, Integer> innerMap = map.get(key);
			
			if( innerMap == null)
			{
				innerMap = new HashMap<String, Integer>();
				map.put(key, innerMap);
			}
			
			NewRDPNode node = rdpLine.getTaxaMap().get(level);
			
			if( node != null && node.getScore() >= 50)
			{
				Integer count = innerMap.get(node.getTaxaName());
				
				if( count == null)
					count =0;
				
				count++;
				
				innerMap.put(node.getTaxaName(), count);
			}
		}
		
		return map;
	}
}
