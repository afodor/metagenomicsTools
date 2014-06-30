package bigDataScalingFactors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteTotalCountsForTaxa
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"June24_risk" + File.separator +"raw_100_taxaAsColumns.txt" );
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"June24_risk" + File.separator +"countsVsTaxa.txt" )));
		
		writer.write("taxa\tcounts\n");
		
		for(String s : wrapper.getOtuNames())
			writer.write(s + "\t" + wrapper.getCountsForTaxa(s) + "\n");
		
		writer.flush(); writer.close();
	}
}
