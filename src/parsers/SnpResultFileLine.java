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


package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import coPhylog.ContextCount;

public class SnpResultFileLine
{
	//longID	dunif	pValue	bhCorrected	bonfCorrected	counts1	count2	list1	list2

	private final long longID;
	private final double dUnif;
	private final double pValue;
	private final double bhCorrected;
	private final double bonfCorrected;
	private final String counts1;
	private final String counts2;
	private final String list1;
	private final String list2;
	
	public ContextCount getContextCount(boolean firstList) throws Exception
	{
		List<Integer> list= getAsInts(firstList);
		
		return new ContextCount(list.get(0), list.get(1), list.get(2),list.get(3));
	}
	
	public static List<Integer> parseTextList(String s1) throws Exception
	{
		s1 = s1.replace("[", "");
		s1 = s1.replace("]", "");
		
		StringTokenizer sToken = new StringTokenizer(s1, ",");
		
		List<Integer> list = new ArrayList<Integer>();
		
		for( int x=0; x < 4; x++)
			list.add(Integer.parseInt(sToken.nextToken()));
		
		if( sToken.hasMoreTokens())
			throw new Exception("Unexpected token " + sToken.nextToken());
		
		return list;
	
	}
	
	public List<Integer> getAsInts(boolean firstlist) throws Exception
	{
		String s1 = firstlist ? counts1 : counts2;
			
		return parseTextList(s1);
	}
	
	public long getLongID()
	{
		return longID;
	}

	public double getdUnif()
	{
		return dUnif;
	}

	public double getpValue()
	{
		return pValue;
	}
	
	public double getBhCorrected()
	{
		return bhCorrected;
	}

	public double getBonfCorrected()
	{
		return bonfCorrected;
	}

	public String getCounts1()
	{
		return counts1;
	}

	public String getCounts2()
	{
		return counts2;
	}

	public String getList1()
	{
		return list1;
	}

	public String getList2()
	{
		return list2;
	}

	private SnpResultFileLine(String s) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, "\t");
		this.longID = Long.parseLong(sToken.nextToken());
		this.dUnif = Double.parseDouble(sToken.nextToken());
		this.pValue = Double.parseDouble(sToken.nextToken());
		this.bhCorrected = Double.parseDouble(sToken.nextToken());
		this.bonfCorrected = Double.parseDouble(sToken.nextToken());
		this.counts1 = sToken.nextToken();
		this.counts2 = sToken.nextToken();
		this.list1 = sToken.nextToken();
		this.list2= sToken.nextToken();
		
		if( sToken.hasMoreTokens())
			throw new Exception("No");
	}
	
	public static HashMap<Long, SnpResultFileLine> parseFile(File file) throws Exception
	{
		HashMap<Long, SnpResultFileLine> map = new HashMap<Long, SnpResultFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			SnpResultFileLine snp = new SnpResultFileLine(s);
			
			if(map.containsKey(snp.getLongID()))
				throw new Exception("Duplicate id " + snp.getLongID());
			
			map.put(snp.getLongID(), snp);
		}
		
		return map;
	}
	
	public static HashMap<Long, SnpResultFileLine> filter(HashMap<Long, SnpResultFileLine> map, double minPValue ) throws Exception
	{
		HashMap<Long, SnpResultFileLine> returnMap = new HashMap<Long, SnpResultFileLine>();
		
		for( Long l : map.keySet() )
		{
			SnpResultFileLine snp = map.get(l);
			
			if( snp.getpValue() <= minPValue)
				returnMap.put(l, snp);
		}
		
		return returnMap;
	}
}
