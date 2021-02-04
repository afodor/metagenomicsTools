package scripts.farnazBarSurgery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AddMeta
{
	public static final String[] LEVELS = {  "phylum", "class", "order" , "family", "genus", "species" };
	
	
	public static void main(String[] args) throws Exception
	{
		for(String s : LEVELS)
		{
			addALevel(s);
		}
	}
	
	private static void addALevel(String level)  throws Exception
	{
		HashMap<String, MetaParser1> metaMap1 = MetaParser1.getMetaMap1();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\BariatricSurgery_Analyses2021-main\\input\\bariatricSurgery_Sep152020_2_2020Sep15_taxaCount_norm_Log10_" + level + ".tsv")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String sampleId = new StringTokenizer(splits[0], "_").nextToken();
			
			if( ! metaMap1.containsKey(sampleId))
				throw new Exception("Could not find " + sampleId );
		}
		
		reader.close();
	}
}
