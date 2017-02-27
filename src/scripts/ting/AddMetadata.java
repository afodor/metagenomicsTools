package scripts.ting;

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
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getTingDir() + 
				File.separator + "otuAsColumns.txt");
		
		File pcoaFile = new File(ConfigReader.getTingDir() + File.separator + 
				"pcoa.txt");
		
		File pcoaMetadataFile = new File(ConfigReader.getTingDir() + File.separator + 
				"pcoaPlustMetadata.txt");
		
		addMetadata(wrapper, pcoaFile, pcoaMetadataFile, true);
		
		File inFile = new File(ConfigReader.getTingDir() + File.separator + 
				"otuAsColumnsLogNorm.txt");
		
		File logNormMetaFile =  new File(ConfigReader.getTingDir() + File.separator + 
				"otuAsColumnsLogNormPlusMetadata.txt");
		
		addMetadata(wrapper, inFile, logNormMetaFile, false);
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
			boolean fromR) throws Exception
	{
		HashMap<Integer, MetadataParser> metaMap = MetadataParser.getMetaMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
	
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
	
		writer.write("mouseId\tgenotype\tcage\ttime\tinitialBodyWeight\tlowestBodyWeight\tbodyWeightAtDay8");
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
