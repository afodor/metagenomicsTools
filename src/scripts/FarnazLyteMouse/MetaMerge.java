package scripts.FarnazLyteMouse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import utils.TabReader;

public class MetaMerge
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getKeyMap();
	}
	
	/*
	 * Key is animalId@dateString
	 */
	public static HashMap<String, String> getKeyMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
			"C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\input\\" + 
					"MCBT_RevisedMetadata_5-9-2020_01_FF.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			TabReader tReader =new TabReader(s);
			
			String firstToken = tReader.nextToken().trim();
			
			if( firstToken.length() > 0 )
			{
				System.out.println( firstToken);
			}
		}
		
		return map;
	}
}
