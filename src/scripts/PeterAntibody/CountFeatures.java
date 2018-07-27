package scripts.PeterAntibody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CountFeatures
{
	private static HashMap<String, String> getFileNameToCategoryMap()
	{
		HashMap<String, String> map = new HashMap<>();
		
		map.put("dekosky_basis_aa_hc_chothia.txt", "dekosky_basis"); 
		map.put("imgt_human_hc_chothia.txt", "imgt_human");
		map.put("mouse_heavy_germline_chothia.txt", "mouse_germline");
		map.put("dekosky_basis_aa_lc_chothia.txt","dekosky_basis");
		map.put("imgt_human_lc_chothia.txt", "mouse_germline");
		map.put("human_heavy_germline_chothia.txt", "human_germline");  
		map.put("imgt_mouse_hc_chothia.txt","imgt_mouse" ); 
		map.put("therapeutic_hc_chothia.txt", "therapeutic");
		map.put("human_kappa_germline_chothia.txt", "human_kappa_germline");  
		map.put("imgt_mouse_lc_chothia.txt", "imgt_mouse");
		map.put("therapeutic_lc_chothia.txt", "therapeutic");
		map.put("mouse_kappa_germline_chothia.txt","mouse_germline" );
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap< String, HashMap<String,HashMap<String,Character>>> map = getMap();
	}
	
	/**
	 * keys are classification, then sequenceid, then position then AminoAcid
	 * 
	 * */
	private static HashMap< String, HashMap<String,HashMap<String,Character>>>  getMap() throws Exception
	{
		HashMap<String, String> fileToCatMap = getFileNameToCategoryMap();
		
		HashMap< String, HashMap<String,HashMap<String,Character>>> map = new HashMap<>();
		
		File topDir = new File(ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				"oneAtATime" + File.separator + "inputFiles");
		
		String[] files = topDir.list();
		
		for( String name : files)
			if( ! name.endsWith("~") && ! name.endsWith("swp"))
		{
			System.out.println(name);
			String assignment = fileToCatMap.get(name);
			
			if( assignment == null)
				throw new Exception("No");
			
			HashMap<String,HashMap<String,Character>> map1 = map.get(assignment);
			
			if( map1 == null)
			{
				map1 =new HashMap<String,HashMap<String,Character>>();
				map.put(assignment, map1);
			}
			
			HashMap<String,Character> seqMap = null;
			
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
