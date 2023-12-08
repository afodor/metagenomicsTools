package scripts.KeRarify;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;

public class QuickCollectorCurve
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\keRarify\\amr_transposed.txt");

		//outer key is the sample name
		// inner map - key is rarefication depth; value is the taxa count at that depth
		HashMap<String, HashMap<Integer, Integer>> map = new HashMap<>();
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName =wrapper.getSampleNames().get(x);
			
			if( map.containsKey(sampleName))
				throw new Exception("Duplicate key");
			
			HashMap<Integer, Integer> innerMap = new HashMap<>();
			map.put(sampleName, innerMap);
			
			List<Integer> countList = getCountsList(wrapper, x);
			
			System.out.println(countList);
			System.exit(1);

			
		}
	}
	
	private static List<Integer> getCountsList(OtuWrapper wrapper, int taxaIndex) throws Exception
	{
		List<Integer> countList = new ArrayList<>();
		
		for( int y=0; y < wrapper.getDataPointsUnnormalized().get(taxaIndex).size() ; y++)
		{
			int countCal = wrapper.getDataPointsUnnormalized().get(taxaIndex).get(y).intValue();
			//System.out.println(sampleName + " " +  y + " " +  countCal);
			
			for( int z=0; z < countCal; z++)
				countList.add(y);
		}
		
		return countList;
		
	}

}
