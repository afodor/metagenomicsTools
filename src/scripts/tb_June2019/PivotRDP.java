package scripts.tb_June2019;

import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class PivotRDP
{
	private static final int THRESHOLD = 80;

	public static void main(String[] args) throws Exception
	{
		File rdpInDir = new File(ConfigReader.getTb_June_2019_Dir() + File.separator + 
					"02_RdpClassifier" + File.separator + "output");
		
		for(int x=1 ; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
		//	HashMap<String, HashMap<String,Long>> map = getCountsForDirectory(rdpInDir, level);
		}
		
	}
	
	private static HashMap<String, HashMap<String,Long>> getCountsForDirectory(File inDir, String level) throws Exception
	{
		HashMap<String, HashMap<String,Long>>  map = new HashMap<>();
		
		for(String fileName : inDir.list())
		{
			File file = new File( inDir.getAbsoluteFile() + File.separator + fileName);
			
			HashMap<String, Long> innerMap = getCountAtLevel(file, level);
			
			String aName = file.getName();
			
			aName = new StringTokenizer(aName, "_").nextToken();
			
			System.out.println("Got " + aName + " "+  innerMap.size() + " "+  level);
			
			
			map.put(aName, innerMap);
		}
		
		return map;
	}
	
	private static HashMap<String, Long> getCountAtLevel(File file, String level) throws Exception
	{
		HashMap<String, Long>  map = new HashMap<>();
		
		HashMap<String, NewRDPParserFileLine> rdpMap = 
		NewRDPParserFileLine.getAsMapFromSingleThread(file);
		
		for(NewRDPParserFileLine rdpLine : rdpMap.values())
		{
			NewRDPNode node = rdpLine.getTaxaMap().get(level);
			
			if( node != null &&  node.getScore() >= THRESHOLD )
			{
				if( node.getTaxaName().toLowerCase().indexOf("unclassified") == -1)  
				{
					Long count =map.get(node.getTaxaName());
					
					if( count == null)
						count =0l;
					
					count = count +1;
					
					map.put(node.getTaxaName(), count);
				}
				
			}
		}
		
		return map;
		
	}
}
