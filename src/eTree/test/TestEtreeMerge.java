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

public class TestEtreeMerge
{
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
		

		etree1.writeAsText(ConfigReader.getETreeTestDir() + File.separator + 
				File.separator + "tree1preCall.txt", false);
		
		etree1.attemptRerunOfErrorsAtTips();
		System.out.println("Validating with " + etree1.getAllNodes().size());
		
		etree1.writeAsText(ConfigReader.getETreeTestDir() + File.separator + 
				File.separator + "tree1Bugged.txt", false);
		
		etree1.validateTree();
		
		etree1.writePairedNodeInformation(ConfigReader.getETreeTestDir() + File.separator+ 
				"tree1Pairs.txt");
		
		/*
		
		ETree etree2 = 
				ETree.getEtreeFromFasta(ConfigReader.getETreeTestDir() + File.separator + 
						"gastro454DataSet" + File.separator + "DEREP_SAMP_PREFIX3B2", "3B2");

		etree2.mergeAllDaughters();
		etree2.validateTree();
		
		etree1.addOtherTree(etree2);
		etree1.mergeAllDaughters();
		etree1.validateTree();
		
		PivotToSpreadheet.pivotToSpreasheet(0.03, etree1, ConfigReader.getETreeTestDir() + File.separator + "twoTrees_003_AsText.txt");
		etree1.writeAsText(ConfigReader.getETreeTestDir() + File.separator + "twoTreesAsText.txt",false);
		etree1.writePairedNodeInformation(ConfigReader.getETreeTestDir() + File.separator + 
				"mergedPairedTree.txt");
		
		/*
		etree1.writeAsSerializedObject(ConfigReader.getETreeTestDir() + File.separator + 
				"gastro454DataSet" + File.separator + "merged.etree");
		
		
		etree1.writeAsXML(ConfigReader.getETreeTestDir() + File.separator + 
				"gastro454DataSet" + File.separator + "mergedXML.xml");
				*/
		
	}
}
