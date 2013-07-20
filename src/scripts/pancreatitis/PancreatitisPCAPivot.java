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


package scripts.pancreatitis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import parsers.OtuWrapper;
import pca.PCA;
import utils.ConfigReader;

public class PancreatitisPCAPivot
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(new File( 
				ConfigReader.getPancreatitisDir() + File.separator + 
		"erinHannaHuman_raw_phyForR.txt"), new HashSet<String>(), new HashSet<String>(),10);
		
		
		double[][] d=  wrapper.getUnnorlalizedAsArray();
		
		List<String> keys = new ArrayList<String>();
		for( String s : wrapper.getSampleNames() )
		{
			keys.add(s.replace("human", ""));
		}
		
		List<String> catHeaders = new ArrayList<String>();
		List<List<String>> categories = new ArrayList<List<String>>();
		File outFile = 
			new File(ConfigReader.getPancreatitisDir() + File.separator
					+ "PCA_PIVOT.txt");
		System.out.println("Writing " + outFile.getAbsolutePath());
		PCA.writePCAFile(keys, catHeaders, categories,
				d, outFile
				);
	}
}
