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

package eTree;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import probabilisticNW.ProbSequence;
import utils.ConfigReader;

public class RunOneSingleThread
{
	/*
	 *  First run dereplicate.DereplicateBySampleIntoOneFile
	 */
	public static void main(String[] args) throws Exception
	{
		HashSet<String> includedSequences = getIncludedSequences(ConfigReader.getETreeTestDir() +File.separator + 
				"gastro454DataSet" + File.separator + "melUchimeClean.fasta");
		
		ETree eTree = new ETree();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(ConfigReader.getETreeTestDir() +File.separator + 
				"gastro454DataSet" + File.separator + "mel74DerupLumped.fna");
		
		int x=0;
		for( FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			if( includedSequences.contains(fs.getFirstTokenOfHeader()) )
			{
				//System.out.println(fs.getHeader());
				StringTokenizer numToken = new StringTokenizer(fs.getFirstTokenOfHeader(), "=");
				numToken.nextToken();
				int n = Integer.parseInt(numToken.nextToken());
				int sum =0;
				StringTokenizer sToken = new StringTokenizer(fs.getHeader(), " ,");
				sToken.nextToken();
				HashMap<String, Integer> map = getSampleMap(sToken);
				for( String s : map.keySet())
					sum += map.get(s);
				
				if( sum != n)
					throw new Exception("No " + sum + " " + n);
				
				ProbSequence probSeq = new ProbSequence( fs.getSequence(), n, map );
				eTree.addSequence(probSeq);
				x++;
				if( x% 10 ==0)
					System.out.println("Finished " + x);
			}
		}
		
		eTree.writeAsSerializedObject(ConfigReader.getETreeTestDir() + File.separator + "mel74withsingletonsChimeraChecked.etree");
		eTree.writeAsXML(ConfigReader.getETreeTestDir() + File.separator + "mel74withsingletonsPhyloxml.xml");
	}
	
	private static HashMap<String, Integer> getSampleMap(StringTokenizer sToken) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		while( sToken.hasMoreTokens())
		{
			StringTokenizer innerToken = new StringTokenizer(sToken.nextToken(), "=");
			String key = innerToken.nextToken();
			
			if( key.startsWith("{"))
				key = key.substring(1);
			
			String valString = innerToken.nextToken();
			
			if( valString.endsWith("}"))
				valString= valString.substring(0, valString.length() -1);
			
			if( map.containsKey(key))
				throw new Exception("No " + key + " " + map);
			
			map.put(key, Integer.parseInt(valString));
			
			if( innerToken.hasMoreTokens())
				throw new Exception("No");
			
		}
		
		return map;
	}
	
	private static HashSet<String> getIncludedSequences(String uchimeOutFilePath) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(uchimeOutFilePath);
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			String key =fs.getFirstTokenOfHeader();
			
			if( set.contains(key))
				throw new Exception("No");
			
			set.add(key);
		}
		
		return set;
	}
}
