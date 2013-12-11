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

public class RefilterInSingleThread
{
	public static void main(String[] args) throws Exception
	{
		File dir = new File(ConfigReader.getMockSeqDir() );
		
		List<String> fileNames = new ArrayList<String>();
		for( String s : dir.list())
			fileNames.add(s);
		
		Collections.shuffle(fileNames);
		
		int numDone =0;
		
		for(String s : fileNames)
			if( s.endsWith(".clust") /*&& numDone < 3*/)
			{
				System.out.println(s);
				List<ENode> clusters = new ArrayList<ENode>();
				
				List<ENode> fileCluster = 
						ReadCluster.readFromFile(dir.getAbsolutePath() + File.separator + s, false);
				
				numDone++;
				System.out.println("Starting " + numDone + " with " + fileCluster.size());;
				String sampleName = s.replace(".clust", "").replace(DereplicateBySample.DEREP_PREFIX, "") + "refilter_";
				ClusterAtLevel.clusterAtLevel(clusters,fileCluster, 
						RunOne.INITIAL_THRESHOLD, RunOne.EXCEED_THRESHOLD, sampleName, ClusterAtLevel.MODE.BOTTOM_LEVEL);
				System.out.println("Finished with " + clusters.size() );
				PivotOut.writeBinaryFile(ConfigReader.getMockSeqDir()+ File.separator + s + "_REFILTERED", clusters);
			}
	}
}
