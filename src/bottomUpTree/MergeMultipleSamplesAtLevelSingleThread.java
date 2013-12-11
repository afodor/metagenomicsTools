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


package bottomUpTree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dereplicate.DereplicateBySample;
import eTree.ENode;

import utils.ConfigReader;

public class MergeMultipleSamplesAtLevelSingleThread
{
	/*
	 * First run dereplicate.DereplicateBySample
	 */
	public static void main(String[] args) throws Exception
	{
		File dir = new File(ConfigReader.getETreeTestDir() + File.separator + 
				"gastro454DataSet" + File.separator );
		
		List<String> fileNames = new ArrayList<String>();
		for( String s : dir.list())
			fileNames.add(s);
		
		Collections.shuffle(fileNames);
		
		int numDone =0;
		List<ENode> clusters = new ArrayList<ENode>();
		
		for(String s : fileNames)
			if( s.endsWith(".clust") /*&& numDone < 3*/)
			{
				List<ENode> fileCluster = 
						ReadCluster.readFromFile(dir.getAbsolutePath() + File.separator + s, false,false);
				
				numDone++;
				System.out.println("Starting " + numDone);;
				ClusterAtLevel.clusterAtLevel(clusters,fileCluster, RunOne.INITIAL_THRESHOLD, RunOne.EXCEED_THRESHOLD,
						s.replace(".clust", "").replace(DereplicateBySample.DEREP_PREFIX,"") + "merge",
						ClusterAtLevel.MODE.BOTTOM_LEVEL);
				System.out.println("Finished with " + clusters.size() );
			}
		
		PivotOut.writeBinaryFile(ConfigReader.getETreeTestDir() + File.separator + "Merged74At03.merged", clusters);
		
	}

}
