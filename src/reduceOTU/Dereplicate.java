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


package reduceOTU;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class Dereplicate
{
	public static void main(String[] args) throws Exception
	{
		File inputFasta = new File(ConfigReader.getReducedOTUDir() + File.separator + "postLucyFiltering.txt");
		File derepFile = new File(ConfigReader.getReducedOTUDir() + File.separator + "derepped.txt");
		
		writeDeReplicateFile(inputFasta, derepFile);
	}
	
	public static void writeDeReplicateFile(File inputFasta, File outputFile )
		throws Exception
	{
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(inputFasta);
		HashMap<String, Integer> dereps = new HashMap<String,Integer>();
		
		for( FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
		{
			Integer i = dereps.get(fs.getSequence());
			
			if( i == null)
				i =0;
			
			i++;
			
			dereps.put(fs.getSequence(),i);
		}
		
		fsoat.close();
		BufferedWriter writer =new BufferedWriter(new FileWriter(outputFile));
		writer.write("sequence\tcount\n");
		
		for(String s: dereps.keySet())
			writer.write(s +"\t" + dereps.get(s) + "\n");
		
		writer.flush();  writer.close();
	}
}
