package scripts.ChWorkshop2019;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class TestMeta
{

	public static void main(String[] args) throws Exception
	{
		File firstFile = new File("C:\\chWorkshop\\190709-DORSEY\\03_countsTables_txtFormat\\190709-DORSEY-txt\\190709_UNC21_0641_000000000-CH7GF.mapping.txt");
		
		HashMap<String, String> map = getMetaMap(firstFile);
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		File ivoryLog = new File("C:\\chWorkshop\\190709-DORSEY\\04_statistics\\190709-DORSEY_log10NormalizedCounts_withMeta.txt");
		
		checkLines(map, ivoryLog);
	}
	
	private static void checkLines(HashMap<String, String> map , File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s =reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String expected = map.get(splits[0]);
			
			if( expected == null)
				throw new Exception("No");
			
			String[] expectedTokens = expected.split("\t");
			
			for( int x=0; x < expectedTokens.length; x++)
			{
				if( !expectedTokens[x].equals(splits[x]))
					throw new Exception("No");
				
				//System.out.println(expectedTokens[x] + " " + splits[x]);
			}
		}
	}
	
	private static HashMap<String, String> getMetaMap(File inFile) throws Exception
	{
		HashMap<String, String>  map = new HashMap<>();
		
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, s);
		}
		
		return map;
	}
}
