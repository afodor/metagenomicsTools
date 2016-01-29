package scripts.TopeDiverticulosisJan2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

import utils.ConfigReader;
import utils.Translate;

public class MergeTwo
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getMergedMap();
		System.out.println(map.size());
	}
	
	public static HashMap<String, String> getMergedMap() throws Exception
	{
		 HashMap<String, String> map1 = Demultiplex.getBarcodeToSampleMap();
		 HashMap<String, String> map2 = getAllMap();
		 
		 HashSet<String> all = new HashSet<String>();
		 all.addAll(map1.keySet());
		 all.addAll(map2.keySet());
		 HashMap<String, String> merged = new HashMap<String,String>();
		 
		 int numInCommon =0;
		 
		 for(String s : map1.keySet())
			 if( map2.containsKey(s))
			 {
				 numInCommon++;
				 //System.out.println( s + " " + map1.get(s) + " " + map2.get(s) );
				 
				 String id = map1.get(s).replaceAll("_" , "-");
				 
				 if( ! id.equals(map2.get(s)))
				 {
					System.out.println(s + " " +  id + " " + map2.get(s)); 
					all.remove(s);
				 }
			 }
				 
		 for(String s1 : map1.keySet())
			 if( all.contains(s1))
		 {
			 if( merged.containsKey(s1))
				 throw new Exception("Logic error");
			 
			 merged.put(s1, map1.get(s1).replaceAll("_" , "-"));
		 }
		 
		 for(String s2 : map2.keySet())
			 if( all.contains(s2))
		 {
			if( map1.containsKey(s2) && ! map1.get(s2).replaceAll("_" , "-").equals(map2.get(s2)))
				throw new Exception("Logic error " + s2 + " " + map1.get(s2) + " "+ map2.get(s2) );
			
			merged.put(s2, map2.get(s2));
		 }
		 
		 System.out.println(map1.size() + " " + map2.size() + " " + numInCommon);
					 
		return merged;
	}
	
	private static HashMap<String, String> getAllMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getTopeDiverticulosisDec2015Dir() + File.separator + 
			"All Diversticulosis Illumina Primer Seq.txt"
			)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 12)
				throw new Exception("No");
			
			String id = splits[0];
			
			if(map.containsValue(id))
				throw new Exception("No");
			
			String primerKey = splits[8] + "@" + Translate.reverseTranscribe(splits[3]);
			
			if( map.containsKey(primerKey))
				throw new Exception("No");
			
			map.put(primerKey,id);
		}
		
		reader.close();
		
		return map;
	}
}
