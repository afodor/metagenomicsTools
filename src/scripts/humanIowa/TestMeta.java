package scripts.humanIowa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class TestMeta
{
	
	/*
	 * The outer key is one of ACT, CON and SED
	 * The middle key is the metadata name
	 * The inner key is the 
	 */
	private static HashMap<String,HashMap<String,String>> getMapFromDerviedFile() throws Exception
	{
		/*
		BufferedReader reader= new BufferedReader(new FileReader(new File(
				ConfigReader.getHumanIowa() + File.separator + "genus_withMDS.txt.txt")));
		
		String[] topSplits = 
		/*
		 * 
		
		 */
		return null;
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getHumanIowa() + File.separator + "AAA_Shook_Anthony_Fodor_4.10.18.txt"	)));
		
		String firstLine = reader.readLine();
		
		String[] topSplits = firstLine.split("\t");
		
		HashMap<TimepointAssignment,Integer> map = new HashMap<>();
		int numNull = 0;
		
		for(int x=5; x < topSplits.length; x++)
		{
			TimepointAssignment ta =getTimepointAssignment(topSplits[x]);
			
			if( ta ==null)
			{
				System.out.println(topSplits[x] );
				numNull++;
			}
			else
			{
				Integer val = map.get(ta);
				
				if( val ==null)
					val= 0;
				
				val++;
				
				map.put(ta, val);
			}
		}
		
		System.out.println( numNull );
		System.out.println(map);
	}
	
	public enum TimepointAssignment { SED, ACT, CON } 
	
	public static TimepointAssignment getTimepointAssignment(String s )
	{
		s =s.toLowerCase();
		String[] splits = s.split("_");
		
		TimepointAssignment returnVal = null;
		
		for( int x=0; x < splits.length; x++)
		{
			if( splits[x].length() >= 3)
			{

				String firstThree = splits[x].substring(0, 3);
				
				if( firstThree.equals("sed"))
				{
					if( returnVal == TimepointAssignment.ACT || returnVal == TimepointAssignment.CON)
						return null;
					
					returnVal = TimepointAssignment.SED;
				}
				

				if( firstThree.equals("act"))
				{
					if( returnVal == TimepointAssignment.SED|| returnVal == TimepointAssignment.CON)
						return null;
					
					returnVal = TimepointAssignment.ACT;
				}
				

				if( firstThree.equals("con"))
				{
					if( returnVal == TimepointAssignment.SED|| returnVal == TimepointAssignment.ACT)
						return null;
					
					returnVal = TimepointAssignment.CON;
				}
					
			}

		}
					
		return returnVal;
	}
	
}




