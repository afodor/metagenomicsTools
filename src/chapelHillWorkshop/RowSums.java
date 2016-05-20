package chapelHillWorkshop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class RowSums
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(
				ConfigReader.getChapelHillWorkshopDir() + File.separator + 
				"humann2_genefamilies.LABELS2.tsv")));
			
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getChapelHillWorkshopDir() + File.separator + 
					"rowSums.txt")));
			
		writer.write("header\tsum\n");
		
		reader.readLine();
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
				
			if( splits.length != 21)
				throw new Exception("unexpected tokens");
			
			double sum = 0;
			
			for( int x=1; x < splits.length; x++)
				sum += Double.parseDouble(splits[x]);
			
			writer.write(splits[0].replaceAll("\"", "").replaceAll(".", "") + "\t" + sum + "\n");
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
}
