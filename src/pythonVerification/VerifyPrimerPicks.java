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


package pythonVerification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;

public class VerifyPrimerPicks
{
	private static HashMap<String, String> getPrimerMap() throws Exception 
	{
		HashMap<String, String> map = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\PrimerKey.txt")));
		
		for(String s = reader.readLine(); s!= null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			String key = sToken.nextToken();
			String value = sToken.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("no");
			
			map.put(key, value);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\trimmedFromJava.txt")));
		
		HashMap<String, String> map = getPrimerMap();
		
		List<FastaSequence> list = FastaSequence.readFastaFile("C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\hamp_fodor_090810.fna");
		
		for( FastaSequence fs : list)
		{
			for(String s : map.keySet())
			{
				if( fs.getSequence().startsWith(s))
				{
					writer.write(">" + fs.getFirstTokenOfHeader() + "_" + map.get(s) + "\n");
					writer.write(fs.getSequence().substring(s.length()) + "\n");
				}
			}
		}
		
		writer.flush();  writer.close();
	}
	
	
}
