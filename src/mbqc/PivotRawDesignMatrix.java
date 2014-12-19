package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class PivotRawDesignMatrix
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getMbqcDir() + File.separator + 
				"dropbox" + File.separator + 
				"raw_design_matrix.txt"));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getMbqcDir() + 
				File.separator + "af_out" + File.separator + "rawDesignPivoted.txt"));
		
		writer.write("ID\textraction_wetlab\tsequencing_wetlab\tMBQC.ID\ttaxaName\ttaxaLevel\tspecimenType\n");
		
		List<String> taxa = new ArrayList<String>();
		String[] headerSplits = reader.readLine().split("\t");
		
		int x=4;
		
		while(headerSplits[x].startsWith("k__"))
		{
			taxa.add(getPhyla(headerSplits[x]));
			x++;
		}
		
		x+=2;
		
		for(String s = reader.readLine(); s!= null; s= reader.readLine())
		{
			String[] splits = s.split("\t");

			for(int y=0; y < taxa.size(); y++)
			{
				for( int z=0; z < 4; z++)
					writer.write(splits[z] + "\t");

				writer.write(taxa.get(y) + "\t");
				writer.write(splits[y+4] + "\t");
				writer.write(splits[x] + "\n");	
			}
		} 
		
		reader.close();
		writer.flush();  writer.close();
	}
	
	private static String getPhyla(String s )
	{
		int index = s.indexOf("p__");
		return s.substring(index + 2);
	}
}
