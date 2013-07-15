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
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
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
		}
		
		int numOk=0;
		int numNotOk = 0;
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			if(lineIsOk(s))
			{
				numOk++;
			}
			else
			{
				System.out.println(s);
				numNotOk++;
			}
				
		}
		
		System.out.println(numOk + " " + numNotOk);
		
		reader.close();
		
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
