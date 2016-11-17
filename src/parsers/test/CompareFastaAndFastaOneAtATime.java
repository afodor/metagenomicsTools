package parsers.test;

import java.io.File;
import java.util.List;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class CompareFastaAndFastaOneAtATime
{
	public static void main(String[] args) throws Exception
	{
		// this file is at 
		// https://github.com/afodor/metagenomicsTools/blob/master/src/parsers/test/someFasta.txt
		File aFile = new File("C:\\Temp\\someFasta.txt");	
		
		List<FastaSequence> list = FastaSequence.readFastaFile(aFile);
		
		if( list.size() != 3)
			throw new Exception("Unexpected size");
		
		if( ! list.get(0).getHeader().equals(">Seq1"))
			throw new Exception("Incorrect header");
		
		if( ! list.get(0).getSequence().equals("AAACCCGTTTAAAACCCTTT"))
			throw new Exception("Incorrect sequence");
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(aFile);
		
		int index =0;
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			FastaSequence fromList = list.get(index);
			
			if( ! fromList.getHeader().equals(fs.getHeader()))
				throw new Exception("Incorrect header");
			
			if( ! fromList.getSequence().equals(fs.getSequence()))
				throw new Exception("Incorrect sequence");
			
			index++;
		}
		
		System.out.println("passed");
	}
}
