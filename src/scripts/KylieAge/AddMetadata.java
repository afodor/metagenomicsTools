package scripts.KylieAge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMetaMap();
		
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKylieAgeDir()+
					File.separator + "rdp" + File.separator + "pcoa_" + level  + ".txt")));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKylieAgeDir()+
					File.separator + "rdp" + File.separator + "pcoa_" + level  + "PlusMetadata.txt")));
			
			writer.write("sampleID\tmonkey\toldYoung\tlocation\toldShare\tcohab_neighbor\t" + reader.readLine() + "\n");
			
			for( String s = reader.readLine() ; s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				String key = new StringTokenizer(splits[0].replaceAll("\"", ""), "_").nextToken();
				
				MetadataParser mp = metaMap.get(key);
				
				if( mp == null)
					throw new Exception("Could not find " + key);
				
				writer.write( splits[0].replaceAll("\"", "") + "\t" +  mp.getMonkey() + "\t" + mp.getOldYoung() + "\t" + mp.getLocation() +"\t" + mp.getOldShare() + "\t" + mp.getCohabitationNeighbor());
				
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
