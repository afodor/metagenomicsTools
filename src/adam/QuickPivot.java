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

package adam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class QuickPivot
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("D:\\adam\\pivoted.txt")));
		
		writer.write("gene\tposition\tsequenceCount\tprediction\n");
		
		BufferedReader reader= new BufferedReader(new FileReader(new File("d:\\adam\\EX_CDS_dataframe_long.out")));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			//System.out.println(s);
			StringTokenizer sToken = new StringTokenizer(s);
			
			String name = sToken.nextToken();
			String[] counts = sToken.nextToken().split(",");
			String[] structures = sToken.nextToken().split(",");
			
			
			if( counts.length != structures.length)
				throw new Exception("No " + counts.length + " " + structures.length);
			
			for( int x=0; x < counts.length; x++)
			{
				writer.write(name + "\t");
				writer.write( (x+1) + "\t");
				writer.write(counts[x] + "\t");
				writer.write( structures[x] + "\n");
			}
		}
		
		writer.flush();  writer.close();
	}
}
