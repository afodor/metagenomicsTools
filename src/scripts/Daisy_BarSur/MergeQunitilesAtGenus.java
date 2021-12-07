package scripts.Daisy_BarSur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class MergeQunitilesAtGenus
{
	private static class Holder
	{
		Double[] krakenQunits = new Double[5];
		Double[] metaphlanQunits = new Double[5];
		
		
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map  =new HashMap<>();
		
		addAFile( map, new File("C:\\bariatricSurgery_Daisy\\fromDaisy\\allPValuesToTimeByQuintileGenusKraken.txt"), true );

		addAFile( map, new File("C:\\bariatricSurgery_Daisy\\fromDaisy\\allPValuesToTimeByQuintileGenusMetaphlan.txt"), false);
	}
	
	private static void addAFile( HashMap<String, Holder> map, File f , boolean isKraken ) throws Exception
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s=  reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			String taxa = splits[2];
			
			Holder h = map.get(taxa);
			
			if( h== null)
			{
				h = new Holder();
				map.put(taxa, h);
			}
			
			Double[] d = isKraken ? h.krakenQunits : h.metaphlanQunits;
			
			int index = Integer.parseInt(splits[1]) -1;
			
			if(  d[index] != null)
				throw new Exception("No");
			
			if( ! splits[0].equals("NA"))
			d[index] = Double.parseDouble(splits[0]);
		}
		
		reader.close();
	}
	
}
