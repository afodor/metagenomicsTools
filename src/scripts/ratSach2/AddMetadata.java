package scripts.ratSach2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;

import utils.ConfigReader;

public class AddMetadata
{
	public static HashMap<String, String> getCageMap() throws Exception
	{
		HashMap<String, String> cageMap = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getRachSachReanalysisDir() + File.separator + "TTULyteCages.txt"	)));
		
		reader.readLine(); 
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( cageMap.containsKey(splits[1]))
				throw new Exception("No");
			
			cageMap.put(splits[1],splits[0]);
		}
		
		return cageMap;
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean onlyOnePerCage = false;
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getRachSachReanalysisDir() + File.separator + 
			"pcoa_otu_Colon Content_taxaAsColsLogNorm.txt")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator +
				(onlyOnePerCage ?"pcoa_otu_Colon Content_taxaAsColsLogNormWithMetadataOnePerCage.txt" :
				"pcoa_otu_Colon Content_taxaAsColsLogNormWithMetadataAllMice.txt"
					))));
		
		writer.write("sample\tcondition\ttissue\tcage\t");
		writer.write(reader.readLine() + "\n");
		HashMap<String, String> cageMap = getCageMap();
		
		HashMap<String, MappingFileLine> map = MappingFileLine.getMap();
		HashSet<String> cages = new HashSet<String>();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			MappingFileLine mfl = map.get(splits[0].replaceAll("\"", ""));
			String cage = cageMap.get(mfl.getRatID());
			
			if( ! onlyOnePerCage ||  ! cages.contains(cage) )
			{
				writer.write(splits[0].replaceAll("\"", "") + "\t");
				writer.write(mfl.getLine() + "\t");
				writer.write(mfl.getTissue() + "\t");
				writer.write(cage);
				
				for(int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
				cages.add(cage);
			}
		}
		
		writer.flush();  writer.close();
		
	}
}
