package scripts.laura.ierProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class QuickDemultiplex
{
	public static void main(String[] args) throws Exception
	{
		File inFile =new File(ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator + 
				"seqs.fna");
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(inFile);
		
		HashMap<String, Integer> sampleMap = new HashMap<>();
		
		int i =0;
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			String key = new StringTokenizer(fs.getFirstTokenOfHeader(),"_").nextToken();
			
			Integer count = sampleMap.get(key);
			
			if( count == null)
				count =0;
			
			count++;
			
			sampleMap.put(key, count);
			
			i++;
		
			if( i % 100000 == 0)
				System.out.println(i);
		}
		
		List<String> keys = new ArrayList<>(sampleMap.keySet());
		Collections.sort(keys);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLauraDir() + File.separator + "IER_Project" + File.separator + "sample.txt" )));
		
		for(String s : keys)
			writer.write(s + "\t" + sampleMap.get(s) + "\n");
		
		writer.flush();  writer.close();
		
		System.out.println("Finished");
	}
}
