package scripts.ageReparse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;

public class AgeReparse
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\anhDraft\\gloorJustCounts.txt");
		
		File logNormFile = new File("C:\\anhDraft\\gloorJustCountsLogNorm.txt" );
		
		wrapper.writeNormalizedLoggedDataToFile(logNormFile);
	
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\anhDraft\\GloorForR.txt")));
		
		BufferedReader logReader = new BufferedReader(new FileReader(logNormFile));
		
		
		HashMap<String, Double> map= getAgeMap();
		
		writer.write("sample\tage");
		
		String[] topSplits = logReader.readLine().split("\t");
		
		for(int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
			
		for(String s = logReader.readLine(); s != null; s= logReader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0] + "\t" + map.get(splits[0]));
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, Double> getAgeMap() throws Exception
	{
		HashMap<String, Double> map = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\anhDraft\\gloor.txt")));
		
		reader.readLine();
		
		for(String s=reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( map.containsKey(splits[1]))
				throw new Exception("Noo");
			
			map.put(splits[1],Double.parseDouble(splits[2]));
		}
		
		return map;
	}
	
}
