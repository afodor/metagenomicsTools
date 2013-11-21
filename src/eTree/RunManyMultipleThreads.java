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

package eTree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;
import dereplicate.DereplicateBySample;

public class RunManyMultipleThreads
{
	private static class TreeWorker implements Runnable
	{
		private final File inputFile;
		private final Semaphore semaphore;
		
		private TreeWorker(File inputFile, Semaphore semaphore)
		{
			this.inputFile = inputFile;
			this.semaphore = semaphore;
		}

		@Override
		public void run()
		{
			try
			{	
				ETree eTree = new ETree();
				System.out.println(inputFile.getAbsolutePath());
				FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(inputFile);
						
				for( FastaSequence fs = fsoat.getNextSequence(); 
						fs != null; 
							fs = fsoat.getNextSequence())
				{
					eTree.addSequence(fs.getSequence(), 
						ETree.getNumberOfDereplicatedSequences(fs), 
						inputFile.getName().replace(DereplicateBySample.DEREP_PREFIX, ""));
				}
				
				eTree.writeAsSerializedObject(ConfigReader.getETreeTestDir() + File.separator +  DereplicateBySample.DEREP_PREFIX + 
						".etree");
				semaphore.release();
			}
			catch(Exception ex)
			{
				System.out.println("Thread crash shutting down VM!");
				ex.printStackTrace();
				System.exit(1);
			}
			
		}
	}
	
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
		
		int numThreads = Runtime.getRuntime().availableProcessors();
		if( numThreads < 1)
			numThreads = 1;
		
		Semaphore semaphore = new Semaphore(numThreads);
		
		for(String s : fileNames)
			if( s.startsWith(DereplicateBySample.DEREP_PREFIX))
			{
				semaphore.acquire();
				TreeWorker worker = new TreeWorker(new File(dir.getAbsolutePath() + File.separator + s), semaphore);
				new Thread(worker).start();
			}
		
		int numToGet = numThreads;
		
		while( numToGet > 0)
		{
			semaphore.acquire();
			numToGet--;
		}
	}
}
