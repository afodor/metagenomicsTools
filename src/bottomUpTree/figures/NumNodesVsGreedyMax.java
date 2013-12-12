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


package bottomUpTree.figures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import probabilisticNW.ProbSequence;
import utils.ConfigReader;
import bottomUpTree.ReadCluster;

import eTree.ENode;
import eTree.ETree;

public class NumNodesVsGreedyMax
{
	public static void main(String[] args) throws Exception
	{
		List<ENode> inList= ReadCluster.readFromFile(
				ConfigReader.getETreeTestDir() + File.separator + "bottomUpMelMerged0.2.merged",false, false);
		
		ENode rootNode = new ENode(new ProbSequence("ACGT", "root"), ETree.ROOT_NAME, 0, null) ;
		
		double totalSeqs = 0;
		System.out.println("Got " + totalSeqs);
		for( ENode node : inList)
		{
			node.setParent(rootNode);
			rootNode.getDaughters().add(node);
			rootNode.getProbSequence().setMapCount(rootNode.getProbSequence(), node.getProbSequence());
			totalSeqs+= node.getNumOfSequencesAtTips();
		}
		
		BufferedWriter writer = new BufferedWriter( new FileWriter( new File(
				ConfigReader.getETreeTestDir() + File.separator + 
				"numNodesVsThreshold.txt")));
		writer.write("d\tnumSeqs\tfractionSeqs\tnumNodes\n");
		for( double d = 0.99; d >=0.00; d = d - 0.01)
		{
			System.out.println(d);
			List<ENode> outList = new ArrayList<ENode>();
			
			for( ENode eNode : rootNode.getDaughters())
				addNodeAndChildren(eNode, d, outList);
			
			int numSeqs =0;
			
			for( ENode node : outList)
				if( node.getDaughters().size() == 0 )
					numSeqs += node.getNumOfSequencesAtTips();
			
			writer.write(d + "\t" + numSeqs + "\t" + (numSeqs/totalSeqs) + "\t" + outList.size() + "\n");
			writer.flush();
			
		}
		
		writer.flush();  writer.close();
	}
	
	private static void addNodeAndChildren(
			ENode enode, double cutoff, List<ENode> list) throws Exception
			{
				list.add(enode);
		
				double numSequencesAccountedForByOneBranch = 
						((double) enode.getGreedyMax()) / enode.getNumOfSequencesAtTips();
				System.out.println(enode.getGreedyMax() + " " +  enode.getNumOfSequencesAtTips());

				if ( numSequencesAccountedForByOneBranch  <= cutoff) 
				{
					for( Iterator<ENode> i = enode.getDaughters().iterator(); i.hasNext();)
					{
						addNodeAndChildren(i.next(), cutoff, list);
					}
				}

			}
}
