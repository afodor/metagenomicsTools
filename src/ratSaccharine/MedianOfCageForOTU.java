package ratSaccharine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import utils.ConfigReader;

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
public class MedianOfCageForOTU
{
	private static HashMap<String, String> getCagePhenotypeMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getSaccharineRatDir() + File.separator+ "mergedUnweightedUnifrac.txt"	)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			for( int x=0; x< 7; x++)
				sToken.nextToken();
			
			String phenotype = sToken.nextToken();
			String cage = sToken.nextToken();
			
			String oldPhenotype = map.get(cage);
			
			if( oldPhenotype == null)
			{
				map.put(cage, phenotype);
			}
			else if( ! oldPhenotype.equals(phenotype))
				throw new Exception("No");
				
			
		}
		
		reader.close();
		return map;
	}
	
	private static HashMap<String, Set<String>> getCageKey() throws Exception
	{
		HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getSaccharineRatDir() + File.separator+ "mergedUnweightedUnifrac.txt"	)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			String id = sToken.nextToken();
			
			for( int x=0; x< 7; x++)
				sToken.nextToken();
			
			String cage = sToken.nextToken();
			
			Set<String> innerSet = map.get(cage);
			
			if( innerSet == null)
			{
				innerSet = new HashSet<String>();
				map.put(cage, innerSet);
			}
			
			if(innerSet.contains(id))
				throw  new Exception("NO");
			
			innerSet.add(id);
		}
		
		reader.close();
		return map;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Set<String>> cageMap = getCageKey();
		System.out.println(cageMap);
		HashMap<String, String> cagePhenotypeMap = getCagePhenotypeMap();
		System.out.println(cagePhenotypeMap);
	}
}
