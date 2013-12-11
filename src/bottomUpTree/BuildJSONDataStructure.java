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

package bottomUpTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import probabilisticNW.ProbSequence;

import utils.ConfigReader;

import eTree.ENode;
import eTree.ETree;

public class BuildJSONDataStructure
{
	public static void main(String[] args) throws Exception
	{
		List<ENode> list= ReadCluster.readFromFile(
				ConfigReader.getETreeTestDir() + File.separator + "bottomUpMelMerged0.04.merged",true, false);
		
		ENode rootNode = new ENode(new ProbSequence("ACGT", "root"), ETree.ROOT_NAME, 0, null);
		
		int numNodes=0;
		for( ENode node : list)
			if( node.getNumOfSequencesAtTips()>=500)
			{
				node.setParent(rootNode);
				rootNode.getDaughters().add(node);
				numNodes++;
			}
		
		System.out.println("Proceeding with " + numNodes);
		
		rootNode.validateNodeAndDaughters(true);
			
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
			ConfigReader.getD3Dir() + File.separator + "aTree.json"	)));
		
		writeNodeAndChildren(writer, rootNode);
		
		writer.flush();  writer.close();
	}
	
	private static void writeNodeAndChildren( BufferedWriter writer,
					ENode enode) throws Exception
	{
		writer.write("{\n");
		writer.write("\"name\": \"" + enode.getNodeName() + "\",\n" );
		writer.write("\"size\": " +  enode.getNumOfSequencesAtTips() + "\n");
		
		if( enode.getDaughters().size() >0 )
		{
			writer.write(",\"children\": [\n");
			
			for( Iterator<ENode> i = enode.getDaughters().iterator(); i.hasNext();)
			{
				writeNodeAndChildren(writer,i.next());
				if( i.hasNext())
					writer.write(",");
				
			}
				
			writer.write("]\n");
		}
		
		
		
		
		writer.write("}\n");
	}
}
