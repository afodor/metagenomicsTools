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


package bottomUpTree.rdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.TabReader;

public class RdpToJson
{
	private static HashMap<String, Integer> getCaseControlMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getNinaWithDuplicatesDir() + File.separator + 
				"TopeNewDataMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader sToken = new TabReader(s);
			
			String key = sToken.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("Duplicate " + key);
			
			for( int x=0; x < 5; x++)
				sToken.nextToken();
			
			int caseContolInt = Integer.parseInt(sToken.nextToken());
			
			// 1 is case; 0 is control
			if( caseContolInt != 0 && caseContolInt != 1)
				throw new Exception("Illegal val " + caseContolInt);
			
			map.put(key, caseContolInt);
		}
		
		reader.close();
		
		//for(String s : map.keySet())
			//System.out.println(s);
		
		return map;
	}
	
	private static String getKeyFromFilename(String s)
	{
		s = s.replace("Tope_","");
		s = s.replace(".fas_rdpOut.txt.gz", "");
		 
		StringTokenizer sToken = new StringTokenizer(s, "_");
		
		s = sToken.nextToken();
		
		if( s.endsWith("A"))
			s = s.substring(0, s.length()-1);
		
		//System.out.println("Stripping an A " + s);
		
		return s;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> caseControlMap = getCaseControlMap();
		
		File rdpDir = new File(ConfigReader.getNinaWithDuplicatesDir() + File.separator + "rdp" );
		
		String[] names = rdpDir.list();
		
		for( String s : names)
		{
			//System.out.println(s);
			String key =getKeyFromFilename(s);
			Integer caseControl = caseControlMap.get(key);
			
			if( caseControl == null)
				throw new Exception("Could not find " + key);
		}
	}
}
