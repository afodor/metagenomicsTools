package scratch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;

public class StraightenOutFasta
{
	public static void main(String[] args) throws Exception
	{
		File inDirectory = new File("c:\\Users\\corei7\\git\\adenomasRelease\\fasta");
		
		File outDirectory = new File("C:\\AdenomasForRoshonda\\filesOneLinePer");
		
		for(String s : inDirectory.list())
		{
			System.out.println(s);
			if(s.endsWith(".gz"))
			{
				FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(
						inDirectory.getAbsolutePath() + File.separator + s ,true);
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					outDirectory.getAbsolutePath() + File.separator + s.replace(".gz", "")	)));
				
				for(FastaSequence fs = fsoat.getNextSequence() ; fs != null; fs = fsoat.getNextSequence())
				{
					writer.write(">" + fs.getFirstTokenOfHeader() + "\n");
					writer.write(fs.getSequence() + "\n");
				}
				
				writer.flush();  writer.close();
				fsoat.close();
			}
		}
		
		
	}
}
