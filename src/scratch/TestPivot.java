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


package scratch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.StringTokenizer;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;

public class TestPivot
{
	public static void main(String[] args) throws Exception
	{
		List<NewRDPParserFileLine> rdpList = 
				NewRDPParserFileLine.getRdpListSingleThread("C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\rdpOutFromLength200");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\classes\\undergradProgramming_2013\\fattyLiverMaterials\\rdpGenusThreeCol.txt")));
		
		writer.write("sequence\tsample\tgenus\n");
		
		for( NewRDPParserFileLine rdp : rdpList)
		{
			NewRDPNode node = rdp.getTaxaMap().get(NewRDPParserFileLine.GENUS);
			
			if( node != null && node.getScore() >=80)
			{
				writer.write(rdp.getSequenceId() + "\t");
				
				StringTokenizer sToken = new StringTokenizer(rdp.getSequenceId(), "_");
				sToken.nextToken();
				writer.write(sToken.nextToken() +"\t");
				writer.write(node.getTaxaName() + "\n");
			}
		}
		
		writer.flush();  writer.close();
		
	}
}
