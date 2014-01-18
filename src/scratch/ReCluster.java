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


package scratch;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class ReCluster
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getNinaWithDuplicatesDir() + File.separator + 
				"ninaOtusPivotedTaxaAsColumns.txt");
		
		int numFound =0;
		
		for( String s : wrapper.getSampleNames())
		{
			File aFile = new File(ConfigReader.getNinaWithDuplicatesDir() + File.separator + "OriginalFiles" + File.separator +
					s + ".gz");
			
			File destinationFile = new File(ConfigReader.getNinaWithDuplicatesDir() + File.separator + "OriginalFiles" + File.separator +
				"ToReCluster" + File.separator + 	s + ".gz");
			
			if( aFile.exists())
				numFound++;
			
			System.out.println("cp " + aFile.getAbsolutePath() + " " + destinationFile.getAbsolutePath());
		}
		
		System.out.println(numFound);
			
	}
}
