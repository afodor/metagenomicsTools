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


package scratch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class QuickGenusCheck
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = 
				new BufferedReader( new FileReader(new File( 
						"C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\rdpOutFromLength200")));
		
		int num=0;
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits[0].endsWith("28D1") )
			{
				double score = Double.parseDouble(splits[splits.length-1]);
				String genus = splits[splits.length-3];
				
				if(  genus.equals("Alistipes") && score >= 0.80 - 0.0001 )
				{
					System.out.println(s);
					num++;
				}
					
			}
		}
		
		System.out.println(num);
		
		reader.close();
		
		
	}
}
