package scripts.lyteBehaviorMarch2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + 
					"rg_results" + File.separator + "LyteSharon_r01_pcoa.txt");
		
		OtuWrapper wrapper =new OtuWrapper(ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + 
				"rg_results" + File.separator + 
				"LyteSharon_r01_crDataOnlyTaxaAsColumns.txt");
		
		File outFile = new File(ConfigReader.getLyteBehaviorMarch2017Dir() + File.separator + 
					"rg_results" + File.separator + "LyteSharon_r01_pcoaPlusMetadata.txt");
		
		addMetadata(inFile, outFile, wrapper, true);
	}
	
	private static void addMetadata(File inFile, File outFile, OtuWrapper wrapper, boolean fromR )
		throws Exception
	{
		HashMap<String, String> metaLines = MetadataParser.getLinesAsMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String[] topSplits = reader.readLine().replace("\"", "").split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		writer.write("sample\tnumSequences\tshannonDiveristy");
		
		String[] topSplitsMeta = MetadataParser.getTopLine().split("\t");
		
		for( int x=1; x < topSplitsMeta.length; x++)
			writer.write("\t" + topSplitsMeta[x]);
		
		for( int x=startPos; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			String[] splits =s.split("\t");
			
			String key = splits[0];
			writer.write(key);
			writer.write("\t" + wrapper.getNumberSequences(splits[0]) + "\t" + 
								wrapper.getShannonEntropy(splits[0]) + "\t");
			
			System.out.println(key);
			
			String metaLine = metaLines.get(key);
			
			if( metaLine == null)
				throw new Exception("No");
			
			String[] metaSplits = metaLine.split("\t");
			
			for( int x=1; x < metaSplits.length; x++)
				writer.write("\t" + metaSplits[x]);
			
			for(int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		reader.close();
		writer.flush();  writer.close();

	}
}
