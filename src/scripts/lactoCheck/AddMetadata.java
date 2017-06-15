package scripts.lactoCheck;

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
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getLactoCheckDir() + File.separator + "gaQiimeClosedRefColumnsAsTaxa.txt");
		
		File logNormFile = new File(ConfigReader.getLactoCheckDir() + File.separator + "gaQiimeClosedRefColumnsAsTaxaLogNorm.txt");
		
		File metaFile =new File(ConfigReader.getLactoCheckDir() + File.separator + "gaQiimeClosedRefColumnsAsTaxaLogNormPlusMeta.txt");
		
		addMetadata(logNormFile, metaFile, wrapper);
	}
	
	private static void addMetadata(File inFile, File outFile, OtuWrapper originalWrapper) throws Exception
	{
		HashMap<String, PCR_DataParser> pcrMap = PCR_DataParser.getPCRData();
		
		for(String s : pcrMap.keySet())
			System.out.println(s);
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] topSplits = reader.readLine().split("\t");
		
		writer.write(topSplits[0] + "\tgroupNumber\tL_crispatus\tL_iners\tbglobulin\tsequencingDepth\tshannonDiveristy");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String[] codes = splits[0].split("\\.");
			
			if(codes.length != 3)
				throw new Exception("No " + splits[0]);

			if( codes[2].startsWith("G"))
			{
				PCR_DataParser pcr = pcrMap.get(codes[2]);
				
				if( pcr == null || ! pcr.getGroup().equals(codes[2]))
					throw new Exception("No");
				
				writer.write(splits[0] + "\t" + codes[2] + "\t" + pcr.getL_crispatus() + "\t" + pcr.getL_iners() + 
						"\t" + pcr.getBglobulin() + "\t" + originalWrapper.getNumberSequences(splits[0]) + "\t" +
								originalWrapper.getShannonEntropy(splits[0]));
				
				for( int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
			}
			else
			{
				System.out.println("Could not find" + splits[0]);
			}
			
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
