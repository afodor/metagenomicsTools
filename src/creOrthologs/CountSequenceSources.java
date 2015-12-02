package creOrthologs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class CountSequenceSources
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = AddMetadata.getBroadCategoryMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s));
		
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getCREOrthologsDir() + File.separator + 
							"contig_7000000220927531_forAlign.txt");
		
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		
		for(FastaSequence fs : list)
		{
			StringTokenizer sToken = new StringTokenizer(fs.getHeader());
			sToken.nextToken();
			String key = sToken.nextToken().replace(".scaffolds.fasta", "");
			
			if( ! map.containsKey(key))
				throw new Exception("No");
			
			Integer val = counts.get(map.get(key));
			
			if( val == null)
				val =0;
			
			val++;
			
			counts.put(map.get(key),val);
		}
		
		System.out.println(counts);
	}
}
