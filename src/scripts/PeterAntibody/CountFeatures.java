package scripts.PeterAntibody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CountFeatures
{
	public static HashSet<Character> getAASet() throws Exception
	{
		HashSet<Character> set = new HashSet<>();
		
		for( String s : WriteFeatureTable.AMINO_ACID_CHARS)
			set.add(s.charAt(0));
		
		for( String s : WriteFeatureTable.AMINO_ACID_CHARS)
			set.add(s.toLowerCase().charAt(0));
		
		if( set.size() != 40 )
			throw new Exception("No");
		
		return set;
	}
	
	private static HashMap<String, String> getFileNameToCategoryMap()
	{
		HashMap<String, String> map = new HashMap<>();
		
		map.put("dekosky_basis_aa_hc_chothia.txt", "dekosky_basis");
		map.put("imgt_human_hc_chothia.txt", "imgt_human");
		map.put("mouse_germline_kappa_chothia.txt", "mouse_germline");
		map.put("dekosky_basis_aa_lc_chothia.txt", "dekosky_basis");
		map.put("imgt_human_lc_chothia.txt","imgt_human" );   
		map.put("therapeutic_hc_chothia.txt", "therapeutic");
		map.put("human_germline_hc_chothia.txt", "human_germline");
		map.put("imgt_mouse_hc_chothia.txt", "imgt_mouse");
		map.put( "therapeutic_lc_chothia.txt", "therapeutic");
		map.put( "human_germline_kappa_chothia.txt", "human_germline"); 
		map.put("imgt_mouse_lc_chothia.txt", "imgt_mouse"); 
		map.put("human_germline_lambda_chothia.txt", "human_germline");
		map.put("mouse_germline_hc_chothia.txt", "mouse_germline");
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<Character> aaSet = getAASet();
		Map< String, Map<String,Map<String,Character>>> map = getMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"seqCounts_5635.txt")));
		
		writer.write("sequenceID\tclassification\tnumPositions\tnumH\tnumL\n");
		
		System.out.println(map.keySet());
		
		for( String classification : map.keySet())
		{
			Map<String,Map<String,Character>> map1 = map.get(classification);
			
			for(String seqID : map1.keySet())
			{
				int numH = 0;
				int numL = 0;
				
				Map<String,Character> map2= map1.get(seqID);
				
				for(String s : map2.keySet())
				{
					if( s.startsWith("H"))
						numH++;
					else if( s.startsWith("L"))
						numL++;
					else throw new Exception("No " + s);
					
					if( ! aaSet.contains(map2.get(s)))
						throw new Exception("No " + map2.get(s));
				}
				
				writer.write(seqID + "\t"  + classification + "\t"
						+ map1.get(seqID).size() + "\t" + numH + "\t" + 
								numL + "\n");
			}
		}	
	}
	
	/**
	 * keys are classification, then sequenceid, then position then AminoAcid
	 * 
	 * */
	private static Map< String, Map<String,Map<String,Character>>>  getMap() throws Exception
	{
		Map<String, String> fileToCatMap = getFileNameToCategoryMap();
		
		Map< String, Map<String,Map<String,Character>>> map = new HashMap<>();
		
		File topDir = new File(ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"oneAtATime" + File.separator + 
						"inputFiles" + File.separator + "Aug_1" );
		
		String[] files = topDir.list();
		
		for( String name : files)
			if( ! name.endsWith("~") && ! name.endsWith("swp"))
		{
			System.out.println(name);
			String assignment = fileToCatMap.get(name);
			
			if( assignment == null)
				throw new Exception("No");
			
			Map<String,Map<String,Character>> map1 = map.get(assignment);
			
			if( map1 == null)
			{
				map1 =new LinkedHashMap<String,Map<String,Character>>();
				map.put(assignment, map1);
			}
			
			Map<String,Character> seqMap = null;
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(topDir.getAbsolutePath()
					+ File.separator + name)));
			
			for(String s= reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
			{
				if( s.startsWith(">"))
				{
					String key = s.substring(1, s.lastIndexOf("|"));
					System.out.println(key);
					
					seqMap = map1.get(key);
					
					if( seqMap == null)
					{
						seqMap = new HashMap<String,Character>();
						map1.put(key, seqMap);
					}
				}
				else
				{
					StringTokenizer sToken =new StringTokenizer(s);
					
					if( sToken.countTokens() != 2)
						throw new Exception("No " + s);
					
					String key = new String(sToken.nextToken());
				
					String charString = sToken.nextToken();
					if( charString.length() != 1)
						throw new Exception("No " + s);
					
					if( seqMap.containsKey(key) && seqMap.get(key) != charString.charAt(0))
					{
						throw new Exception("Duplicate mismatch" + key + " " + seqMap.get(key) + " " + charString.charAt(0));
					}
						
					seqMap.put(key, charString.charAt(0));
				}
			}
			
			reader.close();
		}
		
		return map;
	}
	
}
