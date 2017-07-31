package scripts.lactoCheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import scripts.lactoCheck.rdp.AddMetadata;
import utils.ConfigReader;

public class ToBiolockJMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer,String> typeMap =  AddMetadata.getBirthType();
		
		HashMap<String, Integer> groupMap = AddMetadata.getBirthGroupMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
			"gastricStoolForBiolockJ.tsv"	)));
		
		writer.write("sampleName\tbirthGroup\tdeliveryMode\n");
		
		for(String s: groupMap.keySet())
		{
			writer.write(s + "\t");
			writer.write( groupMap.get(s) + "\t");
			writer.write(typeMap.get(s) + "\n");
		}
		
		writer.flush(); writer.close();
	}
}
