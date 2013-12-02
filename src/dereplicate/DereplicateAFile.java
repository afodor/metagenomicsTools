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


package dereplicate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class DereplicateAFile
{
	private static class Holder implements Comparable<Holder>
	{
		String sequence;
		int count;
		
		private Holder(String sequence, int count)
		{
			this.sequence = sequence;
			this.count = count;
		}


		@Override
		public int compareTo(Holder o)
		{
			return o.count - this.count;
		}
	}
	
	public static List<Holder> getSequenceAndCounts(String fastaFilePath) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(fastaFilePath);
		
		for( FastaSequence fs = fsoat.getNextSequence(); fs != null; )
		{
			if( fs.isOnlyACGT())
			{
				Integer count = map.get(fs.getSequence());
				
				if( count == null)
					count =0;
				
				count++;
				
				map.put(fs.getSequence(), count);
			}
		}
		
		fsoat.close();
		
		List<Holder> list = new ArrayList<Holder>();
		
		for( String key : map.keySet())
			list.add(new Holder(key, map.get(key)));
		
		Collections.sort(list);
		
		return list;
		
	}
	
	private static void dereplicateFile(String inFile, String outFile, String sampleName) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
		
		
		
		writer.flush();  writer.close();
		
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
}
