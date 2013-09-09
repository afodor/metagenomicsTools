/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


package ratSaccharine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MergeMap
{
	private static HashMap<String, String> readMapFile() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() + File.separator + 
				"mapOut.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			String firstToken = sToken.nextToken();
			
			if( map.containsKey( firstToken))
				throw new Exception("Duplicate");
			
			map.put(firstToken, s);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> mapLines = readMapFile();
		
	}
}
