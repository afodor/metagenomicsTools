package creOrthologs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class DistancesFromFasta
{
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getCREOrthologsDir() + File.separator + 
					"contig_7000000220927531_postAlign.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"contig_7000000220927531_distances.txt")));
		
		writer.write("xFasta\tyFasta\tmismatchDistance\tgapToNonGap\tsumDistance\n");
		
		for(int x=0;x < list.size() -1 ; x++)
			for( int y=x+1; y < list.size(); y++)
			{
				System.out.println(x + " " + y);
				FastaSequence xSeq = list.get(x);
				FastaSequence ySeq = list.get(y);
				
				int numDiffs =0;
				int numGapDiffs = 0;
				
				if( xSeq.getSequence().length() != ySeq.getSequence().length())
					throw new Exception("No");
				
				for( int z=0; z < xSeq.getSequence().length(); z++)
				{
					char c1 = xSeq.getSequence().charAt(z);
					char c2 = ySeq.getSequence().charAt(z);
					
					if( c1 != '-' || c2 != '-')
					{
						if ( c1 != c2)
						{
							if( c1 == '-' || c2 == '-')
								numGapDiffs++;
							else
								numDiffs++;
						}
					}
				}
				
				writer.write(xSeq.getHeader() + "\t" + ySeq.getHeader() + "\t" + numDiffs + "\t" + numGapDiffs + "\t" +
									(numDiffs + numGapDiffs) + "\n");	
			}
		
		
		writer.flush();  writer.close();
	}
}
