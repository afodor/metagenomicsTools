package scripts.mattNov2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;


public class CompareAcrossTissues
{
	public static String[] FIRST_WORDS = 
		{"Liver", "Duodenum", "Jejunum", "Ileum", "Cecum", "Proximal Colon", "Distal Colon", "Cecal Contents", "Fecal",
				"Corticosterone"};
	
	private static class Holder
	{
		String fullmetabloiteName;
		Double spearman;
		
		@Override
		public String toString()
		{
			return fullmetabloiteName + " " + spearman;
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String,Holder>> map = getMap();
		
		for(String s : map.keySet())
			System.out.println(s + " "+ map.get(s));
		
		writeResults(map);
	}
	
	private static void writeResults(HashMap<String, HashMap<String,Holder>> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\MattNov14\\pivoted_Fecal.txt"));
		
		writer.write("taxa\tmetabolite");
		
		for(String s : FIRST_WORDS)
			writer.write("\t" + s);

		writer.write("spearman\n");
		
		for(String key : map.keySet() )
		{
			StringTokenizer sToken = new StringTokenizer(key, "@");
			
			if( sToken.countTokens() !=2 )
				throw new Exception("no");
			
			
			writer.write(sToken.nextToken() + "\t" + sToken.nextToken());
			
			HashMap<String,Holder> innerMap = map.get(key);
			
			for(String s : FIRST_WORDS)
			{
				Holder h = innerMap.get(s);
				
				if( h != null)
					writer.write("\t" + h.spearman);
				else
					writer.write("\t");
				
			}

			writer.write("\n");
		}
		
		writer.flush(); writer.close();
	}
	
	
	// key is the taxa name + "@" + metabolite name with location removed; inner map key is the first word
	private static HashMap<String, HashMap<String,Holder>> getMap() throws Exception
	{
		HashMap<String, HashMap<String,Holder>>  map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader("C:\\MattNov14\\firstPValuesGenus_Fecal.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String taxaName = splits[1];
			
			String location= null;
			
			String fullName = splits[2].trim().replaceAll("\"", "");
			
			for(String s2 : FIRST_WORDS)
				if( fullName.startsWith(s2))
					location=s2;
			
			if(location==null)
				throw new Exception("Could not find a start for " + fullName);
			
			String innerKey = taxaName + "@" + fullName.replace(location, "").trim();
			
			HashMap<String, Holder> innerMap = map.get(innerKey);
			
			if( innerMap == null)
			{
				innerMap = new HashMap<>();
				map.put(innerKey, innerMap);
			}
				
			Holder h = new Holder();
			h.fullmetabloiteName = fullName;
			h.spearman = Double.parseDouble(splits[3]);
			innerMap.put(location, h);
		}
		
		reader.close();
		return map;
	}
}
