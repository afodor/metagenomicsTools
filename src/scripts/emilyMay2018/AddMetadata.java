package scripts.emilyMay2018;

import java.io.File;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		File meta1 =new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"2018-01-10_AN703_16S metadata.txt");
		
		File meta2 = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
				"2018-01-10_AN40_16S metadata.txt");
		
		File meta3 = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"2018-01-10_AN34_16S metadata.txt");
		
		File meta4 = new File(ConfigReader.getEmilyMay2018Dir() + File.separator + 
				"2018-04-30_AN81_16S_metadata.txt");	
		
		String top1= scripts.emilyJan2018.AddMetadata.getFirstLine(meta1);
		String top2 = scripts.emilyJan2018.AddMetadata.getFirstLine(meta2);
		String top3 = scripts.emilyJan2018.AddMetadata.getFirstLine(meta3);
		String top4 = scripts.emilyJan2018.AddMetadata.getFirstLine(meta4);
		
		if( ! top1.equals(top2))
			throw new Exception("No");
		
		if( ! top1.equals(top3))
			throw new Exception("No");
		
		if( ! top1.equals(top4))
			throw new Exception("No");
		
		if( ! top2.equals(top3))
			throw new Exception("Logic error");
		
		if( ! top2.equals(top4))
			throw new Exception("Logic error");
		
		HashMap<String, String> map = new HashMap<>();
		scripts.emilyJan2018.AddMetadata.addToMap(meta1, map, "AN703");
		scripts.emilyJan2018.AddMetadata.addToMap(meta2, map, "AN40");
		scripts.emilyJan2018.AddMetadata.addToMap(meta3, map,"AN34");
		scripts.emilyJan2018.AddMetadata.addToMap(meta4, map, "AN81");
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(taxa);
			
			File inFile = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"spreadsheets" + File.separator  + "pivoted_" + taxa + "asColumnsLogNormal.txt");
			
			File outFile = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"spreadsheets" + File.separator  + "pivoted_" + taxa + "asColumnsLogNormalPlusMeta.txt");
			
			File unnormalized = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"spreadsheets" + File.separator  + "pivoted_" + taxa + "asColumns.txt");
			
			scripts.emilyJan2018.AddMetadata.writeAMeta(inFile, outFile, unnormalized, top1, map);
		}
	}	
}
