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
import java.io.FileWriter;

import parsers.FastaSequenceOneAtATime;
import probabilisticNW.ProbNW;
import probabilisticNW.ProbSequence;
import utils.ConfigReader;

public class ETree
{
	public static final double[] LEVELS = {0.0,  0.1, 0.07, 0.05, 0.04, 0.03};
	
	private final ENode topNode;
	
	public void addSequence(String sequence) throws Exception
	{
		ProbSequence probSeq = new ProbSequence(sequence);
		
		ENode index = addToOrCreateNode(topNode, probSeq);
		
		while( index != null)
			index = addToOrCreateNode(index, probSeq);
	}
	
	private int getIndex(double level) throws Exception
	{
		for( int i =0; i < LEVELS.length; i++)
			if( LEVELS[i] == level)
				return i;
		
		throw new Exception("Could not find " + level);
	}
	
	private ENode addToOrCreateNode( ENode parent , ProbSequence newSeq) throws Exception
	{
		if( parent.getDaughters().size() == 0 )
			return null;
		
		for( ENode node : parent.getDaughters() )
		{
			ProbSequence possibleAlignment= ProbNW.align(node.getProbSequence(), newSeq);
			//System.out.println( possibleAlignment.getSumDistance()  + "  " + node.getLevel()  );
			if( possibleAlignment.getSumDistance() <= node.getLevel())
			{
				node.setProbSequence(possibleAlignment);
				return node;
			}
		}
		
		// still here - no matches - add a new node
		ENode newNode = new ENode(newSeq, parent.getDaughters().get(0).getLevel(), parent);
		parent.getDaughters().add(newNode);
		
		int index = getIndex(newNode.getLevel());
		
		for( int x=index +1; x < LEVELS.length; x++)
		{
			ENode previousNode =newNode;
			newNode = new ENode(newSeq, LEVELS[x], previousNode);
			previousNode.getDaughters().add(newNode);
		}
		
		return null;
	}
	
	public ETree(String starterSequence)
	{
		this.topNode = new ENode(starterSequence, LEVELS[0], null);
		ENode lastNode = topNode;
		
		for( int x=1; x < LEVELS.length; x++)
		{
			ENode nextNode = new ENode(starterSequence, LEVELS[x], lastNode);
			lastNode.getDaughters().add(nextNode);
			lastNode = nextNode;
		}
	}
	
	public void writeAsXML(String xmlFilePath) throws Exception
	{
		writeAsXML(new File(xmlFilePath));
	}
	
	public void writeAsXML(File xmlFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(xmlFile));
		
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<phyloxml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.phyloxml.org http://www.phyloxml.org/1.10/phyloxml.xsd\" xmlns=\"http://www.phyloxml.org\">\n");
		writer.write("<phylogeny rooted=\"true\" rerootable=\"false\">\n");
		
		addNodeAndDaughtersToXML(this.topNode,writer,0);
		
		writer.write("</phylogeny>\n");
		writer.write("</phyloxml>\n");
		
		writer.flush();  writer.close();
	}
	
	private void addNodeAndDaughtersToXML( ENode node, BufferedWriter writer, int level ) throws Exception
	{
		String tabString = "";
		
		for( int x=0; x <= level; x++ )
			tabString += "\t";
		
		writer.write(tabString + "<clade>\n");
		
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
			
		writer.write(tabString + "\t<taxonomy>\n");
		
		// obviously, just a stub at this point
		writer.write(tabString + "\t<scientific_name>taxa " + node.getLevel() + " with " + node.getNumOfSequencesAtTip() + " sequences </scientific_name>\n");
		
		writer.write(tabString + "\t</taxonomy>\n");
		
		level++;
		
		for( ENode daughter: node.getDaughters() )
			addNodeAndDaughtersToXML(daughter, writer, level);
		
		writer.write(tabString + "</clade>\n");
	}
	
	public static void main(String[] args) throws Exception
	{
		FastaSequenceOneAtATime fsoat = 
				new FastaSequenceOneAtATime( ConfigReader.getETreeTestDir() + 
						File.separator + "postLucyFiltering.txt");
		
		ETree eTree = new ETree(fsoat.getNextSequence().getSequence());
		
		for( int x=0; x < 500; x++)
		{
			eTree.addSequence(fsoat.getNextSequence().getSequence());
			System.out.println("Adding " + x);
		}
			
		eTree.writeAsXML(ConfigReader.getETreeTestDir() + File.separator + 
				"testXML.xml");
	}
}
