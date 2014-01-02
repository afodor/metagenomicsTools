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

package bottomUpTree.rdp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;

import scripts.pancreatitis.PivotOTUs;
import utils.ConfigReader;

public class WriteSequenceToSampleFileRDP
{
	public static float THRESHOLD_CUTOFF = 50;
	
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			File outFile = writeALevel(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			HashMap<String, HashMap<String, Integer>> countMap = PivotOTUs.getMap(outFile.getAbsolutePath());
			PivotOTUs.writeResults(countMap, new File(ConfigReader.getNinaWithDuplicatesDir() + File.separator + "rdp" + File.separator + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "_taxaAsColumns.txt").getAbsolutePath());
		}
			
	}
	
	public static File writeALevel(String level) throws Exception
	{
		System.out.println(level);
		File rdpDir = new File(ConfigReader.getNinaWithDuplicatesDir() + File.separator + "rdp" );
		
		File outFile = new File(rdpDir.getAbsolutePath() + File.separator + 
				"rdp_" + level + ".txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		writer.write("sequenceID\tsample\ttaxa\n");
		
		String[] names = rdpDir.list();
	
		for(String s : names)
			if( s.endsWith("rdpOut.txt"))
			{
				List<NewRDPParserFileLine> rdpList = 
				NewRDPParserFileLine.getRdpList(rdpDir.getAbsolutePath() + File.separator + s);
				
				String sampleId = RdpToJson.getKeyFromFilename(s);
				
				for( NewRDPParserFileLine fileLine : rdpList ) 
				{
					NewRDPNode node = fileLine.getTaxaMap().get(level);
					
					if( node != null && node.getScore() >= THRESHOLD_CUTOFF)
					{
						writer.write(fileLine.getSequenceId() + "\t");
						writer.write(sampleId + "\t");
						writer.write(node.getTaxaName() + "\n");
					}
				}
			}
		
		writer.flush();  writer.close();
		
		return outFile;
	}
}
