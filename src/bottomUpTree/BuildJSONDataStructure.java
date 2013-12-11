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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
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
			if( node.getNumOfSequencesAtTips()>=1)
			{
				node.setParent(rootNode);
				rootNode.getDaughters().add(node);
				numNodes++;
			}
		
		System.out.println("Proceeding with " + numNodes);
		
		rootNode.validateNodeAndDaughters(true);
		
		ETree etree = new ETree();
		etree.setTopNode(rootNode);
		
		File rdpFile = new File(ConfigReader.getETreeTestDir() + File.separator +  "mel04RDPFfile.txt");
		//etree.writeAsXML(ConfigReader.getETreeTestDir() + File.separator + "fullMel74PhyloXML.txt", rdpFile);
		HashMap<String, NewRDPParserFileLine> rdpMap = NewRDPParserFileLine.getAsMapFromSingleThread(rdpFile);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
			ConfigReader.getD3Dir() + File.separator + "aTree.json"	)));
		
		writeNodeAndChildren(writer, rootNode,500, rdpMap);
		 
		writer.flush();  writer.close();
		
	}
	
	private static void writeNodeAndChildren( BufferedWriter writer,
					ENode enode, int cutoff, HashMap<String, NewRDPParserFileLine> rdpMap) throws Exception
	{
		NewRDPParserFileLine fileLine = rdpMap.get(enode.getNodeName());
		
		String genusName = "none";
		NewRDPNode genus = null;
		
		if( fileLine != null)
			genus = fileLine.getTaxaMap().get(NewRDPParserFileLine.GENUS);
		
		if( genus != null)
			genusName = genus.getTaxaName();
		
		
		writer.write("{\n");
		writer.write("\"name\": \"" + genusName + "\",\n" );
		
		int level = 0;
		
		if( ! enode.getNodeName().equals(ETree.ROOT_NAME))
			level = ETree.getIndexForLevel(enode.getLevel()) + 1;
		
		writer.write("\"level\": " +  level + ",\n");
		
		
		//writer.write("\"genus\": " +  genusName+ ",\n");
		
		writer.write("\"size\": " +  enode.getNumOfSequencesAtTips() + "\n");
		
		List<ENode> daughters =new ArrayList<ENode>();
		
		for( ENode d : enode.getDaughters() )
			if( d.getNumOfSequencesAtTips() >= cutoff)
				daughters.add(d);
		
		if( daughters.size() >0 )
		{
			writer.write(",\"children\": [\n");
			
			for( Iterator<ENode> i = daughters.iterator(); i.hasNext();)
			{
				writeNodeAndChildren(writer,i.next(), cutoff, rdpMap);
				if( i.hasNext())
					writer.write(",");
				
			}
				
			writer.write("]\n");
		}
		
		
		
		
		writer.write("}\n");
	}
}
