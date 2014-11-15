package ruralVsUrban;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteLogNormalizedForR
{
	private static String getUrbanRural(String sample) throws Exception
	{
		sample = sample.replaceAll("A", "");
		int sampleID = Integer.parseInt(sample);
		
		if( sampleID >= 1 && sampleID <=39)
			return "rural";
		
		if( sampleID >= 81 && sampleID <= 120)
			return "urban";
					
		throw new Exception("No");
	}
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getChinaDir() + File.separator + 
									"phylaVsSamples.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getChinaDir() + 
				File.separator + "phylaVsSamplesPlusMedata.txt"));
		
		writer.write("sample\tcommunity");
		
		for(String s : wrapper.getOtuNames() )
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( String s : wrapper.getSampleNames())
		{
			writer.write(s + "\t");
			writer.write(getUrbanRural(s));
			int sampleID = wrapper.getIndexForSampleName(s);
			
			for( int x=0; x < wrapper.getOtuNames().size(); x++)
				writer.write("\t" + wrapper.getDataPointsNormalizedThenLogged().get(sampleID).get(x));
			
			writer.write("\n");
			
		}
		
		writer.flush();  writer.close();
	}
}
