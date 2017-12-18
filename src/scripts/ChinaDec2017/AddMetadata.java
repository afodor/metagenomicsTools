package scripts.ChinaDec2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	
	public static void main(String[] args) throws Exception
	{

		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++ )
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDecember2017Dir() + File.separator + 
				"pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + ".txt")));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getChinaDecember2017Dir() + File.separator + 
					"pcoa_" + NewRDPParserFileLine.TAXA_ARRAY[x] + "WithFirstChar.txt")));
			
			writer.write("id\tfirstChar\t" + reader.readLine().replaceAll("\"", "") + "\n");
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				s = s.replaceAll("\"", "");
				String[] splits = s.split("\t");
				
				writer.write(splits[0] + "\t" + splits[0].charAt(0));
				
				for( int y=1; y < splits.length; y++)
				writer.write("\t" + splits[y]);
				
				writer.write("\n");
			}
			
			writer.flush();  writer.close();
			
			reader.close();
		}
	}
}
