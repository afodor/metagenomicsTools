package scripts.topeVicki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
			
			BufferedReader reader = new BufferedReader(new FileReader(loggedFile));
			
			reader.readLine();
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				
				MetadataFileParser mfp = metaMap.get(splits[0]);
				
				if( mfp == null)
					System.out.println("No " + splits[0]);
				else
					System.out.println("Got " + splits[0]);
			}
		}
		
	}
}
