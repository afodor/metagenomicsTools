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
import java.util.HashSet;

import utils.ConfigReader;

/*
 * This has dependencies on CountSNPs
 */
public class CreateSNPPivotTable
{
	public static void main(String[] args) throws Exception
	{
		HashSet<Long> ids = getSnpIds();
		System.out.println(ids.size());
	}
	
	private static HashSet<Long> getSnpIds() throws Exception
	{
		HashSet<Long> set = new HashSet<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getBurkholderiaDir() + File.separator + "summary" +
					File.separator + "details.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			set.add(Long.parseLong(s.split("\t")[5]));
		}
		
		return set;
	}
}
