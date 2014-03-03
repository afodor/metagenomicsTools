/** 
 * Author:  anthony.fodor@gmail.com
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */

package scripts.metabolitesVs16S;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WriteOTUSpreadsheets
{
	public static void main(String[] args) throws Exception
	{
		String[] vals = { "fam" , "gen", "ord", "phy" , "cls", "counts" };
		
		for(String s : vals)
			writeALevel(s);
	}
	
	private static void writeALine(BufferedWriter writer, String s) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s);
		sToken.nextToken(); 
		writer.write(sToken.nextToken());
		sToken.nextToken();
		
		while( sToken.hasMoreTokens())
			writer.write("\t" + sToken.nextToken());
		
		writer.write("\n");
		
	}
	
	private static void writeALevel(String level) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMetabolitesCaseControl() +
				File.separator + level +"_asColumns.txt")));
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMetabolitesCaseControl()+
				File.separator + "topeFeb2014_raw_" + level + ".txt" )));
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
			writeALine(writer, s);
		
		reader.close();
		writer.flush();  writer.close();
	}
}
