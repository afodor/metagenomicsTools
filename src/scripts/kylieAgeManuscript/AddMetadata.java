package scripts.kylieAgeManuscript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class AddMetadata
{
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> metaMap = MetadataParserFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			"D:\\Kylie_Manuscript_May12_2015\\tables\\pcoa_hFROnly.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"D:\\Kylie_Manuscript_May12_2015\\tables\\pcoa_hFROnlyPlusMetadata.txt")));
					
		String[] topHeaders = reader.readLine().split("\t");
		
		writer.write( "sampleID" + "\t" + "animalID\t" + 
		"dateAsFactor\t" + "diet\t" + "cage\t" + "ageCategory\t" + "days");
		
		for( int x=1; x < topHeaders.length; x++)
			writer.write("\t" + topHeaders[x]);
		
		writer.write("\n");
	
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			MetadataParserFileLine meta = metaMap.get(splits[0].replaceAll("\"", ""));
			
			if( meta == null)
				throw new Exception("No " + splits[0].replaceAll("\"", ""));
			
			writer.write(meta.getSampleID() + "\t" + meta.getAnimalID() + "\t" +
			"Date_" + meta.getDateAsFactor() + "\t" + meta.getDiet() + "\t" + meta.getCage() + "\t" +
					meta.getAgeCategory() + "\t" + meta.getDays());
			
			for( int x=1; x < topHeaders.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		
		writer.flush();  writer.close();
		reader.close();
	}
}
