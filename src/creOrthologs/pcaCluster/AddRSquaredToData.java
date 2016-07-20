package creOrthologs.pcaCluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddRSquaredToData
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> map = getRSquaredMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
				"chs_11_plus_cards.txt")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
					"pcaCluster" + File.separator + "chs_11_plus_cardsPlusRsquared.txt")));
		
		writer.write(reader.readLine() + "\trSquared\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0].replaceAll("\"", "");
			Double val = map.get(key);
			
			writer.write(s);
			
			if( val != null)
				writer.write("\t" + val + "\n");
			else
				writer.write("\t\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static HashMap<String, Double> getRSquaredMap() throws Exception
	{
		HashMap<String, Double> map =new HashMap<String,Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
					"pcaCluster" + File.separator + "linearModelFromFirst5Axes.txt")));
		
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0].replaceAll("\"", "").replace("Line_", "");
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, Double.parseDouble(splits[2]));
		}
		
		reader.close();
		
		return map;
	}
}
