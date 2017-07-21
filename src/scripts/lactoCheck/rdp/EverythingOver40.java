package scripts.lactoCheck.rdp;

import java.io.File;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class EverythingOver40
{
	public static void main(String[] args) throws Exception
	{
			for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];

			System.out.println("\n\n" + level);
			
			OtuWrapper wrapper = 
					new OtuWrapper(
							ConfigReader.getLactoCheckDir()+ File.separator + "rdp" 
									+ File.separator +
									 level + "asColumns.txt");
			
			for( int y=0; y < wrapper.getSampleNames().size(); y++)
				if( wrapper.getSampleNames().get(y).indexOf("G") != -1 || wrapper.getSampleNames().get(y).indexOf("neg") != -1)
				if( wrapper.getSampleNames().get(y).indexOf("Run1") != -1 && wrapper.getSampleNames().get(y).indexOf("R1") != -1 )
				for( int z= 0; z < wrapper.getOtuNames().size(); z++)
				{
					double val = wrapper.getDataPointsUnnormalized().get(y).get(z) /
									wrapper.getCountsForSample(y);
					
					if( val >=.4)
					{
						System.out.println(wrapper.getSampleNames().get(y) + " " + 
											wrapper.getOtuNames().get(z) + " "  + val);		
					}
				}
		}
	}
			
	
}
