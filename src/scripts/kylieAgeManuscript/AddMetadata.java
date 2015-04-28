package scripts.kylieAgeManuscript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> metaMap = MetadataParserFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getKylieDropoxDir() + File.separator +
			"classifications" + File.separator +  "tables" + File.separator + "rdpClassifier" +
						File.separator + "family_rdpClassifier.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getKylieDropoxDir() + File.separator +
				"classifications" + File.separator +  "tables" + File.separator + "rdpClassifier" +
							File.separator + "family_rdpClassifierPlusMetadata.txt"
				)));
		
		String[] topHeaders = reader.readLine().split("\t");
		
		writer.write( topHeaders[0] + "\t" + "sampleID" + "\t" + "animalID\t" + 
		"dateAsFactor\t" + "diet\t" + "cage\t" + "ageCategory");
		
		for( int x=1; x < topHeaders.length; x++)
			writer.write("\t" + topHeaders[x]);
		
		writer.write("\n");
	
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			MetadataParserFileLine meta = metaMap.get(splits[0]);
			
			if( meta == null)
				throw new Exception("No");
			
			writer.write(splits[0] + "\t" + meta.getSampleID() + "\t" + meta.getAnimalID() + "\t" +
			meta.getDateAsFactor() + "\t" + meta.getDiet() + "\t" + meta.getCage() + "\t" + meta.getAgeCategory());
			
			for( int x=1; x < topHeaders.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		
		writer.flush();  writer.close();
		reader.close();
	}
}
