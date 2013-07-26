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

package hmpReferenceGenomes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;
import utils.TabReader;

public class GenerateWGETs
{
	public static void main(String[] args) throws Exception
	{
		// this file downloaded from http://hmpdacc.org/reference_genomes/reference_genomes.php
		// on July 23, 2013
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getBigBlastDir() + File.separator + "project_catalog.txt"	)));
		
		reader.readLine();
		
		int count =0;
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			TabReader tr = new TabReader(s);
			
			for(int x=0; x < 10; x++)
				tr.nextToken();
			
			String genbankID = tr.nextToken().trim();
			
			if(genbankID.length() > 0)
			{
				System.out.println("nice wget http://www.ncbi.nlm.nih.gov/Traces/wgs/?download=" + 
						genbankID.substring(0,4) + 	"01.fsa.1.gz");
				count++;
			}
		}
		
		System.out.println(count);
	}
}
