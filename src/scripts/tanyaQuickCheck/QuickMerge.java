package scripts.tanyaQuickCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.TabReader;

public class QuickMerge
{
	public static String[] LEVELS = {"phylum", "class", "order", "family", "species"};
	
	public static void main(String[] args) throws Exception
	{
	
		getMetaboliteLines( new File( "C:\\tanyaQuickRep\\pos_pre.txt"));
		
		/*
		for(String level : LEVELS)
		{
			System.out.println(level);
			OtuWrapper wrapper = new OtuWrapper("C:\\tanyaQuickRep\\tanya_kraken_"+ level +  ".txt");
			
			wrapper.writeLoggedDataWithTaxaAsColumns( new File( "C:\\tanyaQuickRep\\tanya_kraken_"+ level +  "logNorm.txt"));
		}
		*/
	}
	
	
	/*
	 * Key is going to be the microbiome id; 
	 * the values is going to be the entire line of the metabolite file
	 */
	private static HashMap<String, String> getMetaboliteLines(File file) throws Exception
	{
		HashMap<String, String> idMap = getMetaMap();
		
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		int numFound =0;
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			String microbeID = idMap.get(key);
			
			if( microbeID == null) 
			{
				System.out.println("Could not find " +key );
			}
			else
			{
				System.out.println("Found " + microbeID);
				numFound++;
			}
				
		}
		
		System.out.println("Found " + numFound);
		reader.close();
		
		return map;
		
	}
	
	private static HashMap<String, String> getMetaMap() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\tanyaQuickRep\\alderete_k99_metadata_30dec18.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tr = new TabReader(s);
			
			String value= tr.getNext();
			tr.getNext();
			
			String key = tr.getNext();
			
			String[] parts  = value.split("\\.");
			
			value = parts[0];
			
			map.put(key, value);
		}
		
		reader.close();
		return map;
	}
}
