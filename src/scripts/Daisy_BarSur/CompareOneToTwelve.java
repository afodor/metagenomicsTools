package scripts.Daisy_BarSur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.TabReader;

public class CompareOneToTwelve
{
	private static class Holder
	{
		String oneMonth;
		String twelveMonth;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = getOldMap();
		
		for(String s : map.keySet())
		{
			System.out.println(s + " "  + map.get(s).oneMonth + " " + map.get(s).twelveMonth);
		}
	}
	
	@SuppressWarnings("resource")
	private static HashMap<String, Holder> getOldMap() throws Exception
	{
		HashMap<String, Holder> map = new LinkedHashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\BariatricSurgery_Analyses2021-main\\input\\AF_Merged\\mergedMeta_phylum.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			for( int x=0;x  < 4; x++)
				tReader.nextToken();
			
			String patientId = tReader.nextToken();
			
			for( int x=0;x  < 3; x++)
				tReader.nextToken();
			
			
			Holder h= map.get(patientId);
			
			if( h == null)
			{
				h = new Holder();
				map.put(patientId, h);
			}
			
			String oneMWeightString = tReader.nextToken();
			
			tReader.nextToken();tReader.nextToken();
			
			String twelveWeightString = tReader.nextToken();
			
			if( h.oneMonth != null && ! h.oneMonth.equals(oneMWeightString))
				throw new Exception("No " + patientId + " " + h.oneMonth + " " + oneMWeightString);

			if( h.twelveMonth!= null && ! h.twelveMonth.equals(twelveWeightString))
				throw new Exception("No " + patientId + " " + h.twelveMonth+ " " + twelveWeightString);
			
			h.oneMonth = oneMWeightString;
			h.twelveMonth = twelveWeightString;
		}
		
		return map;
	}
}