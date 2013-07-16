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


package scripts.sequenceScripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;

import parsers.SnpResultFileLine;

import utils.ConfigReader;

public class SnpTableToFasta
{
	private static class Holder
	{
		String strainName;
		StringBuffer buff = new StringBuffer();
	}
	
	/*
	 * This has dependencies on CreateSNPPivotTable
	 */
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getBurkholderiaDir() + 
				File.separator + "summary" + File.separator + "pivotedSNPS.txt")));
		
		String header = reader.readLine();
		
		StringTokenizer sToken = new StringTokenizer(header, "\t");
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
		{
			Holder h = new Holder();
			h.strainName = sToken.nextToken();
			list.add(h);
		}
		
		int numOk=0;
		int numNotOk = 0;
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			if(lineIsOk(s))
			{
				numOk++;
				
				sToken = new StringTokenizer(s);
				sToken.nextToken();
				
				for(int x=0; x < list.size(); x++)
				{
					List<Integer> cellList = SnpResultFileLine.parseTextList(sToken.nextToken());
					list.get(x).buff.append(getMaxChar(cellList));
				}
			}
			else
			{
				numNotOk++;
			}
				
		}
		
		System.out.println(numOk + " " + numNotOk);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getBurkholderiaDir() + File.separator + "summary" + 
						File.separator + "summaryFastaNoSingletons.txt")));
		
		//list = excludeSingetonColumns(list);
		
		for(Holder h : list)
		{
			writer.write(">" + h.strainName + "\n");
			writer.write(h.buff.toString() + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
		
	}
	
	/*
	private static List<Holder> excludeSingetonColumns(List<Holder> inList)
		throws Exception
	{
		HashSet<Integer> excludedColumns = new LinkedHashSet<>();
		
		int length = inList.get(0).buff.length();
		
		for(Holder h : inList)
			if( h.buff.length() != length)
				throw new Exception("Logic error");
		
		for( int x=0; x < length; x++)
		{
			HashMap<Character,Integer> map = new HashMap<>();
			
			for(Holder h : inList)
			{
				Integer i = map.get(h.buff.charAt(x) );
				
				if( i == null )
				{
					i = 0;
				}
				
				i++;
				map.put(h.buff.charAt(x) , i);
			}
			System.out.println(x + " " + map);
			
			if( map.size() == 2)
			{
				List<Integer> numbers = new ArrayList<>(map.values());
				Collections.sort(numbers);
				
				if( numbers.get(0) <= 2)
					excludedColumns.add(x);
			}
		}
		
		System.out.println(excludedColumns);
		
		List<Holder> newList = new ArrayList<>();
		
		for( Holder h : inList)
		{
			Holder newHolder = new Holder();
			newList.add(newHolder);
			newHolder.strainName = h.strainName;
			
			for( int x=0; x < length; x++)
				if(! excludedColumns.contains(x))
					newHolder.buff.append(h.buff.charAt(x));
		}
		
		return newList;
	}
	*/
	
	private static char getMaxChar( List<Integer> list )
		throws Exception
	{
		char maxChar = '-';
		int max =-1;
		
		if( list.get(0) > max )
		{
			maxChar = 'A';
			max = list.get(0);
		}
		
		if( list.get(1) >= max)
		{
			maxChar = 'C';
			max = list.get(1);
		}
		
		if( list.get(2) >= max)
		{
			maxChar = 'G';
			max = list.get(2);
		}
		
		if( list.get(3) >= max)
		{
			maxChar = 'T';
			max = list.get(3);
		}
		
		return maxChar;
	}
	
	private static boolean lineIsOk(String s ) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s,"\t");
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
		{
			List<Integer> list = SnpResultFileLine.parseTextList(sToken.nextToken());
			
			Collections.sort(list); 
			
			int sum =0;
			
			for(Integer i : list)
				sum += i;
			
			if( sum <= 3)
				return false;
			
			for( int x=0; x<=2; x++)
				if( list.get(x) > 1)
					return false;
		}
		
		return true;
	}
}
