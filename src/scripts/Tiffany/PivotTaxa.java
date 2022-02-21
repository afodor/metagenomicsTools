package scripts.Tiffany;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PivotTaxa
{
	private static final File IN_FILE = new File( "C:\\tiffany\\normalized_otu_fil.txt");
	private static final File OUT_FILE = new File("C:\\tiffany\\pivot_otu_fil.txt");
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<Double>> map =getMap();
		writeFile( map);
		
	}
	
	private static void writeFile( HashMap<String, List<Double>> map ) throws Exception
	{
		BufferedReader topReader = new BufferedReader(new FileReader(IN_FILE));
		topReader.readLine();
		
		String[] topSplits = topReader.readLine().split("\t");
		
		topReader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(OUT_FILE));
		
		writer.write("sample");
		
		List<String> taxa = new ArrayList<>(map.keySet());
		
		for(String s : taxa)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for( int x=1; x < topSplits.length-2; x++)
		{
			writer.write(topSplits[x]);
			
			for(String s : taxa)
			{
				writer.write("\t" + map.get(s).get(x-1));
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	// key is taxa
	@SuppressWarnings("resource")
	private static HashMap<String, List<Double>> getMap() throws Exception
	{
		HashMap<String, List<Double>> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(IN_FILE));
		
		reader.readLine();
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String taxa = splits[0];
			
			List<Double> innerList = map.get(taxa);
			
			if( innerList == null)
			{
				innerList = new ArrayList<>();
				
				for( int x=1 ; x < splits.length-2; x++)
					innerList.add(0.0);
				
				map.put(taxa, innerList);
			}
			
			for( int x=1; x < splits.length-2; x++)
				innerList.set(x-1, innerList.get(x-1) + Double.parseDouble(splits[x]));
			
		}
		
		
		return map;
	}
}
