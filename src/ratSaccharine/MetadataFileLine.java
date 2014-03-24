package ratSaccharine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
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
public class MetadataFileLine
{
	private final String cage;
	private final String id;
	private final String tissue;
	private final String phenotype;
	
	private MetadataFileLine(String s) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s);
		
		this.id = sToken.nextToken();
		
		sToken.nextToken();  sToken.nextToken();
		
		this.tissue = sToken.nextToken();
		
		sToken.nextToken();  sToken.nextToken(); sToken.nextToken();  
		
		this.phenotype = sToken.nextToken();
		this.cage = sToken.nextToken();
		
	}
	
	public static HashMap<String, MetadataFileLine> getMap() throws Exception
	{
		HashMap<String, MetadataFileLine> map = new HashMap<String, MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() 
				+ File.separator +
				"mergedMapBrayCurtisPCOA.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s!= null; s= reader.readLine())
		{
			MetadataFileLine mfl = new MetadataFileLine(s);
			
			if( map.containsKey(mfl.getId()))
				throw new Exception("No");
			
			map.put(mfl.getId(), mfl);
		}
		
		reader.close();
		
		return map;
	}
	public String getCage()
	{
		return cage;
	}
	public String getId()
	{
		return id;
	}
	public String getTissue()
	{
		return tissue;
	}
	public String getPhenotype()
	{
		return phenotype;
	}
	
	
}
