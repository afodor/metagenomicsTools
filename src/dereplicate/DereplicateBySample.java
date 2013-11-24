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
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class DereplicateBySample
{
	public static String REP_PREFIX = "REP_SAMP_PREFIX_";
	public static String DEREP_PREFIX = "DEREP_SAMP_PREFIX";
	
	private static void splitBySample(String inFile, HashSet<String> goodSeqs) throws Exception
	{
		splitBySample(new File(inFile), goodSeqs);
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String sequence;
		int num;
		
		public Holder(String sequence, int num)
		{
			this.sequence = sequence;
			this.num = num;
		}

		@Override
		public int compareTo(Holder o)
		{
			return o.num - this.num;
		}
	}
	
	private static void writeDereplicateFiles( File dir) throws Exception
	{
		String[] files = dir.list();
		
		for( String s : files)
			if( s.startsWith(REP_PREFIX))
			{
				File file = new File(dir.getPath() + File.separator + s);
				
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				
				FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(file);
				
				for( FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
				{
					Integer val = map.get(fs.getSequence());
					if( val == null)
						val =0;
					val++;
					map.put(fs.getSequence(), val);
				}

				List<Holder> list = new ArrayList<Holder>();
				
				for( String seq : map.keySet())
					list.add(new Holder(seq,map.get(seq)));
				
				Collections.sort(list);
				int numWritten=1;
				int totalSequences =0;
				int numSingletons =0;
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(dir.getPath() + File.separator + DEREP_PREFIX +  s.replace(REP_PREFIX, ""))));
				
				for( Holder h : list)
				{
					//if( h.num > 1)
					{
						writer.write(">" + s.replace(REP_PREFIX, "") + "_" + numWritten + "_" + h.num + "\n" );
						writer.write(h.sequence + "\n");
						totalSequences += h.num;
						numWritten++;
					}
					//else
					{
						//numSingletons++;
					}
				}
				
				writer.flush();  writer.close();
				System.out.println(s.replace(REP_PREFIX, "") + " " + totalSequences + " " + numWritten + " " + numSingletons);
			}
	}
	
	private static void splitBySample(File inFile, HashSet<String> goodSeqs ) throws Exception
	{
		HashMap<String, BufferedWriter> map = new HashMap<String, BufferedWriter>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(inFile);
		
		for( FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			if( goodSeqs.contains(fs.getSequence()))
			{
				String sample = fs.getFirstTokenOfHeader();
				sample = new StringTokenizer(sample, "_").nextToken();
				
				BufferedWriter writer = map.get(sample);
				
				if( writer == null)
				{
					writer = new BufferedWriter(new FileWriter(new File(inFile.getParentFile() + File.separator+ REP_PREFIX + sample)));
					
					map.put(sample, writer);
				}
				
				writer.write(fs.getHeader() + "\n");
				writer.write(fs.getSequence() +"\n");
				writer.flush();
			}
			else
			{
				System.out.println("Skipping chimera " + fs.getHeader());
			}
		}
		
		for(BufferedWriter writer : map.values())
		{
			writer.flush();  writer.close();
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> goodSeqs = FastaSequence.getSeqsAsHashSet(
				ConfigReader.getETreeTestDir() + File.separator + "gastro454DataSet" + File.separator +
				"melUchimeClean.fasta");
		
		splitBySample(
				ConfigReader.getETreeTestDir() + File.separator + "gastro454DataSet" + File.separator + "mels74samples.fna",
								goodSeqs);
		writeDereplicateFiles(new File(ConfigReader.getETreeTestDir() + File.separator + "gastro454DataSet" ));
	}
}
