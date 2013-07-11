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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

import fileAbstractions.PairedReads;

public class RunAll
{

	public static void main(String[] args) throws Exception
	{
		List<PairedReads> pairedList = getAllBurkholderiaPairs();
	}
	
	public static List<PairedReads> getAllBurkholderiaPairs() throws Exception
	{
		List<PairedReads> list = new ArrayList<>();
		
		File dir = new File(ConfigReader.getBurkholderiaDir());
		
		String[] files= dir.list();
		List<String> fileNames = new ArrayList<>();
		
		for( String s : files)
		{
			if( s.endsWith(".gz") )
				fileNames.add(s);
		}
		
		for( int x=0; x < fileNames.size(); x++)
		{
			list.add( makePairedReads( dir.getAbsolutePath() + File.separator +  fileNames.get(x), 
						dir.getAbsolutePath() + File.separator + fileNames.get(x+1)) );
			x++;
		}
		
		for(PairedReads pr : list)
		{
			StringTokenizer sToken1= new StringTokenizer(pr.getFirstRead().getName(), "_");
			StringTokenizer sToken2 = new StringTokenizer(pr.getSecondRead().getName(), "_");
			
			String s1 = sToken1.nextToken() + "_" + sToken1.nextToken();
			String s2 = sToken2.nextToken() + "_" + sToken2.nextToken();
			System.out.println(s1 + " " + s2);
			
			if( ! s1.equals(s2))
				throw new Exception("No ");
		}
			
		return list;
	}
	
	
	private static PairedReads makePairedReads(final String filepath1, final String filepath2) throws Exception
	{
		return new PairedReads()
		{
			
			@Override
			public File getSecondRead()
			{
				return new File(filepath2);
			}
			
			@Override
			public File getFirstRead()
			{
				return new File(filepath1);
			}
		};
	}
}
