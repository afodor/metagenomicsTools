package scripts.lactoCheck.figures.errorBarsToQPCR;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import scripts.lactoCheck.rdp.AddMetadata;
import utils.ConfigReader;

public class MetaToTotalForCrispers
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
				"qPCRWithErrorBars" + File.separator + "summarizedFileForCrispers.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
				"qPCRWithErrorBars" + File.separator + "crispersWithMeta.txt")));
		
		writer.write(reader.readLine() + "\tbirthGroup\tdeliveryMode\n");
		
		HashMap<String, Integer> birthMap = AddMetadata.getBirthGroupMap();
		HashMap<Integer, String> deliveryMap = AddMetadata.getBirthType();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String key = splits[0];
			
			if( key.startsWith("G"))
			{
				int aVal = Integer.parseInt(splits[0].substring(1));
				writer.write(s + "\t" + birthMap.get(key) + "\t" + deliveryMap.get(aVal) + "\n");
			}
			else
			{
				writer.write(s + "\t" + key + "\t" + key+ "\n");
			}
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}

