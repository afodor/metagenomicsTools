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
				"June24_risk" + File.separator +"raw_100.txt" );
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"June24_risk" + File.separator +"raw_100_WithMeta.txt" )));
		
		
		writer.write("sample\tshannonDiversity\ttotalSeqs");
		
		for(String s : wrapper.getSampleNames())
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(int x=0; x < wrapper.getOtuNames().size(); x++)
		{
			writer.write(wrapper.getOtuNames().get(x) + "\t");
			writer.write(wrapper.getShannonEntropy(x) + "\t");
			writer.write(wrapper.getCountsForSample(x) + "");
			
			for( int y=0; y < wrapper.getSampleNames().size(); y++)
			{
				writer.write("\t" + wrapper.getDataPointsUnnormalized().get(y).get(x));
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
	}
}
