package scripts.compareEngel.WuRelease;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import scripts.compareEngel.MetadataFileParser;
import utils.ConfigReader;

public class MergeMetaKraken
{
	public static final String[] levels = {"phylum", "class", "order", "family", "genus", "species"};
	
	
	public static void main(String[] args) throws Exception
	{
		for(String s : levels)
			mergeAtLevel(s);
	}
	
	private static void mergeAtLevel( String level ) throws Exception
	{
		System.out.println(level);
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getEngelCheckDir() +File.separator + "krakenCheck" + File.separator + 
				"mikeRelease" + File.separator + "kraken_" + level + ".txt");
	
		HashMap<String, MetadataFileParser> map =  MetadataFileParser.getMetaMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getEngelCheckDir() +File.separator + "krakenCheck" + File.separator + 
				"mikeRelease" + File.separator + "kraken_" + level + "MetaLogNorm.txt"
				)));
		
		writer.write("sampleID\tpatientID\teupct\tyripct\tc_mrace\tc_case2\tshannnonDiversity\tsequenceDepth");
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
			writer.write("\t" + wrapper.getOtuNames().get(x));
		
		writer.write("\n");
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String aName = wrapper.getSampleNames().get(x);
			
			writer.write(aName + "\t");
			String sampleID = new StringTokenizer(aName, "-").nextToken();
			writer.write(sampleID + "\t");
			
			MetadataFileParser mfp = map.get(sampleID);
			
			if( mfp == null)
			{
				writer.write("NA\tNA\tNA\t");
				System.out.println("Could not find " + sampleID);
			}
			else
			{
				writer.write( getFloatOrEmpty(mfp.getEupct())
						+ "\t" + getFloatOrEmpty(mfp.getYripct()) + "\t" 
							+ mfp.getC_mrace() 
				+"\t" + mfp.getC_case2());
			}
			
			writer.write("\t" + wrapper.getShannonEntropy(x) + "\t" + wrapper.getNumberSequences(aName));
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
				writer.write("\t" + wrapper.getDataPointsNormalizedThenLogged().get(x).get(y));
			
			writer.write("\n");
			
		}
		
		writer.flush(); writer.close();
	}
	
	private static String getFloatOrEmpty(Float f)
	{
		if( f== null)
			return "NA";
		
		return f.toString();
	}
}
