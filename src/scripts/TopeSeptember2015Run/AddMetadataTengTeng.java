package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadataTengTeng
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeSep2015Dir() + File.separator + 
					"tengteng_December_2019" + File.separator + 
						"Genus level data.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getTopeSep2015Dir() + File.separator + 
				"tengteng_December_2019" + File.separator + 
				"GenusLogNormCaseControl.txt")));
		
		
		String[] topSplits =reader.readLine().split("\t");
		
		writer.write(topSplits[0] + "\t" + "caseControl");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s=reader.readLine())
		{
			String[] splits =s.split("\t");
			
			MetadataParser mdp = metaMap.get(splits[0]);
			
			writer.write(splits[0]);
			
			writer.write( "\t" + ( mdp == null ? "-1" : mdp.getCaseControl()));
			
			for(int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
}
