package scripts.luthurJan2019;

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
		for(String level : SequenceToTaxaParser.TAXA_LEVELS )
		{
			System.out.println(level);
			File originalFile = new File( ConfigReader.getLuthurJan2019Dir() + File.separator + 
					"data" + File.separator + level + "asColumns.txt");
			
			File inFile = new File( ConfigReader.getLuthurJan2019Dir() + File.separator + 
					"data" + File.separator + level + "_logNorm.txt");
			
			File outFile =new File( ConfigReader.getLuthurJan2019Dir() + File.separator + 
					"data" + File.separator + level + "_logNormPlusMeta.txt");
			 
			writeAMeta(originalFile, inFile, outFile);
		}
	}
	
	private static void writeAMeta(File originalFile, File inFile, File outFile) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(originalFile);
		HashMap<String, MetadataFileParser> metaMap = MetadataFileParser.getMap();
		
		BufferedReader reader= new BufferedReader(new FileReader(inFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

		String topLine = reader.readLine();
		String[] topSplits = topLine.split("\t");
		
		writer.write("sample");
		
		writer.write("sampleID\tmouseID\tsex\tcage\tdiet\tchallenge\tage\tdayOnDiet\tdayAfterFMT\tdaysPostChallenge\tshannonDiveristy");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != topSplits.length)
				throw new Exception("Parsing error");
			
			MetadataFileParser mfp = metaMap.get(splits[0]);
			
			if( mfp == null)
				throw new Exception("Could not find " + splits[0]);
			
			writer.write(splits[0]);
			
			writer.write("\t" + mfp.getMouseID() + "\t" + mfp.getSex() + "\t" + mfp.getCage() + "\t" + mfp.getDiet() + "\t"+ 
			mfp.getChallenge() + "\t" + mfp.getAge() + "\t"+ mfp.getDayOnDiet() + "\t" + mfp.getDayAfterFMT()  + "\t" + 
						mfp.getDaysPostChallenge() + "\t" + wrapper.getShannonEntropy(splits[0]) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
