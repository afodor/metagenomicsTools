package kleb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
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
				File.separator + "Klebs.mfa");
		
		HashMap<Integer,StrainMetadataFileLine> metaMap = StrainMetadataFileLine.parseMetadata();
		HashSet<Integer> includedSet = MergeDataAndDistance.getOutbreakGroup();
		List<Integer> samples = new ArrayList<Integer>();
		List<GregorianCalendar> dates = new ArrayList<GregorianCalendar>();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKlebDir()+
				File.separator + "sequencesForR.fasta")));
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			String aKey = fs.getFirstTokenOfHeader();
			aKey = new StringTokenizer(aKey, "_").nextToken();
			
			Integer sampleID = Integer.parseInt(aKey);
			
			if(includedSet.contains(sampleID))
			{
				System.out.println(sampleID);
				samples.add(sampleID);
				dates.add(MergeDataAndDistance.getGregorianCalendar(metaMap.get(sampleID).getDateString()));
				writer.write(">" + aKey + "\n");
				writer.write(fs.getSequence() + "\n");
			}
			
			
		}
		
		writer.flush();  writer.close();
	}
	
}
