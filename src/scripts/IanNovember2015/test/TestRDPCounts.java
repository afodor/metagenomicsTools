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
				
				File rdpFile = new File(ConfigReader.getIanNov2015Dir() + File.separator +
							"rdpOut" + File.separator + sampleName);
				HashMap<String, Integer> expected = 
						getCountsForTaxaAtLevel(rdpFile.getAbsolutePath(), level);
				
				for( int z=0; z < wrapper.getOtuNames().size(); z++)
				{
					String otuName = wrapper.getOtuNames().get(z).replaceAll("\"", "");
					
					double wrapperVal = wrapper.getDataPointsUnnormalized().get(y).get(z);
					
					if( wrapperVal == 0 && expected.containsKey(otuName))
						throw new Exception("No");
					
					if( wrapperVal >0 )
					{
						Integer expectedVal = expected.get(otuName);
						
						if( expectedVal == null)
							throw new Exception("No " + otuName);
						
						System.out.println(expectedVal + " " + wrapperVal);
						
						if( Math.abs(expectedVal - wrapperVal) > 0.000001)
							throw new Exception("No");
					}
				}
				
				System.out.println("Pass " + rdpFile.getAbsolutePath() + " " + level);
			}
		}
		
		System.out.println("Gloabal pass");
	}
	
	private static HashMap<String, Integer> getCountsForTaxaAtLevel(
			String rdpFile, String level) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				rdpFile)));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			boolean gotIt = false;
			String next = null;
			while( ! gotIt && sToken.hasMoreTokens())
			{
				String last = next;
				
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
						gotIt = true;
					}
				}
				
			}
		}
		
		reader.close();
		
		return map;
	}
}
