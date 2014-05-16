package kleb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class WriteSequencesForR
{
	public static void main(String[] args) throws Exception
	{
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(ConfigReader.getKlebDir() + 
				File.separator + "alignmentOnlyDifferingPositions.txt");
		
		HashMap<Integer,StrainMetadataFileLine> metaMap = StrainMetadataFileLine.parseMetadata();
		//outbreaker can't deal with long times
		List<Integer> samples = new ArrayList<Integer>();
		List<GregorianCalendar> dates = new ArrayList<GregorianCalendar>();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKlebDir()+
				File.separator + "sequencesForR.fasta")));
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			String aKey = fs.getFirstTokenOfHeader();
			aKey = new StringTokenizer(aKey, "_").nextToken();
			
			Integer sampleID = Integer.parseInt(aKey);
			
			//if(includedSet.contains(sampleID))
			{
				System.out.println(sampleID);
				samples.add(sampleID);
				dates.add(MergeDataAndDistance.getGregorianCalendar(metaMap.get(sampleID).getDateString()));
				writer.write(">" + aKey + "\n");
				writer.write(fs.getSequence() + "\n");
			}
			
			
		}
		
		writer.flush();  writer.close();
		System.out.println(samples);
		writeDateVector(dates);
	}
	
	private static void writeDateVector(List<GregorianCalendar> dates) throws Exception
	{
		long minTime = Long.MAX_VALUE;
		
		for( GregorianCalendar gc : dates)
			minTime = Math.min(minTime, gc.getTimeInMillis());
		
		long aDay = 1000 * 60 * 60 * 24;
		
		System.out.println("c(");
		
		for( Iterator<GregorianCalendar> i = dates.iterator(); i.hasNext();)
		{
			System.out.print( ((i.next().getTimeInMillis() - minTime) / (aDay*10)) + "");
			
			if( i.hasNext())
				System.out.print(",");
		}

		System.out.print(")\n");
	}
	
}
