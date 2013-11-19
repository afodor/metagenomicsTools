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
import utils.ConfigReader;

public class ETree
{
	public static final float[] LEVELS = { 0.1f, 0.07f, 0.04f, 0.03f, 0.02f, 0.01f };
	
	private final ENode topNode;
	
	public void addSequence(String sequence)
	{
		
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
		
		addNodeAndDaughters(this.topNode,writer,0);
		
		writer.write("</phylogeny>\n");
		writer.write("</phyloxml>\n");
		
		writer.flush();  writer.close();
	}
	
	private void addNodeAndDaughters( ENode node, BufferedWriter writer, int level ) throws Exception
	{
		String tabString = "";
		
		for( int x=0; x <= level; x++ )
			tabString += "\t";
		
		writer.write(tabString + "<clade>\n");
		
		if( level > 0 )
		{
			float branchLength = LEVELS[level] - LEVELS[level-1];
			writer.write(tabString + "\t<branch_length>" + branchLength +  "</branch_length>\n");
		}
			
		writer.write(tabString + "\t<taxonomy>\n");
		
		// obviously, just a stub at this point
		writer.write(tabString + "\t<scientific_name>taxa" + level + "</scientific_name>\n");
		
		writer.write(tabString + "\t</taxonomy>\n");
		
		level++;
		
		for( ENode daughter: node.getDaughters() )
			addNodeAndDaughters(daughter, writer, level);
		
		writer.write(tabString + "</clade>\n");
	}
	
	public static void main(String[] args) throws Exception
	{
		FastaSequenceOneAtATime fsoat = 
				new FastaSequenceOneAtATime( ConfigReader.getETreeTestDir() + 
						File.separator + "postLucyFiltering.txt");
		
		ETree eTree = new ETree(fsoat.getNextSequence().getSequence());
		
		eTree.writeAsXML(ConfigReader.getETreeTestDir() + File.separator + 
				"testXML.xml");
	}
}
