package scripts.mattNov2019;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ComparePValues
{
	private static class Holder
	{
		Double spearmanFromCecum;
		Double spearmanFromFecal;
	}
	
	public static void main(String[] args) throws Exception
	{
		// key is taxa +"@" +  metabolite
		HashMap<String, Holder> map = new HashMap<>();
		
		File fecalFile = new File("C:\\MattNov14\\firstPValuesGenus_Fecal.txt");
		
		addToMap(map, fecalFile, true);
		
		File cecalFile = new File("C:\\MattNov14\\firstPValuesGenus_Cecal.txt");

		addToMap(map,cecalFile, false);
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(
				new File("C:\\MattNov14\\PValuesCecalVsFecal_genus.txt")));
		
		writer.write("taxa\tmetabolite\tmetaboliteFirstName\tmetaboliteLastName\tcecumSpearman\tfecalSpeaman\n");
		
		for(String s : map.keySet())
		{
			StringTokenizer sToken =new StringTokenizer(s, "@");
			
			if( sToken.countTokens() != 2)
				throw new Exception();
			
			String taxa = sToken.nextToken();
			String fullMetabolite = sToken.nextToken();
			String firstName = getFirstName(fullMetabolite).trim();
			String lastName  = fullMetabolite.replace("\"", "").replace(firstName, "").trim();
			
			
			writer.write(taxa+ "\t" + fullMetabolite+ "\t"+ firstName + "\t" + lastName + "\t" + 
				getValOrEmpty(map.get(s).spearmanFromCecum) + "\t" +
					getValOrEmpty( map.get(s).spearmanFromFecal) + "\n");
			
			
		}
		
		writer.flush(); writer.close();
	}
	
	private static String getValOrEmpty(Double d)
	{
		if ( d== null )
			return "";
		
		return d.toString();
	}
	
	public static String getFirstName(String s) throws Exception
	{
		s = s.replace("\"", "").trim();
		
		for(String s2 : CompareAcrossTissues.FIRST_WORDS)
			if( s.startsWith(s2))
				return s2;
		
		throw new Exception("No " + s);
	}
	
	private static void addToMap(HashMap<String, Holder> map ,File f, boolean isFecal)
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[1] + "@" + splits[2];
			
			Holder h = map.get(key);
			
			if( h==null)
			{
				h = new Holder();
				map.put(key, h);
			}
				
			if( isFecal)
			{
				if( h.spearmanFromFecal != null )
					throw new Exception("Duplicate");
				
				h.spearmanFromFecal= Double.parseDouble(splits[3]);
			}
			else
			{
				String aVal = splits[3];
				
				if( ! aVal.equals("NA"))
				{
					if( h.spearmanFromCecum!= null )
						throw new Exception("Duplicate");
					
					h.spearmanFromCecum= Double.parseDouble(splits[3]);
				}				
			}
		}
		
		reader.close();
	}
	
}
