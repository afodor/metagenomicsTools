

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

package utils;

import java.io.File;

import sequenceRuns.RunDescription;

public class FilePathGenerator
{
	/*
	 * Static methods only
	 */
	private FilePathGenerator()
	{
		
	}
	
	public static File getPfamProteinAssignmentFamily(RunDescription rd) 
		throws Exception
	{
		File file = getAndCreateResultsDir(rd);
		return new File(file.getAbsolutePath() + File.separator + 
				rd.getQueryDescription() + "_ToPFamFamilies");
		
	}
	
	public static File getPfamCountsFile(RunDescription rd) 
		throws Exception
	{
		File file = getAndCreateResultsDir(rd);
		return new File(file.getAbsolutePath() + File.separator + 
				rd.getQueryDescription() + "_PfamCounts");
	}
	
	/*
	 * Attempts to create the directory if it does not already exist
	 */
	public static File getAndCreateResultsDir( RunDescription rd ) throws Exception
	{
		File file = new File(rd.getTopResultsDir() + 
				File.separator + rd.getQueryDescription() + "_TO_" + rd.getTargetDescription());
		
		if( ! file.exists())
			file.mkdir();
		
		return file;
	}
	
	public static File getAndCreateQueryIdForTaxSubDirectory(RunDescription rd) throws Exception
	{
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
				 File.separator + "QueryIdsForTax");
	}
	
	public static File getCountVsScoreFile( RunDescription rd) throws Exception
	{
		
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
				 File.separator + File.separator + rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_TOP_HITSCountVsScore.txt" );
	}
	
	public static File getCountVsLengthFile( RunDescription rd) throws Exception
	{
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
				 File.separator + File.separator + rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_TOP_HITSCountVsLength.txt" );
	}
	
	public static File getQueryLengthDistributionFile( RunDescription rd )
		throws Exception
	{
		return new File( rd.getTopResultsDir() + File.separator + 
				rd.getQueryDescription() + "SequenceLengths.txt");
	}
	
	public static File getTopHitsFile( RunDescription rd ) throws Exception
	{
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
			  File.separator + rd.getQueryDescription() 
			 + "_TO_" + rd.getTargetDescription() + "_TOP_HITS.txt" );
	}
	

	public static File getTopHitsFileWithExtendedHeader( RunDescription rd ) throws Exception
	{
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
			  File.separator + rd.getQueryDescription() 
			 + "_TO_" + rd.getTargetDescription() + "_TOP_HITS_ExtendedHeader.txt" );
	}
	
	public static File getTopHitsFileWithExtendedHeaderAndRibotypeAssignments( RunDescription rd ) throws Exception
	{
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
			  File.separator + rd.getQueryDescription() 
			 + "_TO_" + rd.getTargetDescription() + "_TOP_HITS_ExtendedHeaderWithRibotypes.txt" );
	}
	
	public static File getTaxCategoryFile(RunDescription rd, char c) throws Exception
	{
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
				 File.separator + File.separator + rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_TaxCategory_" + c  +".txt" );
	}
	
	public static File getTaxAssignmentFile(RunDescription rd) throws Exception
	{
		return new File( getAndCreateResultsDir(rd).getAbsolutePath() + 
				 File.separator + File.separator + rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_TaxAssignment.txt" );
	}
	
	public static File getGeneCountFile(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_GeneCounts.txt" );
	}
	
	public static File getNodeGeneCountFile(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		File returnDir = 
			new File(baseDir + File.separator +rd.getQueryDescription() 
					 + "_TO_" + rd.getTargetDescription() + "_GeneCountsNodes.txt" );
		
		if( returnDir.exists())
			returnDir.mkdir();
		
		return returnDir;
	}
	
	public static File getQuerySequenceSubsetFile(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_QuerySequenceSubset.txt" );
	}
	
	public static File getNumOver80File(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_NumOver80.txt" );
	}
	
	public static File getTaxaTableFromQuerySeqs(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_TaxaTableFromQuerySeqs.txt" );
	}
	
	public static File getNumberNovelSequencesVsEScore(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_NumNovelSequencesVsEScore.txt" );
	}
	
	public static File getPublicationsFile(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_Publications.txt" );
	}
	
	public static File getPhylumCount(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_PhylumCount.txt" );
	}
	
	public static File get16SPhylumCount(RunDescription rd) throws Exception
	{
		File baseDir = getAndCreateResultsDir(rd);
		
		return new File(baseDir + File.separator +rd.getQueryDescription() 
				 + "_TO_" + rd.getTargetDescription() + "_16SPhylumCount.txt" );
	}
	
	
}
