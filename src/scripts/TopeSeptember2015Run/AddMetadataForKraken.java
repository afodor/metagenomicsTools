package scripts.TopeSeptember2015Run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadataForKraken
{

	public static String[] TAXA_ARRAY
		= { "domain", "phylum", "class", "order", "family", "genus", "species"};
	
	public static void main(String[] args) throws Exception
	{
		for( int x=0; x < TAXA_ARRAY.length; x++)
		{
			System.out.println(TAXA_ARRAY[x]) ;
			File inFile = new File(ConfigReader.getTopeSep2015Dir() + File.separator +
					"kraken" + File.separator +
					"kraken_" + TAXA_ARRAY[x] + ".txt");
			OtuWrapper wrapper = new OtuWrapper(inFile);
			
			File metadataFile = new File(ConfigReader.getTopeSep2015Dir() 
					+ File.separator +
					"kraken" + File.separator +
					"kraken_" + TAXA_ARRAY[x] + "PlusMetadata.txt");
			
			addMetadata(wrapper, inFile, metadataFile, false);
		}
	}
	
	private static void addMetadata( OtuWrapper originalWrapper, File inFile, File outFile, boolean fromR)
		throws Exception
	{
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("key\tcaseControl");
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x= (fromR ? 0 : 1); x < splits.length; x++ ) 
			writer.write("\t" + splits[x].replaceAll("\"", ""));
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			StringTokenizer pToken = new StringTokenizer(splits[0].replaceAll("\"", ""),"_");
			
			String key = splits[0].replaceAll("\"", "");
			String sample = pToken.nextToken().replaceAll("\"", "");

			MetadataParser mdp = metaMap.get(sample);
			
			if( mdp != null && mdp.getCaseControl() != -1 )
			{
				int readNumber = Integer.parseInt(
						key.substring(key.lastIndexOf("_")+1, key.length()).replace(".fasta", ""));
				
				if( readNumber == 1)	
				{
					writer.write(key + "\t" + mdp.getCaseControl());
					
					for( int x=1; x < splits.length; x++)
						writer.write("\t" + splits[x]);
					
					writer.write("\n");
				}
					
			}
			
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
