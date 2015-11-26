package creOrthologs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class DistancesFromFasta
{
	private static String getMergedLocation(String s1, String s2)
	{
		List<String> list = new ArrayList<String>();
		list.add(s1);
		list.add(s2);
		Collections.sort(list);
		
		return list.get(0) + "_" + list.get(1);
	}
	
	private static String getLocation(String s) throws Exception
	{
		String[] splits = s.split("_");
		
		for(int x=1; x < splits.length; x++)
			if( Character.isDigit( splits[x].charAt(0)))
				return splits[x-1];
		
		throw new Exception("Could not find " + s);
	}
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> list = FastaSequence.readFastaFile(ConfigReader.getCREOrthologsDir() + File.separator + 
					"contig_7000000220927531_postAlign.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"contig_7000000220927531_distances.txt")));
		
		writer.write("xFasta\tyFasta\txLocation\tyLocation\txLocation_yLocaiton\tmismatchDistance\tgapToNonGap\tsumDistance\n");
		
		for(int x=0;x < list.size() -1 ; x++)
		{
			System.out.println(x );
			FastaSequence xSeq = list.get(x);
			String xLocation = getLocation(xSeq.getHeader());
			
			for( int y=x+1; y < list.size(); y++)
			{
				FastaSequence ySeq = list.get(y);
				String yLocation= getLocation(ySeq.getHeader());
				
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
				
				writer.write(xSeq.getHeader() + "\t" + ySeq.getHeader() + "\t" +
						xLocation + "\t" + yLocation + "\t" + getMergedLocation(xLocation, yLocation)+ "\t" + 
						numDiffs + "\t" + numGapDiffs + "\t" +
									(numDiffs + numGapDiffs) + "\n");	
			}
		}
		
		writer.flush();  writer.close();
	}
}
