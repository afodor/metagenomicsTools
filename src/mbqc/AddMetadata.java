package mbqc;

import java.io.File;
import java.util.HashMap;

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
		for(String s : wrapper.getOtuNames())
		{
			MetadataTSVParser tsv = map.get(s.replace(prefix, ""));
			if( tsv == null)
				throw new Exception("Could not find " + s.replace(prefix, ""));
		}
		
	}
}
