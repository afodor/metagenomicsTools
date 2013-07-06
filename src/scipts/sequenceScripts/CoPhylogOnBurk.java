package scipts.sequenceScripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.FastQ;

import utils.ConfigReader;
import coPhylog.ContextCount;
import coPhylog.ContextHash;

public class CoPhylogOnBurk
{
	public static void main(String[] args) throws Exception
	{
		File file = new File(ConfigReader.getBurkholderiaDir() + File.separator + 
				"AS130-2_ATCACG_s_2_1_sequence.txt");
		
		HashMap<Long, ContextCount> map = new HashMap<>();
		
		int contextSize = 13;
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int numDone =0;
		
		for(FastQ fq = FastQ.readOneOrNull(reader); 
				fq != null; 
				fq = FastQ.readOneOrNull(reader))
		{
			ContextHash.addToHash(fq.getSequence(), map, contextSize);
			numDone++;
			
			if(numDone % 10000 == 0 )
				System.out.println(numDone + " " + map.size() + " " + (((float)map.size())/numDone) );
		}
		
		reader.close();
	}
}
