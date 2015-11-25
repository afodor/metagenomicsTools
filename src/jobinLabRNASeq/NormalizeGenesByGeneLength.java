package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.GffParser;
import utils.ConfigReader;

public class NormalizeGenesByGeneLength
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, GffParser.Range> geneRangeMap =  GffParser.getAllRanges();
		
		//for(String s : geneRangeMap.keySet())
		//	System.out.println(s);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"pivotedSamplesAsColumnsR1Only.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"pivotedSamplesAsColumnsR1OnlyNormalizedByGeneLength.txt")));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine();
					s != null;
						s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			System.out.println(splits[0]);
			GffParser.Range r  = geneRangeMap.get(splits[0]);
			
			writer.write(splits[0]);
			
			
			
			float length = 1;
			
			if( ! splits[0].equals(FindGeneForMappedSequence.INTER_GENIC))
				length =  r.upper - r.lower;
			
			for( int x=1; x < splits.length; x++)
				writer.write( "\t" + ( Integer.parseInt(splits[x]) / length) );
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
		
	}
}
