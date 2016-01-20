package creOrthologs.kmers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class AddColumnPositionToPCOA
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + "sym1_pcoa.txt")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + "sym1_pcoaWithStartPos.txt")));
			
		writer.write("position\t" + reader.readLine().replaceAll("\"", "") + "\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine() )
		{
			s = s.replaceAll("\"", "");
			String[] splits = s.split("\t");
			
			String[] firstSplits = splits[0].split("_");
			
			if( firstSplits.length != 3)
				throw new Exception("No");
			
			writer.write(firstSplits[1] + "");
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
