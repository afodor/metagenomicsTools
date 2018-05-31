package scripts.humanIowa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import scripts.humanIowa.TestMeta.TimepointAssignment;
import utils.ConfigReader;

public class FindExactlyOneMetaPerTimepoint
{	
	
	private static class Holder
	{
		int sed =0;
		int act =0;
		int con =0;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> metaMap = getMetaMap();
		
		for(String s : metaMap.keySet())
		{
			Holder h = metaMap.get(s);
			
			System.out.println(s + " " + h.sed + " "+ h.act + " " +h.con);
		}
		
		int numPerfectMatch =0;
		
		for( Holder h : metaMap.values() )
			if( h.act == 1 && h.con ==1 && h.sed == 1)
				numPerfectMatch++;
		
		System.out.println(numPerfectMatch + " " + metaMap.size());
	}
	

	public static HashMap<String, Holder> getMetaMap() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getHumanIowa() + File.separator + "AAA_Shook_Anthony_Fodor_4.10.18.txt"	)));
		
		String firstLine = reader.readLine();
		
		String[] topSplits = firstLine.split("\t");
	
		HashMap<String, Holder> map = new HashMap<>();
		
		for( int x=5; x < topSplits.length; x++)
		{
			String val = topSplits[x];
			
			TimepointAssignment ta = TestMeta.getTimepointAssignment(val);
			
			if( ta != null)
			{
				String key = val.toLowerCase().replaceAll(
						ta.toString().toLowerCase(), "");
				
				Holder h = map.get(key);
				
				if( h == null)
				{
					h = new Holder();
					map.put(key,h );
				}
				
				if( ta == TimepointAssignment.ACT)
					h.act++;
				if( ta == TimepointAssignment.CON)
					h.con++;
				if( ta == TimepointAssignment.SED)
					h.sed++;
			}
		}
		
		return map;
	}
}

