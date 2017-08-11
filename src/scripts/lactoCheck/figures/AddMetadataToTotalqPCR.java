package scripts.lactoCheck.figures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import scripts.lactoCheck.rdp.AddMetadata;
import utils.ConfigReader;

public class AddMetadataToTotalqPCR
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
					"NormalizedToBGlobin.txt")));
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getLactoCheckDir() 
				+ File.separator + 
				"NormalizedToBGlobinPlusMetadata.txt")));
		
		writer.write(reader.readLine() + "\tbirthGroup\tdeliveryMode\tcrispMinusIners\n");
		

		HashMap<String, Integer> birthMap = AddMetadata.getBirthGroupMap();
		HashMap<Integer, String> deliveryMap = AddMetadata.getBirthType();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String key = splits[0];
			int aVal = Integer.parseInt(splits[0].substring(1));
			
			writer.write(s + "\t" + birthMap.get(key) + "\t" + deliveryMap.get(aVal) + "\t");
			double diff = Double.parseDouble(splits[1]) - Double.parseDouble(splits[2]);
			writer.write( diff + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
