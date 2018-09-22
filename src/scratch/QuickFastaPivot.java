package scratch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import parsers.FastaSequence;

public class QuickFastaPivot
{
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> list = FastaSequence.readFastaFile("c:\\temp\\someSeqs.txt");
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
			"c:\\temp\\out.txt"	)));
		
		writer.write("sequenceID\tnumA\tnumC\tnumG\tnumT\tsequence\n");
		
		char[] a= { 'A','C','G','T' };
		
		for(FastaSequence fs : list)
		{
			writer.write(fs.getFirstTokenOfHeader());
			
			for( char c : a )
				writer.write("\t" +  getNum(fs.getSequence(), c));
			
			writer.write("\t" + fs.getSequence() + "\n");
			
		}
		
		writer.flush();  writer.close();
	}
	
	private static int getNum(String s , char c)
	{
		int num=0;
		
		for( int x=0; x < s.length(); x++)
			if( s.charAt(x)==c)
				num++;
		
		return num;
	}
}
