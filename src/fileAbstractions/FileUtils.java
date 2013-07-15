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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class FileUtils
{
	public static String getCommonNameForPairs( PairedReads pr)
		throws Exception
	{
		String s1 = getShortName(pr.getFirstFileName().getName());
		String s2 = getShortName(pr.getSecondFileName().getName());
	
		if( ! s1.equals(s2))
				throw new Exception("Unequal file names");
			
		return s1;
	}
	
	private static String getShortName(String s)
	{
		if( s.startsWith("AS130-2"))
			return "AS_130_2";
		
		StringTokenizer sToken = new StringTokenizer(s, "_");
		
		return sToken.nextToken() + "_" + sToken.nextToken();
	}
	
	public static File getCountsFile(File inFile) throws Exception
	{
		return new File( inFile.getParentFile() + File.separator + "results" + File.separator + 
								inFile.getName() + "_CO_PhylogBin.gz");
	}
	
	public static File getSNPResultsFile( File file1, File file2 ) throws Exception
	{
		List<String> names = new ArrayList<>();
		names.add(file1.getName());
		names.add(file2.getName());
		Collections.sort(names);
		
		return new File( file1.getParentFile() + File.separator + "snpProbs" + File.separator + 
				names.get(0) + "vs" + names.get(1)+ ".txt");
	}
}
