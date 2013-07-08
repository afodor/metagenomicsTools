package scripts.sequenceScripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;

import utils.ConfigReader;
import coPhylog.ContextCount;
import coPhylog.ContextHash;

public class CoPhylogOnBurk
{
	public static void main(String[] args) throws Exception
	{
		File sequenceDir = new File(ConfigReader.getBurkholderiaDir());
		
		String[] files = sequenceDir.list();
		
		for(String s : files)
		{
			if( s.endsWith("gz"))
			{
				File outFile = new File(sequenceDir + File.separator + "results" + File.separator + 
						s + "_CO_Phylog.txt");
				
				runAFile(new File(sequenceDir.getAbsolutePath() + File.separator + s), outFile);
			}
		}
			
	}
	
	public static void runAFile(File inFile, File outFile) throws Exception
	{	
		System.out.println("Starting " + inFile.getPath());
		HashMap<Long, ContextCount> map = new HashMap<>();
		
		int contextSize = 13;
		
		BufferedReader reader = 
				inFile.getName().toLowerCase().endsWith("gz") ? 
						new BufferedReader(new InputStreamReader( 
								new GZIPInputStream( new FileInputStream( inFile)))) :  
						new BufferedReader(new FileReader(inFile)) ;
		
		int numDone =0;
		int numRemoved =0;
		
		for(FastQ fq = FastQ.readOneOrNull(reader); 
				fq != null && numDone < 2000000; 
				fq = FastQ.readOneOrNull(reader))
		{
			ContextHash.addToHash(fq.getSequence(), map, contextSize);
			numDone++;
			
			if(numDone % 10000 == 0 )
			{
				System.gc();
				System.out.println( numRemoved + " " +  numDone + " " + map.size() + " " + (((float)map.size())/numDone) + " "+ 
				Runtime.getRuntime().freeMemory() + " " + Runtime.getRuntime().totalMemory() +  " " + Runtime.getRuntime().maxMemory() 
				+ " " + ((double)Runtime.getRuntime().freeMemory())/Runtime.getRuntime().maxMemory() );
				
				
				double fractionFree= 1- (Runtime.getRuntime().totalMemory()- ((double)Runtime.getRuntime().freeMemory() ))
								/Runtime.getRuntime().totalMemory();
				
				System.out.println("fraction Free= " + fractionFree);
				
				double fractionAllocated = 1-  (Runtime.getRuntime().maxMemory()- ((double)Runtime.getRuntime().totalMemory() ))
						/Runtime.getRuntime().maxMemory();
				
				System.out.println("fraction allocated = " + fractionAllocated);
				
				if( fractionFree <= 0.10 && fractionAllocated >= 0.90 )
					removeSingletons(map);
				
				System.out.println("\n\n");
			}
				
		}
		
		reader.close();
		
		System.out.println("Finished reading with " + map.size() + " having removed " + numRemoved + " singletons ");
		
		System.out.println("Removing singletons");
		
		
		
		System.out.println("Removed singletons " + map.size() );
		System.out.println("Writing text file");
		
		removeSingletons(map);
		
		writeTextFile(outFile, map);
		
		System.out.println("Finished " + inFile.getAbsolutePath());
	}
	
	private static int removeSingletons(HashMap<Long, ContextCount> map) 
	{
		System.out.println("Removing singletons");
		int num =0;
		
		for( Iterator<Long> i = map.keySet().iterator(); i.hasNext(); )
		{
			if( map.get(i.next()).isSingleton() )
			{
				i.remove();
				num++;
			}
				
		}
		
		return num;
	}
	
	private static void writeTextFile( File outFile, HashMap<Long, ContextCount> map ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("bits\tnumA\tnumC\tnumG\tnumT\n");
		
		for( Long l : map.keySet() )
		{
			writer.write(l + "\t");
			
			ContextCount cc = map.get(l);
			
			writer.write(cc.getNumA() + "\t");
			writer.write(cc.getNumC() + "\t");
			writer.write(cc.getNumG() + "\t");
			writer.write(cc.getNumT() + "\n");
		}
		
		writer.flush();  writer.close();
	}
}
