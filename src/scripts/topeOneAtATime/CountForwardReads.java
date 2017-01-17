package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class CountForwardReads
{
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getTopeOneAtATimeDir() + File.separator + 
					"merged" + File.separator + 
						"pivoted_" + taxa + "asColumnsPlusMetadata.txt")));
			
			reader.readLine();
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				
				int readNum = Integer.parseInt(splits[10]);
			}
		}
	}
}
