package scripts.tingReProcessed;

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
		for( int x=2; x<=6; x++)
			addMetadataForLevel(x);
		
	}
	
	public static void addMetadataForLevel(int i) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getTingDir() + File.separator +  "may_2017_rerun" 
				+ File.separator + "otuAsColumns_rerun_" + i + ".txt");
		
		File pcoaFile = new File(ConfigReader.getTingDir() + File.separator +  "may_2017_rerun" 
				+ File.separator +
				"pcoa_rerun_L" + i + ".txt");
		
		File pcoaMetadataFile = new File(ConfigReader.getTingDir() + File.separator +  "may_2017_rerun" 
				+ File.separator +
				"pcoa_rerunPlusMetadata_L" + i + ".txt");
		
		addMetadata(wrapper, pcoaFile, pcoaMetadataFile, true);
		
		File inFile = new File(ConfigReader.getTingDir() + File.separator + 
				 "may_2017_rerun" + File.separator + 
				"otuAsColumnsLogNorm_rerun_" + i + ".txt");
		
		File logNormMetaFile =  new File(ConfigReader.getTingDir() + File.separator + 
				 "may_2017_rerun" + File.separator + 
				"otuAsColumnsLogNorm_rerunPlusMetadata_L" + i + ".txt");
		
		addMetadata(wrapper, inFile, logNormMetaFile, false);
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
			boolean fromR) throws Exception
	{
		HashMap<Integer, MetadataParser> metaMap = MetadataParser.getMetaMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
	
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
	
		writer.write("mouseId\tgenotype\tcage\ttime\tinitialBodyWeight\tlowestBodyWeight\tbodyWeightAtDay8\tbodyWeightDay8Fracion\tmaxWeightLossPercent");
		writer.write("\tnumberSequencesPerSample\tshannonEntropy");
	
		String[] firstSplits = reader.readLine().split("\t");
	
		int startPos = fromR ? 0 : 1;
	
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
	
		writer.write("\n");
	
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
		
			Integer key = Integer.parseInt(splits[0].replaceAll("\"", ""));
			
			MetadataParser mp = metaMap.get(key);
			
			writer.write(key+ "\t" + mp.getGenotype() + "\t" + mp.getCage() + "\t" + 
							mp.getTime() + "\t" + mp.getInitialBodyWeight() + "\t" + 
								mp.getLowestBodyWeight() + "\t" + mp.getBodyWeightAtDay8() + "\t"+ 
									(mp.getBodyWeightAtDay8() / mp.getInitialBodyWeight()) + "\t" + 
							mp.getMaxWeightLossPercent() + "\t" + 
				wrapper.getNumberSequences(key.toString()) 
					+ "\t" + wrapper.getShannonEntropy(key.toString())  );
		
		for( int x=1; x < splits.length; x++)
			writer.write("\t" + splits[x]);
		
		writer.write("\n");
	}
	
	writer.flush();  writer.close();
	reader.close();
}
}
