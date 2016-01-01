package scripts.IanNovember2015.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class TestRDPCounts
{
	public static void main(String[] args) throws Exception
	{	
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			OtuWrapper wrapper = new OtuWrapper(
					ConfigReader.getIanNov2015Dir() + File.separator + 
					"spreadsheets" + File.separator + 
					"pivoted_"+ level + "asColumns.txt" );
			
			for(int y=0; y< wrapper.getSampleNames().size(); y++)
			{
				String sampleName = wrapper.getSampleNames().get(y);
				System.out.println(sampleName);
			}
					
		
		}
	}
	
	private static HashMap<String, Integer> getCountsForTaxaAtLevel(
			String rdpFile, String level) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				rdpFile)));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			String next = null;
			while( sToken.hasMoreTokens())
			{
				String last = next.replaceAll("\"", "");
				
				if( sToken.hasMoreTokens())
				{
					next = sToken.nextToken().replaceAll("\"", "");
					
					if( next.equals(level) && Double.parseDouble(sToken.nextToken()) >= 0.499999 )
					{
						Integer count = map.get(last);
						
						if( count == null)
							count =0;
						
						count++;
						
						map.put(last, count);
					}
				}
				
			}
		}
		
		reader.close();
		
		return map;
	}
}
