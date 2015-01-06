package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

import java.util.HashMap;


public class RDPLookup
{
	/*
	 * Run mark.CollectRDPLines first in the clusterstuff package...
	 */
	public static HashMap<String, String> getRDPLookupByGenus() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getRachSachReanalysisDir()
				+ File.separator + "rdpAnalysis" + File.separator + "rdpLines.txt")));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split(";");
			
			String genus = splits[splits.length-1].substring(3);
			
			if( map.containsKey(genus))
				throw new Exception("Duplicate " + genus);
			
			map.put(genus, s);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> genusMap = getRDPLookupByGenus();
		
		for(String s : genusMap.keySet())
			System.out.println(s );
	}
}
