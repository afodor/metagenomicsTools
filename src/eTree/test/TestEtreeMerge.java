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

package eTree.test;

import java.io.File;

import utils.ConfigReader;
import eTree.ETree;
import eTree.PivotToSpreadheet;

public class TestEtreeMerge
{
	private static void writeTreeFiles( ETree etree, String prefix ) throws Exception
	{

		etree.writeNodesInTabularFormat(ConfigReader.getETreeTestDir() + File.separator + 
				File.separator + prefix +  "treeTabbed.txt");
		
		etree.writeAsText(ConfigReader.getETreeTestDir() + File.separator + 
				File.separator +  prefix + "treeAsTxt.txt", true);
		
		etree.writePairedNodeInformation(ConfigReader.getETreeTestDir() + File.separator+ 
				prefix +  "treesPaired.txt");
	}
	
	/*
	 * An example of reading and merging two trees
	 */
	public static void main(String[] args) throws Exception
	{
		ETree etree1 = 
				ETree.getEtreeFromFasta(ConfigReader.getETreeTestDir() + File.separator + 
						"gastro454DataSet" + File.separator + "DEREP_SAMP_PREFIX3B1", "3B1");
		
		etree1.validateTree();
		etree1.mergeAllDaughters();
		etree1.validateTree();
		writeTreeFiles(etree1, "tree1Pre");
		
		ETree etree2 = 
				ETree.getEtreeFromFasta(ConfigReader.getETreeTestDir() + File.separator + 
						"gastro454DataSet" + File.separator + "DEREP_SAMP_PREFIX3B2", "3B2");

		etree2.mergeAllDaughters();
		etree2.validateTree();
		writeTreeFiles(etree2, "tree2Pre");
		
		etree1.addOtherTree(etree2);
		etree1.mergeAllDaughters();
		etree1.validateTree();
		
		PivotToSpreadheet.pivotToSpreasheet(0.03, etree1, ConfigReader.getETreeTestDir() + File.separator + "twoTrees_003_AsText.txt");
		writeTreeFiles(etree1, "mergedTree");
	}
}
