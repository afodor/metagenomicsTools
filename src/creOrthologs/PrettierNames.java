package creOrthologs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class PrettierNames
{
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getCREOrthologsDir() +
						File.separator + "contig_7000000220927531_forAlign.fasta");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"contig_7000000220927531_shorterNames.fasta")));
		
		HashMap<String, String> map = AddMetadata.getBroadCategoryMap();

		for(FastaSequence fs : list)
		{
			StringTokenizer sToken = new StringTokenizer(fs.getHeader());
			sToken.nextToken();
			String key = sToken.nextToken().replace(".scaffolds.fasta", "");
			
			if( ! map.containsKey(key))
				throw new Exception("No");
			
			
			if( map.get(key).equals("susceptible"))
				key = "*" + key;
			
			writer.write(">" + key + "\n");
			writer.write(fs.getSequence() + "\n");
		}
		
		writer.flush();  writer.close();
	}
}
