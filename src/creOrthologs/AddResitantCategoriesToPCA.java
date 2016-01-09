package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class AddResitantCategoriesToPCA
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = AddMetadata.getBroadCategoryMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + "transposedPCA.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + "transposedPCAPlusMetadata.txt")));
		
		writer.write("genome\tcategory\t" + reader.readLine() + "\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0].replaceAll("\"", ""));
			
			String category = map.get(splits[0].replaceAll("\"", ""));
			
			if( category == null)
				throw new Exception("No " + splits[0]);
			
			writer.write("\t" + category);
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		
		reader.close();
	}
}
