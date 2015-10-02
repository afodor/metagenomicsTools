package scratch;

import java.util.List;

import parsers.FastaSequence;

public class FastaSequenceDriver
{
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> fastaList = 
				FastaSequence.readFastaFile("c:\\pointsToSome\\FastaFile.txt");
		
		for( FastaSequence fs : fastaList)
		{
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println(fs.getGCRatio());
		}
	}
}
