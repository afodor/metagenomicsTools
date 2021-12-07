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
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = getOldMap();
		
	//	for(String s : map.keySet())
//		{
	//		System.out.println(s + " "  + map.get(s).oneMonth + " " + map.get(s).twelveMonth);
	//	}
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\bariatricSurgery_Daisy\\fromDaisy\\updated_phylum_Metaphlan.txt" )));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			tReader.nextToken();
			
			String patientId = tReader.nextToken();
			
			Holder h = map.get(patientId);
			
			if( h != null )
			{
				for( int x=0; x < 39; x++)
					tReader.nextToken();
				
				String oneToTwelve = tReader.nextToken();
				
				if( ! oneToTwelve.equals("NA") )
				{
					Double oneVal = Double.parseDouble(h.oneMonth);
					Double twleveVal = Double.parseDouble(h.twelveMonth);
					
					double percentChange = (twleveVal -oneVal ) / oneVal;
					
					System.out.println("MATCH " + patientId + " " + oneToTwelve + " " + percentChange);
					
					Double daisyVal = Double.parseDouble(oneToTwelve);
					
					if( Math.abs(daisyVal - percentChange) > 0.0001)
						throw new Exception("Fail");
					
				}
				
				
			}
		}
		
		System.out.println("PASS");
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
