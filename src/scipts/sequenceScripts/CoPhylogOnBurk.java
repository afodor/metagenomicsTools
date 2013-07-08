package scipts.sequenceScripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

import parsers.FastQ;

import utils.ConfigReader;
import coPhylog.ContextCount;
import coPhylog.ContextHash;

public class CoPhylogOnBurk
{
	public static void main(String[] args) throws Exception
	{
		File file = new File(ConfigReader.getBurkholderiaDir() + File.separator + 
				"AS_130_TTAGGC_s_2_1_sequence.txt");
		
		HashMap<Long, ContextCount> map = new HashMap<>();
		
		int contextSize = 13;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int numDone =0;
		
		for(FastQ fq = FastQ.readOneOrNull(reader); 
				fq != null && numDone < 2000000; 
				fq = FastQ.readOneOrNull(reader))
		{
			ContextHash.addToHash(fq.getSequence(), map, contextSize);
			numDone++;
			
			if(numDone % 10000 == 0 )
				System.out.println(numDone + " " + map.size() + " " + (((float)map.size())/numDone) );
		}
		
		reader.close();
		
		System.out.println("Finished reading with " + map.size());
		
		System.out.println("Removing singletons");
		
		for( Iterator<Long> i = map.keySet().iterator(); i.hasNext(); )
		{
			if( map.get(i.next()).isSingleton() )
				i.remove();
		}
		
		System.out.println("Removed singletons " + map.size() );
		System.out.println("Writing text file");
		
		File outFile = new File(ConfigReader.getBurkholderiaDir() + File.separator + "AS_130_TTAGGC_s_2_1_textOut.txt");
		
		writeTextFile(outFile, map);
		
		System.out.println("Finished");
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
