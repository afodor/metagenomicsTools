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


package sandraJune2012Rivers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;

import scripts.pancreatitis.PivotOTUs;
import utils.ConfigReader;

public class DirectRDPPivotWithNoChimeras
{
	public static int THRESHOLD=50;
	
	private static HashSet<String> getNonChimericIDs() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getSandraRiverJune2012Dir()+
				File.separator + "uchimeOutSandraThreePrime.txt")));
		
		for(String s= reader.readLine(); s!= null; s=reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			sToken.nextToken();
			
			String id = sToken.nextToken();
			
			if(set.contains(id))
				throw new Exception("Unexpected duplicate");
			
			while( sToken.countTokens() > 1)
				sToken.nextToken();
			
			String lastVal = sToken.nextToken();
			
			if( lastVal.equals("N"))
				set.add(id);
			
			if( sToken.hasMoreTokens())
				throw new Exception("Parsing error");
		}
		
		reader.close();
		
		return set;
	}
	
	private static File writeALevel(String level,HashSet<String> nonChimericIDs, 
						List<NewRDPParserFileLine> rdpList) throws Exception
	{
		int numWritten =0;
		File outFile = new File(ConfigReader.getSandraRiverJune2012Dir() + File.separator + 
						 level + "_directSequencesToTaxa.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		writer.write("sequence\tsample\ttaxa\n");
		
		for( NewRDPParserFileLine fileLine : rdpList)
		{
			NewRDPNode aNode = fileLine.getTaxaMap().get(level);
			
			if( aNode != null && aNode.getScore() >= THRESHOLD)
			{
				StringTokenizer sToken = new StringTokenizer(fileLine.getSequenceId(), "_");
				
				if( sToken.countTokens() != 2)
					throw new Exception("Unexpected id " + fileLine.getSequenceId());
				
				writer.write(fileLine.getSequenceId()  + "\t");
				sToken.nextToken();
				writer.write(sToken.nextToken() + "\t");
				writer.write( aNode.getTaxaName() + "\n" );
				numWritten++;
			
			}
		}
		
		writer.flush();  writer.close();
		System.out.println("Wrote " + numWritten + " for " + level);
		return outFile;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> nonChimericIDs = getNonChimericIDs();
		System.out.println(nonChimericIDs.size());
		List<NewRDPParserFileLine> rdpList= NewRDPParserFileLine.getRdpListSingleThread(
				ConfigReader.getSandraRiverJune2012Dir() 
					+ File.separator + "sandraThreePrimeTrimmedDirectByRDP2_6.txt");
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			File outFile = 
					writeALevel(NewRDPParserFileLine.TAXA_ARRAY[x], nonChimericIDs,  rdpList);
			
			HashMap<String, HashMap<String, Integer>> countMap = PivotOTUs.getMap(outFile.getAbsolutePath());
			PivotOTUs.writeResults(countMap, new File(ConfigReader.getSandraRiverJune2012Dir() 
					+ File.separator +
					NewRDPParserFileLine.TAXA_ARRAY[x] + "_directtaxaAsColumnsChimeraScreened.txt").getAbsolutePath());
		}
			
	}
}
