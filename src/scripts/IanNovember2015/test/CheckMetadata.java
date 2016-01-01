package scripts.IanNovember2015.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class CheckMetadata
{
	private static final Integer TOP = -1;
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, String> metaLine = getMetaFileLines();
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];

			HashMap<String, String> loggedFileLines = getLoggedFileLines(level);
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getIanNov2015Dir() + File.separator + 
					"spreadsheets" + File.separator + 
					"pivoted_"+ level + "asColumnsLogNormalPlusMetadata.txt" )));
			
			boolean first = true;
			
			for(String s = reader.readLine(); s != null && s.trim().length() > 0; s = reader.readLine())
			{
				//System.out.println(s);
				String[] splits = s.split("\t");
				
				Integer key = TOP;
				
				if( ! first)
				{
					StringTokenizer sToken = new StringTokenizer(splits[0], "_");
					sToken.nextToken();
					key = Integer.parseInt(sToken.nextToken());
				}
				
				if( key != 46 && key != 7)
				{
					String lines = metaLine.get(key);
					
					String[] metaSplits = lines.split("\t");
					
					for( int y=1; y < metaSplits.length; y++)
					{
						if( first)
							System.out.println(metaSplits[y] + " " +splits[y+3] );
						
						if( ! metaSplits[y].equals(splits[y+3]))
							throw new Exception("No");
					}
				}
				
				String keyString = "" + TOP;
				
				if( ! first)
					keyString = splits[0];
				
				String[] loggedSplits = loggedFileLines.get(keyString).split("\t");
				
				for( int y=1; y < loggedSplits.length; y++)
				{
					if( first)
						System.out.println( loggedSplits[y] + " " + splits[y+20]);
					
					if( ! loggedSplits[y].equals(splits[y+20]))
						throw new Exception("No");
				}
				
				first = false;
			}
		}
		System.out.println("Global pass");
	}
	
	private static HashMap<String, String> getLoggedFileLines(String level) throws Exception
	{
		HashMap<String,String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getIanNov2015Dir() + File.separator + 
				"spreadsheets" + File.separator + 
				"pivoted_"+ level + "asColumnsLogNormal.txt" )));
	
		map.put("" + TOP, reader.readLine());
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			if( map.containsKey(splits[0].replace(".fastatoRDP.txt", "")))
				throw new Exception("No");
			
			map.put(splits[0].replace(".fastatoRDP.txt", ""), s);
		}
		
		reader.close();
		return map;
		
	}
	
	private static HashMap<Integer, String> getMetaFileLines() throws Exception
	{
		HashMap<Integer, String> map = new HashMap<Integer,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
			new File(ConfigReader.getIanNov2015Dir() + File.separator + 
						"HC Psych Data for Anthony_10.20.15_BlankColsDeleted.txt")));
		
		map.put(TOP, reader.readLine());
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			Integer key = Integer.parseInt(splits[0].replace("HC", ""));
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, s);
		}
		
		return map;
	}
}
