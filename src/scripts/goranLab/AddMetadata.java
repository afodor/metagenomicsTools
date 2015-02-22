package scripts.goranLab;

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
		HashMap<String, MetadataFileLine> metaMap = MetadataFileLine.getMetaMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getGoranTrialDir() + File.separator + 
			"family_pcoa_.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getGoranTrialDir() + File.separator + 
					"family_pcoa_withMetadata.txt")));
		
		writer.write("sample\tsanVsSol\tplq\trNumber\t" +   reader.readLine() + "\n");
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{ 
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			writer.write( key + "\t");
			
			MetadataFileLine mfl = metaMap.get(key);
			
			writer.write(mfl.getSanVsSol() + "\t" + mfl.getPlq3Orplq4() + "\t" + mfl.getrNumber() );
			writer.write( reader.readLine() + "\n");
		}
		
		writer.flush();  writer.close();
	}
}
