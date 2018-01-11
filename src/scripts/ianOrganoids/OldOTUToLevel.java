package scripts.ianOrganoids;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class OldOTUToLevel
{
	public static void main(String[] args) throws Exception
	{
		String[] levels = {"p","c","o","f","g"};
		
		for(String s : levels)
			countALevel(s);
	}
	
	private static void countALevel(String level) throws Exception
	{
		HashMap<String, List<Double>> map = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getIanOrganoidDirectory() + File.separator + 
					"otu_table_FINAL.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for(String s= reader.readLine(); s != null && s.length() > 0 ; s= reader.readLine())
		{
			String[] splits=  s.split("\t");
			String lastColumn = splits[splits.length-1];
			String taxa = getTaxa(lastColumn, level);
			
			if( taxa != null)
			{
				List<Double> counts = map.get(taxa);
				
				if( counts == null)
				{
					counts = new ArrayList<Double>();
					
					for( int x=1; x < topSplits.length-1; x++)
						counts.add(0.0);
					
					map.put(taxa, counts);
				}
				
				for( int x=1; x < topSplits.length-1;x++)
					counts.set((x-1), counts.get(x-1) + 
								Double.parseDouble(splits[x].replaceAll("\"", "")));

			}
		}
		
		File outFile = new File(ConfigReader.getIanOrganoidDirectory() + File.separator + 
				level +"OldSheet.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("sample");
		List<String> taxa = new ArrayList<>(map.keySet());
		Collections.sort(taxa);
		
		for( String s: taxa)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(int x=1; x< topSplits.length -1 ; x++)
		{
			writer.write(topSplits[x]);
			
			for( int y=0; y < taxa.size(); y++ )
			{
				List<Double> counts = map.get(taxa.get(y));
				writer.write("\t" + counts.get(x-1));
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static String getTaxa(String lastColumn, String level) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(lastColumn, ";");
		
		while( sToken.hasMoreTokens())
		{
			String nextToken = sToken.nextToken().trim();
			if( nextToken.startsWith(level))
			{
				String returnString = nextToken.substring(3).trim();
				if( returnString.length() == 0 )
					returnString = "unclassified";
				
				return returnString;
			}
		}
		
		System.out.println("returning null for " + lastColumn);
		return null;
	}
}
