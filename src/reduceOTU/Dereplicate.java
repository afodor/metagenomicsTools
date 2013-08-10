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
import java.util.HashMap;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class Dereplicate
{
	public static void main(String[] args) throws Exception
	{
		if(args.length != 2)
		{
			System.out.println( "Usage: Dereplicate inFile outFile" );
			System.exit(1);
		}
		
		File inputFasta = new File(args[0]);
		
		File outputFile = new File(args[1]);
		
		if(outputFile.exists())
		{
			System.out.println(outputFile.getAbsolutePath() + " already exits!  Exiting" );
			System.exit(1);
		}
		
		writeDeReplicateFile(inputFasta, outputFile);
	}
	
	public static void writeDeReplicateFile(File inputFasta, File outputFile )
		throws Exception
	{
		boolean zipped = false;
		
		if(inputFasta.getName().toLowerCase().endsWith("gz"))
			zipped = true;
			
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(inputFasta, zipped);
		HashMap<String, Integer> dereps = new HashMap<String,Integer>();
		
		for( FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence() )
		{
			String upperCaseSeq = fs.getSequence().toUpperCase();
			Integer i = dereps.get(upperCaseSeq);
			
			if( i == null)
				i =0;
			
			i++;
			
			dereps.put(upperCaseSeq,i);
		}
		
		fsoat.close();
		BufferedWriter writer =new BufferedWriter(new FileWriter(outputFile));
		writer.write("sequence\tcount\n");
		
		for(String s: dereps.keySet())
			writer.write(s +"\t" + dereps.get(s) + "\n");
		
		writer.flush();  writer.close();
	}
}
