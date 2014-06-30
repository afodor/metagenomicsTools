package bigDataScalingFactors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddTotalCountsAndDiversity
{
	private static HashSet<String> getIncluded()
	{
		HashSet<String> set = new HashSet<String>();

		
		String[] a = {"469709","199293","190511","198251","181330","326977","331820","130663","158660","470239"
				};
		
		for(String s :a )
			set.add(s);
		
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> included = getIncluded();
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"June24_risk" + File.separator +"vsd_100_taxaAsColumns.txt" );
		
		OtuWrapper rawWrapper = new OtuWrapper(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"June24_risk" + File.separator +"raw_100_taxaAsColumns.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getBigDataScalingFactorsDir() + File.separator + 
				"June24_risk" + File.separator +"vsd_100_taxaAsColumnsWithMeta.txt" )));
		
		writer.write("sample\tshannonDiversity\ttotalSeqs");
		
		for(String s : wrapper.getOtuNames())
			if( included.contains(s))
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			writer.write(wrapper.getSampleNames().get(x) + "\t");
			writer.write(wrapper.getShannonEntropy(x) + "\t");
			writer.write(rawWrapper.getCountsForSample(wrapper.getSampleNames().get(x)) + "");
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
				if( included.contains( wrapper.getOtuNames().get(y)))
				{
					writer.write("\t" + wrapper.getDataPointsUnnormalized().get(x).get(y));
				}
				
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
	}
}
