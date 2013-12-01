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
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import probabilisticNW.ProbSequence;
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
		
		int numDone =1;
		List<ProbSequence> finalClusterList = null;
		for(String s : fileNames)
			if( s.endsWith(".clust") /*&& numDone < 3*/)
			{
				List<ProbSequence> fileCluster = 
						ReadCluster.readFromFile(dir.getAbsolutePath() + File.separator + s, false);
				
				if( finalClusterList == null)
				{
					finalClusterList = fileCluster;
				}
				else
				{
					numDone++;
					System.out.println("Starting " + numDone);
					finalClusterList.addAll(fileCluster);
					finalClusterList = ClusterAtLevel.clusterAtLevel(finalClusterList, RunOne.INITIAL_THRESHOLD, RunOne.EXCEED_THRESHOLD);
					System.out.println("Finished with " + finalClusterList.size() );
				}
			}
		
		ObjectOutputStream out =new ObjectOutputStream( new GZIPOutputStream(
				new FileOutputStream(new File(ConfigReader.getETreeTestDir() + File.separator + "Merged74At03.merged"))));
		
		out.writeObject(finalClusterList);
		
		out.flush(); out.close();
	}

}
