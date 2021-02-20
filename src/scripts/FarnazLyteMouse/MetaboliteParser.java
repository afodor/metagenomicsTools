package scripts.FarnazLyteMouse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.TabReader;

public class MetaboliteParser
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> keyMap = MetaMerge.getKeyMap();
		
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(
						"C:\\LyteManuscriptInPieces\\Beef Supplementation Data - Corrected values.txt"
						)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String anId = getOneId(s, keyMap);
			
			if( anId != null )
				System.out.println(anId);
		}
		
		reader.close();
	}
	
	private static String getOneId(String s, HashMap<String, String> keyMap ) throws Exception
	{
		String yearString = TabReader.getTokenAtIndex(s, 0);
		
		yearString = yearString.replace("2017", "17");
		yearString = yearString.replace("2018", "18");
		
		String key = TabReader.getTokenAtIndex(s, 8) + "@" + yearString;
		
		String fecalID = keyMap.get(key + "@Fecal");
		String cecalID = keyMap.get(key + "@Cecal");
		
		if( fecalID == null && cecalID == null)
		{
			System.out.println("Found neither " + key);
			return null;
		}
			
		if( fecalID != null && cecalID != null)
		{
			if( ! fecalID.equals(cecalID))
				throw new Exception("Conflicting answers " + fecalID + " " + cecalID);
			
			return fecalID;
		}
		
		if( fecalID == null)
			return cecalID;
		
		if( cecalID != null)
			throw new Exception("Logic error");
		
		return fecalID;
		
	}
	
}
