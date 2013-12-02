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

package eTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import probabilisticNW.ProbNW;
import probabilisticNW.ProbSequence;
import utils.ConfigReader;
import utils.ProcessWrapper;

public class ETree implements Serializable
{
	private static final long serialVersionUID = 8463272194826212918L;
	public static final String ROOT_NAME = "root";
	
	public static final float[] LEVELS = {0.40f, 0.30f, 0.20f,0.15f,0.14f,0.13f,0.12f,0.11f,0.10f,0.09f,0.08f,0.07f,0.06f, 0.05f,0.04f,0.03f};
	public static final int RDP_THRESHOLD = 80;	
	
	private ENode topNode=null;
	
	public void validateTree() throws Exception
	{
		this.topNode.validateNodeAndDaughters();
	}
	
	public ENode getTopNode()
	{
		return topNode;
	}
	
	public int getTotalNumberOfSequences()
	{
		return this.topNode.getNumOfSequencesAtTips();
	}
	
	private static int getLeastCommonDistance(  ENode aNode, ENode anotherNode)
		throws Exception
	{
		if( aNode.getLevel() != anotherNode.getLevel())
			throw new Exception("Logic error");
		
		int x=0;
		
		while( ! aNode.getParent().getNodeName().equals(anotherNode.getParent().getNodeName()))
		{
			aNode = aNode.getParent();
			anotherNode = anotherNode.getParent();
			x++;
		}
		
		return x;
	}
	
