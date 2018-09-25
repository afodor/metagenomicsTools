package scripts.topeVicki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMeta
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileParser> metaMap = MetadataFileParser.getMetaMap();
		String[] levels = { "p", "c","o","f","g" };
		
		for(String level : levels)
		{
			File loggedFile = new File(ConfigReader.getTopeVickiDir() + File.separator + level + "_mergedLogged.txt");
			
			File metaFile = new File(ConfigReader.getTopeVickiDir() + File.separator + 
					level + "_mergedLoggedMeta.txt");
			
			BufferedReader reader = new BufferedReader(new FileReader(loggedFile));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(metaFile));
			
			writer.write("id\trace\tbmi\ttumorHistology\ttumorGrade");
			
			String[] topSplits = reader.readLine().split("\t");
			
			for( int x=1; x < topSplits.length; x++)
				writer.write("\t" + topSplits[x]);
			
			writer.write("\n");
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				writer.write(splits[0]);
				
				MetadataFileParser mfp = metaMap.get(splits[0].substring(0,splits[0].length()-2));
				
				if( mfp == null)
				{
					writer.write("\tNA\tNA\tNA\tNA");
				}
				else
				{
					writer.write("\t" + mfp.getRace() + "\t" + mfp.getBmi() + "\t" + 
										mfp.getTumorHistology() + "\t" + mfp.getTumorGrade());
				}
				
				for( int x=1; x < splits.length; x++)
					writer.write("\t"  + splits[x]);
				
				writer.write("\n");
			}
			
			writer.flush();  writer.close();
			reader.close();
		}
		
	}
}
