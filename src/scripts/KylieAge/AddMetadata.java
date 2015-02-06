package scripts.KylieAge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static HashMap<String, Long> getSequenceCounts() throws Exception
	{
		HashMap<String, Long> map = new HashMap<String, Long>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKylieAgeDir() + File.separator + "counts.txt")));
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0].replace(".fastq.gz", "");
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Long.parseLong(splits[1]));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMetaMap();
		HashMap<String, Long> countMap = getSequenceCounts();
		System.out.println(countMap.keySet());
		
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			OtuWrapper wrapper = new OtuWrapper( 
					ConfigReader.getKylieAgeDir()+
					File.separator + "rdp" + File.separator + 
					"all" + level +  "asColumns.txt");
			System.out.println(level);
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKylieAgeDir()+
					File.separator + "rdp" + File.separator + "pcoa_" + level  + ".txt")));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKylieAgeDir()+
					File.separator + "rdp" + File.separator + "pcoa_" + level  + "PlusMetadata.txt")));
			
			writer.write("sampleID\tread1_2\tnumRawSequences\tfractionCalledSequences\tnumCalledSequences\tshannonEvenness\tshannonDiversity\tunrarifiedRichness\t" + 
			"monkey\toldYoung\tlocation\toldShare\tcohab_neighbor\t" + reader.readLine() + "\n");
			
			for( String s = reader.readLine() ; s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				String key = new StringTokenizer(splits[0].replaceAll("\"", ""), "_").nextToken();
				
				MetadataParser mp = metaMap.get(key);
				Long rawCounts = countMap.get(splits[0].replaceAll("\"", ""));
				
				if( mp == null)
					throw new Exception("Could not find " + key);
				
				String sampleID = splits[0].replaceAll("\"", "");
				System.out.println(splits[0]);
				writer.write( sampleID+ "\t");
				writer.write( sampleID.split("_")[3] + "\t" );
				writer.write( rawCounts + "\t");
				writer.write( wrapper.getCountsForSample(sampleID)/ (((double)rawCounts)) + "\t");
				writer.write( wrapper.getCountsForSample(sampleID) + "\t" );
				writer.write( wrapper.getEvenness(sampleID) + "\t" );
				writer.write( wrapper.getShannonEntropy(sampleID) + "\t" );
				writer.write( wrapper.getRichness(sampleID) + "\t");
				
				writer.write(mp.getMonkey() + "\t" + mp.getOldYoung() + "\t" + mp.getLocation() +"\t" + mp.getOldShare() + "\t" + mp.getCohabitationNeighbor());
				
				for( int y=1; y < splits.length; y++)
					writer.write("\t" + splits[y].replaceAll("\"", "") );
				
				writer.write("\n");
			}

			writer.flush();
			writer.close();
			reader.close();
		}
	}
}
