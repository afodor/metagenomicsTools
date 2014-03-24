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

package ratSaccharine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class OTUViaTTest
{

	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataFileLine> map = MetadataFileLine.getMap();
		
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getSaccharineRatDir() + File.separator + "otuCounts.txt");
		
		
		
		System.out.println(map);
	}
	
	private static class Holder
	{
		// the key is the cage
		// the outer list has one entry for each OTU
		// the inner list has one entry for each cage
		HashMap<String, List<List<Double>>> lowSachData = new HashMap<String, List<List<Double>>>();
		HashMap<String, List<List<Double>>> highSachData = new HashMap<String, List<List<Double>>>();
	}
	
	/*
	private static void writeTTestFile(String tissue, HashMap<String, MetadataFileLine> metaMap, OtuWrapper wrapper)
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getSaccharineRatDir() + 
				File.separator + "otuTTests"  + tissue + ".txt")));
		
		for( int x=0; x < wrapper.
		
		writer.flush();  writer.close();
	}
	*/
	
	
}
