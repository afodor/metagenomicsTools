package creOrthologs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class GC_Content
{
	public static double getGCContent(String s, int position, int windowSize) 
	{
		int end = s.length();
		end = Math.min(position+windowSize, end);
		
		double numGC =0;
		double numGood = 0;
		
		for( int x=position; x < end; x++)
		{
			char c = s.charAt(x);
			
			if( c == 'A' || c=='C' || c == 'G' || c=='T')
				numGood++;
			
			if( c== 'C' || c == 'G')
				numGC++;
		}
		
		if( numGood == 0 )
			return 0;
		
		return numGC / numGood;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getCREOrthologsDir()+
				File.separator + "carolina" + File.separator + 
				"klebsiella_pneumoniae_chs_11.0.scaffolds.fasta");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir()+
				File.separator + "carolina" + File.separator + 
				"gc11.txt")));
		
		
		writer.write("contig\tstart\tgcContent\n");
		
		
		for(FastaSequence fs : list)
		{
			String seq = fs.getSequence().toUpperCase();
			
			for( int x=0; x < seq.length(); x= x+ 1000)
			{
				writer.write(fs.getFirstTokenOfHeader() + "\t" + x + "\t" + 
							getGCContent(seq, x, 5000) + "\n");
			}
		}
		
		writer.flush();  writer.close();
	}
}
