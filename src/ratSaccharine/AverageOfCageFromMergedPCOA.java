package ratSaccharine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class AverageOfCageFromMergedPCOA
{
	private static final int NUM_AXES = 40;
	
	private static class Holder
	{
		String cage;
		String tissueType;
		String phenotype;
		List<Double> data= new ArrayList<Double>();
		int n =0;
		
		Holder()
		{
			for( int x=0; x < NUM_AXES; x++)
				data.add(0.0);
		}
		
	}
	
	private static void writeResults( HashMap<String, Holder> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getSaccharineRatDir() +
				File.separator + "averageMergedBrayCurtisPCOA.txt"));
		
		writer.write("cage\ttissue\tphenotype\tnumAnimals");
		
		for( int x=0; x < NUM_AXES; x++)
			writer.write("\tPCOA_" + (x+1));
		
		writer.write("\n");
		
		for( Holder h : map.values())
		{
			writer.write(h.cage + "\t");
			writer.write(h.tissueType + "\t");
			writer.write(h.phenotype + "\t");
			writer.write("" + h.n);
			
			for( int x=0; x < NUM_AXES; x++)
				writer.write("\t" + h.data.get(x)/h.n);
			

			writer.write("\n");
			
			
		}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, Holder> getHolders() throws Exception
	{
		HashMap<String, Holder> map = new HashMap<String, Holder>();
		
		BufferedReader reader= new BufferedReader(new FileReader(new File(ConfigReader.getSaccharineRatDir() + 
				File.separator + "mergedMapBrayCurtisPCOA.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			
			sToken.nextToken();sToken.nextToken();sToken.nextToken();
			
			String tissue = sToken.nextToken();
			sToken.nextToken();sToken.nextToken();sToken.nextToken();
			
			String phenotype =sToken.nextToken();
			String cage = sToken.nextToken();
			
			for( int x=0; x < 15; x++)
				sToken.nextToken();
			
			String key = cage + "_" + tissue;
			
			Holder h = map.get(key);
			
			if( h == null)
			{
				h = new Holder();
				h.cage = cage;
				h.tissueType = tissue;
				h.phenotype = phenotype;
				map.put(key,h);
			}
			else
			{
				if( ! h.cage.equals(cage))
					throw new Exception ("NO");
				

				if( ! h.tissueType.equals(tissue))
					throw new Exception ("NO");
				
				if( ! h.phenotype.equals(phenotype))
					throw new Exception ("NO");
			}
			
			h.n++;
			
			for( int x=0; x < NUM_AXES; x++)
				h.data.set(x, h.data.get(x) + Double.parseDouble(sToken.nextToken()));
			
			if( sToken.hasMoreTokens())
				throw new Exception("No");
		}
		
		reader.close();
		
		return map;
	}
		
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = getHolders();
		System.out.println(map);
		writeResults(map);
	}
}
