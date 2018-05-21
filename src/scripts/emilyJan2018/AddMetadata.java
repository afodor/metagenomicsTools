package scripts.emilyJan2018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
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
		
		String top1= getFirstLine(meta1);
		String top2 = getFirstLine(meta2);
		String top3 = getFirstLine(meta3);
		
		if( ! top1.equals(top2))
			throw new Exception("No");
		
		if( ! top1.equals(top3))
			throw new Exception("No");
		
		if( ! top2.equals(top3))
			throw new Exception("Logic error");
		
		HashMap<String, String> map = new HashMap<>();
		addToMap(meta1, map, "AN703");
		addToMap(meta2, map, "AN40");
		addToMap(meta3, map,"AN34");
		
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
			
			writeAMeta(inFile, outFile, unnormalized, top1, map);
		}
	}
	
	public static void addToMap(File inFile, HashMap<String, String> map, String suffix) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			
			String key = splits[0] + "_" + suffix;
			
			if( map.containsKey(key))
				throw new Exception("Duplciate key " +  key);
			
			map.put(key, s);
		}
		
		reader.close();
	}
	
	public static void writeAMeta(File inFile, File outFile, File unnormalizedFile, String firstLine,
			HashMap<String, String> map ) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(unnormalizedFile);
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer  = new BufferedWriter(new FileWriter(outFile));
		
		String topLine = reader.readLine();
		String[] topSplits = topLine.split("\t");
		
		writer.write(topSplits[0] + "\tkey_donorID\tdonorID\treadNum\tuniqueDonor\tshannonDiveristy\tnumReads\t" + firstLine  );
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine();  s != null; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write( splits[0] );
			
			String[] keySplits = splits[0].split("_");
			String keyPlusDonor = keySplits[0] + "_" + keySplits[1];
			
			String metaLine = map.get(keyPlusDonor);
			
			if( metaLine == null)
				throw new Exception("No");
			
			String[] metaSplits = metaLine.split("\t");
			
			if( metaSplits.length > 6)
			{
				writer.write("\t" + keyPlusDonor);
				writer.write("\t" + keySplits[1]);
				writer.write("\t" + keySplits[2].charAt(0));
				
				String mouseGroup = metaSplits[12];
				System.out.println( "TIME CHECK " +  mouseGroup);
				
				if( mouseGroup.equals("T1") || mouseGroup.equals("T2"))
					writer.write("\t" + keySplits[1] + "_case");
				else if( mouseGroup.equals("HC"))
					writer.write("\t" + keySplits[1] + "_control");
				else 
					writer.write("\tn.a.");
				
				writer.write("\t" + wrapper.getShannonEntropy(splits[0]));
				writer.write("\t" + wrapper.getNumberSequences(splits[0]));
				
				writer.write("\t" + metaLine);
				
				for( int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");

			}
			else
			{
				System.out.println("Warning: Problems with meta file ...." + keyPlusDonor);
			}
		}
		
		reader.close();
		writer.flush();  writer.close();
	}
	
	public static String getFirstLine(File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String firstLine = reader.readLine();
		
		reader.close();
		
		return firstLine;
	}
}
