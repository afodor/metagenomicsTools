package scripts.PeterAntibody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
		
		String[] files = new File(ConfigReader.getPeterAntibodyDirectory() + File.separator + 
					"oneAtATime" + File.separator + "inputFiles").list();
		
		for( String name : files)
		{
			System.out.println(name);
			String assignment = fileToCatMap.get(name);
			
			if( assignment == null)
				throw new Exception("No");
			
			HashMap<String,HashMap<String,Character>> map1 = map.get(assignment);
			
			if( assignment == null)
			{
				map1 =new HashMap<String,HashMap<String,Character>>();
				map.put(assignment, map1);
			}
		}
		
		return map;
	}
	
}
