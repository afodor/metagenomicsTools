package scripts.ratSach2;

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
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getRachSachReanalysisDir() + File.separator + 
			"pcoa_otu_Cecal Content_taxaAsColsLogNorm.txt")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + 
				"pcoa_otu_Cecal Content_taxaAsColsLogNormWithMetadata.txt")));
		
		writer.write("sample\tcondition\ttissue\t");
		writer.write(reader.readLine() + "\n");
		
		HashMap<String, MappingFileLine> map = MappingFileLine.getMap();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			writer.write(splits[0].replaceAll("\"", "") + "\t");
			
			MappingFileLine mfl = map.get(splits[0].replaceAll("\"", ""));
			writer.write(mfl.getCondition() + "\t");
			writer.write(mfl.getTissue());
			
			for(int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
	}
}
