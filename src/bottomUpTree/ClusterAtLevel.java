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
import java.util.HashSet;
import java.util.List;

import kmerDatabase.KmerDatabase;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import probabilisticNW.KmerDatabaseForProbSeq;
import probabilisticNW.ProbSequence;

import utils.ConfigReader;

public class ClusterAtLevel
{
	public static final float CLUSTER_LEVEL = 0.03f;
	public static final float CUTOFF_LEVEL = 0.06f;
	
	/*
	 * As a side effect, all seqs are removed from seqsToCluster
	 */
	public static List<ProbSequence> clusterAtLevel( List<ProbSequence> seqstoCluster, 
								float levelToCluster, float stopSearchThreshold,
								KmerDatabase kmerDatabase)
	{
		
		List<ProbSequence> clusters = new ArrayList<ProbSequence>();
		
		while( ! seqstoCluster.isEmpty())
		{
			ProbSequence seedSeq = seqstoCluster.get(0);
			List<ProbSequence> unclusteredSeqs = new ArrayList<ProbSequence>();
			
			for( int x=1; x < seqstoCluster.size(); x++)
			{
				
			}
			
			//System.out.println(" have " + clusters.size() + " with " + seqs.size() + " remaining ");
			seqstoCluster= unclusteredSeqs;
			
		}
		
		return clusters;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<ProbSequence> probSeqs = new ArrayList<ProbSequence>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(		
				ConfigReader.getETreeTestDir() + File.separator + 
		"gastro454DataSet" + File.separator + "DEREP_SAMP_PREFIX3B1");
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
			if( fs.isOnlyACGT())
			{
				probSeqs.add(new ProbSequence(fs.getSequence(), "3B1"));
			}
		
		KmerDatabaseForProbSeq db = KmerDatabaseForProbSeq.buildDatabase(probSeqs);
		
		System.out.println(db.queryDatabase(probSeqs.get(0).getConsensus()));
	}
	
	
}
