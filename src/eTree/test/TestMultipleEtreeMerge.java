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


package eTree.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eTree.ETree;

import utils.ConfigReader;

public class TestMultipleEtreeMerge
{
	public static void main(String[] args) throws Exception
	{
		// this has 74 etrees created by calling eTree.RunManyMultipleThreads
		File startDir =new File(ConfigReader.getETreeTestDir() + File.separator + "gastro454DataSet" + File.separator + 
				"etrees" + File.separator );
		
		List<File> files = new ArrayList<File>();
		
		for( String s : startDir.list() )
			if(s.endsWith("etree"))
				files.add(new File(startDir.getAbsolutePath() + File.separator + s));
		
		ETree firstTree = ETree.readAsSerializedObject(files.get(0).getAbsolutePath());
		
		for( int x=1; x < files.size(); x++)
		{
			System.out.println("Reading " + files.get(x).getAbsolutePath());
			ETree otherTree = ETree.readAsSerializedObject(files.get(x).getAbsolutePath());
			System.out.println("Merging ");
			firstTree.addOtherTree(otherTree);
		}
	
		System.out.println("Writing final tree");
		firstTree.writeAsSerializedObject(ConfigReader.getETreeTestDir() + File.separator + "melmergedFromParallel.etree");
		firstTree.writeAsXML(ConfigReader.getETreeTestDir() + File.separator + "melmergedFromParallelXML.xml");
	}
}
