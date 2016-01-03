package scripts.IanNovember2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class CountSignificantAcrossAllColumns
{
	public static void main(String[] args) throws Exception
	{
		File topDir = new File(ConfigReader.getIanNov2015Dir() + File.separator + 
					"spreadsheets" );
		
		if( ! topDir.exists())
			throw new Exception("No " + topDir.getAbsolutePath());
		
		String[] list = topDir.list();
		
		long numSearched =0;
		long numMatched =0;
		
		for(String s : list)
		{
			if( s .indexOf("_Corr_") != -1)
			{
				BufferedReader reader = new BufferedReader(new FileReader(new File(
					topDir + File.separator + s	)));
				
				reader.readLine();
				
				for(String s2 = reader.readLine(); s2 != null; s2 = reader.readLine() )
				{
					numSearched++;
					String[] splits = s2.split("\t");
					
					if( Double.parseDouble(splits[5]) < 0.05)
					{
						System.out.println(s2);
						numMatched++;
					}
				}
				
				reader.close();
			}
		}
		
		System.out.println("Searched " + numSearched + " and found " + numMatched);
	}
}
