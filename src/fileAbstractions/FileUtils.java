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


package fileAbstractions;

import java.io.File;

public class FileUtils
{
	public static File getCountsFile(File inFile) throws Exception
	{
		return new File( inFile.getParentFile() + File.separator + "results" + File.separator + 
								inFile.getName() + "_CO_PhylogBin.gz");
	}
	
	public static File getSNPResultsFile( File file1, File file2 ) throws Exception
	{
		return new File( file1.getParentFile() + File.separator + "snpProbs" + File.separator + 
				file1.getName() + "vs" + file2.getName() + ".txt");
	}
}
