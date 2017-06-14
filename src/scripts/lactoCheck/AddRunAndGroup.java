package scripts.lactoCheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddRunAndGroup
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> countMap = Normalize.getTotalCounts(); 
		
		for(String s : countMap.keySet())
			System.out.println(s);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator 
				+ "Lacto_Iners.tsv")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLactoCheckDir() + File.separator + "Lacto_InersPlusRunID.tsv"
				)));
		
		String[] splits= reader.readLine().split("\t");
		
		writer.write(splits[0] + "\trun\tsequenceDepth");
		
		for( int x=1; x < splits.length; x++)
			writer.write("\t" + splits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null;  s= reader.readLine())
		{
			splits = s.split("\t");
			
			writer.write(splits[0]);
			
			writer.write("\t" + splits[1].substring(0, 4) + "\t" + 
								countMap.get(splits[1]));
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
