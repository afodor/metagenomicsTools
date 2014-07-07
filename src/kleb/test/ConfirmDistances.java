package kleb.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class ConfirmDistances
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getSequencesByTag();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getKlebDir() + File.separator +
				"distancesUpperTriangle.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String seq1 = map.get(splits[0]);
			String seq2 = map.get(splits[1]);
			
			if( seq1.length() != seq2.length())
				throw new Exception("No");
			
			int distance =0;
			
			for( int x=0; x < seq1.length(); x++)
				if( seq1.charAt(x) != seq2.charAt(x))
					distance++;
			
			System.out.println(seq1.length() + " " + distance + " "+ Integer.parseInt(splits[2]));
			
			if( distance != Integer.parseInt(splits[2]))
				throw new Exception("No " + distance + " " + splits[2]);
		}
				
	}
	
	private static void checkLine(String s) throws Exception
	{
		for(char c : s.toCharArray())
		{
			if ( c != 'A' && c != 'C' && c != 'G' && c != 'T')
				throw new Exception("NO");
		}
		System.out.println(s.length());
	}
	
	private static HashMap<String, String> getSequencesByTag() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getKlebDir() + File.separator + 
			"all76.mfa")));
		
		StringBuffer buff = new StringBuffer();
		String oldKey = null;
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.startsWith(">"))
			{
				if( oldKey != null)
				{
					map.put(oldKey, buff.toString());
					checkLine(map.get(oldKey));
					buff = new StringBuffer();
				
				}
				oldKey = s.replace(">", "").replace("_V1", "");
			}
			else
			{
				buff.append(s.trim());
			}
		}
		map.put(oldKey, buff.toString());
		checkLine(map.get(oldKey));
			
		return map;
	}
}
