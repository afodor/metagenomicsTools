package bigDataScalingFactors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddTotalCountsAndDiversity
{
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"July_StoolRemoved" + File.separator +"risk_raw_countsTaxaAsColumnsStoolOnly.txt" );
		
		//OtuWrapper rawWrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
			//	"June24_risk" + File.separator +"raw_100_taxaAsColumns.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"July_StoolRemoved" + File.separator +"July_StoolRemoved_taxaSummaryNormalized.txt" )));
		
		writer.write("sample\tshannonDiversity\ttotalSeqs");
		
		for(String s : wrapper.getOtuNames())
			if( wrapper.getCountsForTaxa(s) >= 5000)
				writer.write("\t" + s);
		
		writer.write("\n");
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			System.out.println(x +  "  " + wrapper.getSampleNames().size());
			writer.write(wrapper.getSampleNames().get(x) + "\t");
			writer.write(wrapper.getShannonEntropy(x) + "\t");
			writer.write(wrapper.getCountsForSample(wrapper.getSampleNames().get(x)) + "");
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
				if( wrapper.getCountsForTaxa(wrapper.getOtuNames().get(y)) >= 5000 )
				{
					writer.write("\t" + wrapper.getDataPointsNormalized().get(x).get(y));
				}
				
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
	}
}