	public void writePairedNodeInformation( String filepath ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filepath)));
		
		writer.write("nodeA\tnodeB\tparentA\tparentB\tnodeLevel\tsameParents\tleastCommonDistnace\talignmentDistance\talignmentDistanceMinusPredictedDistance\tnumSequencesA\tnumSequencesB\tmaxTreeSeqsA\tmaxTreeSeqsB\ttotalSeqs\n");
		
		List<ENode> list = getAllNodes();
		
		for( int x=0; x < list.size()-1; x++)
		{
			System.out.println("Writing outer node " + x + " Of " + list.size());
			ENode aNode = list.get(x);
			
			if( aNode.getParent() != null && ! aNode.getParent().getNodeName().equals(ROOT_NAME)) 
				for( int y=x+1; y <list.size(); y++)
				{
					ENode bNode = list.get(y);
					
					if( bNode.getParent() != null && ! bNode.getParent().getNodeName().equals(ROOT_NAME) &&
							aNode.getLevel() == bNode.getLevel()) 
						{
							writer.write( aNode.getNodeName() + "\t" );
							writer.write( bNode.getNodeName() + "\t" );
							writer.write( aNode.getParent().getNodeName() + "\t");
							writer.write( bNode.getParent().getNodeName() + "\t");
							writer.write( aNode.getLevel()+ "\t");
							writer.write(aNode.getParent().getNodeName().equals(bNode.getParent().getNodeName()) + "\t");
							writer.write( getLeastCommonDistance(aNode, bNode) + "\t");
							ProbSequence probSeq = ProbNW.align(aNode.getProbSequence(), bNode.getProbSequence());
							writer.write(probSeq.getAverageDistance() + "\t");
							writer.write((probSeq.getAverageDistance() - aNode.getLevel()) + "\t" );
							writer.write( aNode.getProbSequence().getNumRepresentedSequences() + "\t");
							writer.write( bNode.getProbSequence().getNumRepresentedSequences() + "\t");
							writer.write( aNode.getMaxNumberOfSeqsInBranch() + "\t");
							writer.write( bNode.getMaxNumberOfSeqsInBranch()+ "\t");
							
							int totalNum = aNode.getProbSequence().getNumRepresentedSequences() + 
									bNode.getProbSequence().getNumRepresentedSequences();
							writer.write(totalNum + "\n");
						}
				}
			writer.flush();
		}
		
		writer.flush();  writer.close();
	}
	
	public List<ENode> getAllNodes()
	{
		List<ENode> list = new ArrayList<ENode>();
		addNodeAndDaughters(this.topNode, list);
		return list;
	}
	
	public List<ENode> getAllNodesAtTips()
	{
		List<ENode> allNodes = getAllNodes();
		
		for( Iterator<ENode> i = allNodes.iterator(); i.hasNext();)
			if( i.next().getDaughters().size() >0)
				i.remove();
				
		return allNodes;
	}
	
	private void addNodeAndDaughters(ENode node, List<ENode> list)
	{
		list.add(node);
		
		for( ENode d : node.getDaughters())
			addNodeAndDaughters(d, list);
	}
	
	public static int getIndex(double level) throws Exception
	{
		for( int i =0; i < LEVELS.length; i++)
			if( LEVELS[i] == level)
				return i;
		
		throw new Exception("Could not find " + level);
	}
	
	
	

	public ETree()
	{
		
	}
	
	public void writeAsXML(String xmlFilePath) throws Exception
	{
		writeAsXML(new File(xmlFilePath));
	}
	
	public void writeAsText(String textFile, boolean detailed) throws Exception
	{
		writeAsText(new File(textFile), detailed);
	}
	
	public void writeAsText(File textFile, boolean detailed) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));
		
		this.topNode.writeNodeAndDaughters(writer,detailed);
		
		writer.flush();  writer.close();
	}
	
	public void writeNodesInTabularFormat( String outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("nodeName\tlevel\tparentName\tnumberSequences\tmaxNumSequences\talignmentScore\t"  +
		"alignmentScoreAverage\talignmentLength\tconsensusSequence\n");
		
		for( ENode node : getAllNodes() )
		{
			writer.write(node.getNodeName() + "\t");
			writer.write(node.getLevel() + "\t");
			writer.write((node.getParent() == null ? ROOT_NAME : node.getParent().getNodeName()) + "\t");
			writer.write( node.getNumOfSequencesAtTips() +"\t" );
			writer.write(node.getMaxNumberOfSeqsInBranch() + "\t");
			writer.write(node.getProbSequence().getAlignmentScore() + "\t");
			writer.write(node.getProbSequence().getAlignmentScoreAveragedByCol() + "\t");
			writer.write(node.getProbSequence().getColumns().size() + "\t");
			writer.write(node.getProbSequence().getConsensus() + "\n");
			
		}
		
		writer.flush();  writer.close();
	}
	
	public void writeAsXML(File xmlFile) throws Exception
	{

		HashMap<String, NewRDPParserFileLine> rdpMap =  tryForRDPMap();
			
		BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile));
		
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<phyloxml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.phyloxml.org http://www.phyloxml.org/1.10/phyloxml.xsd\" xmlns=\"http://www.phyloxml.org\">\n");
		writer.write("<phylogeny rooted=\"true\" rerootable=\"false\">\n");
		
		addNodeAndDaughtersToXML(this.topNode,writer,0, rdpMap);
		
		writer.write("</phylogeny>\n");
		writer.write("</phyloxml>\n");
		
		writer.flush();  writer.close();
	}
	
	public void writeUngappedConsensusSequences(String filePath) throws Exception
	{
		writeUngappedConsensusSequences(new File(filePath));
	}
	
	public void writeUngappedConsensusSequences(File outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		for( ENode node : topNode.getDaughters() )
			writeUngappedSequenceAndSubSequences(writer, node);
			
		writer.close(); writer.close();
	}
	
	
	private HashMap<String, NewRDPParserFileLine> tryForRDPMap()
	{
		try
		{
			File seqFile = new File( ConfigReader.getETreeTestDir() + File.separator + "consensusSequences.txt");
			
			if( seqFile.exists())
				seqFile.delete();
			
			if( seqFile.exists())
				throw new Exception("Could not delete " + seqFile.getAbsolutePath());
			
			File rdpFile = new File(ConfigReader.getETreeTestDir() + File.separator + "rdpSeqFile.txt");
			
			if( rdpFile.exists())
				rdpFile.delete();
			
			if( rdpFile.exists())
				throw new Exception("Could not delete " + rdpFile.getAbsolutePath());
			
			writeUngappedConsensusSequences(seqFile);
			
			String[] args = new String[7];
			
			args[0] = "java";
			args[1] = "-jar";
			args[2] = ConfigReader.getRDPJarPath();
			args[3] = "-q";
			args[4] = seqFile.getAbsolutePath();
			args[5] = "-o";
			args[6] = rdpFile.getAbsolutePath();
			
			new ProcessWrapper(args);
			
			return NewRDPParserFileLine.getAsMapFromSingleThread(rdpFile.getAbsolutePath());
		}
		catch(Exception ex)
		{
			System.out.println("Could not get RDP map");
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private void writeUngappedSequenceAndSubSequences( BufferedWriter writer, ENode node) throws Exception
	{
		writer.write(">" + node.getNodeName() + "\n");
		writer.write(node.getProbSequence().getConsensus().replaceAll("-", "") + "\n");
		
		for( ENode subNode : node.getDaughters() )
			writeUngappedSequenceAndSubSequences(writer, subNode);
	}
	
	private void addNodeAndDaughtersToXML( ENode node, BufferedWriter writer, int level, HashMap<String, NewRDPParserFileLine> rdpMap ) throws Exception
	{
		
		String tabString = "";
		
		for( int x=0; x <= level; x++ )
			tabString += "\t";
		
		String taxaName = "" + node.getLevel();
		String rank = null;
		String commonName = null;
		//String phylaName = null;
		
		if( rdpMap != null )
		{
			NewRDPParserFileLine line = rdpMap.get( node.getNodeName() );
			
			if( line != null)
			{
				NewRDPNode rdpNode = line.getLowestNodeAtThreshold(RDP_THRESHOLD);
				taxaName = rdpNode.getTaxaName() + " " + taxaName;
				rank = line.getLowestRankThreshold(RDP_THRESHOLD);
				commonName = tabString + "\t<common_name>" + line.getSummaryStringNoScore(50, 2)+"</common_name>\n";
				//NewRDPNode phylaNode = line.getTaxaMap().get(NewRDPParserFileLine.PHYLUM);
				
				//if( phylaNode != null)
				//	phylaName = tabString + "\t<accession>" + phylaNode.getTaxaName() + "</accession>\n";
			}
				
		}
		
		writer.write(tabString + "<clade>\n");
		
		//if( phylaName != null)
		//	writer.write(phylaName);
		writer.write( tabString + "\t<name>" + node.getNodeName() 
				+ "(" + node.getNumOfSequencesAtTips() + "seqs) level " + node.getLevel() +"</name>\n");
		
		if( level > 1 )
		{
			double branchLength = LEVELS[level-1] - LEVELS[level];
			writer.write(tabString + "\t<branch_length>" + branchLength +  "</branch_length>\n");
		}
		else
		{
			double branchLength = 0.01;
			writer.write(tabString + "\t<branch_length>" + branchLength +  "</branch_length>\n");
		}
		
		writer.write(tabString + "\t<taxonomy>");
		// obviously, just a stub at this point
		
		
		writer.write(tabString + "\t<scientific_name>" + taxaName + "(" + rank + ")" +"</scientific_name>\n");
		
		if( commonName != null)
			writer.write(commonName);
		
		if( rank != null)
			writer.write(tabString + "\t<rank>" + rank + "</rank>\n");
		
		writer.write(tabString + "\t</taxonomy>\n");
		
		level++;
		
		for( ENode daughter: node.getDaughters() )
			addNodeAndDaughtersToXML(daughter, writer, level, rdpMap);
		
		writer.write(tabString + "</clade>\n");
	}
		public void writeAsSerializedObject(String outFilePath) throws Exception
	{
		ObjectOutputStream out =new ObjectOutputStream( new GZIPOutputStream(
				new FileOutputStream(new File(outFilePath))));
		
		out.writeObject(this);
		
		out.flush(); out.close();
	}
	
	public static ETree readAsSerializedObject( String inFilePath) throws Exception
	{
		ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(inFilePath)));
		ETree etree = (ETree) in.readObject();
		in.close();
		return etree;
	}
}
