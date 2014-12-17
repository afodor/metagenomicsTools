package mbqc;

import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		String prefix = "jpetrosino";
		OtuWrapper wrapper = new OtuWrapper(
				ConfigReader.getMbqcDir() + 
				File.separator + "dropbox" + 
					File.separator +  "merged_otu_filtered_"+ prefix + "TaxaAsColumns.txt");
		
		HashMap<String, MetadataTSVParser> map = MetadataTSVParser.getMap();
		
		int numFound =0;
		int numNotFound =0;
		for(String s : wrapper.getSampleNames())
		{
			String key = s.replace(prefix + ".", "");
			
			if( ! map.containsKey(key))
			{
				
			}
			MetadataTSVParser tsv = map.get(key);
			
			if( tsv == null)
			{
				numNotFound++;
				System.out.println("Could not find " + key);
				
			}
			else
			{
				numFound++;
			}
		}
		
		System.out.println(numFound +  " " + numNotFound);
		
	}
}
