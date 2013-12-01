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

import probabilisticNW.ProbSequence;
import utils.ConfigReader;
import dereplicate.DereplicateBySample;

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
		
		List<ProbSequence> finalClusterList = null;
		for(String s : fileNames)
			if( s.startsWith(DereplicateBySample.DEREP_PREFIX))
			{
				String sampleName = s.replace(DereplicateBySample.DEREP_PREFIX, "");
				File file = new File(dir.getAbsolutePath() + File.separator + s);
				List<ProbSequence> initialCluster = ClusterAtLevel. getInitialSequencesFromFasta(file.getAbsolutePath(), sampleName,0.03f, 0.06f);
				
				if( finalClusterList == null)
				{
					finalClusterList = initialCluster;
				}
				else
				{
					finalClusterList.addAll(initialCluster);
					finalClusterList = ClusterAtLevel.clusterAtLevel(finalClusterList, 0.03f, 0.06f);
					System.out.println("Finished with " + finalClusterList);
				}
			}
	}

}
