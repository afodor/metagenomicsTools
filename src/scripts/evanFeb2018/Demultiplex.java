package scripts.evanFeb2018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;

import parsers.FastQ;
import utils.ConfigReader;

public class Demultiplex
{
	public static void main(String[] args) throws Exception
	{
		File mappingFile1 = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
				"Batch_1_2015" + File.separator + "batch_1_2015_mapping.txt");
		
		File seqFile1= 
				new File(
						ConfigReader.getEvanFeb2018Dir() + File.separator + 
						"Batch_1_2015" + File.separator + "R_2015_03_31_14_16_19_user_Nikki.fastq");
		
		demultiplex(mappingFile1, seqFile1, 1);
		
		File mappingFile2 = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
				"Dellon_Batch2.mapping.txt");
		
		File seqFile2= 
				new File(
						ConfigReader.getEvanFeb2018Dir() + File.separator + "Dellon_Batch2.fastq");
		
		demultiplex(mappingFile2, seqFile2, 2);
	}
	
	private static void demultiplex(File mapFile, File sequenceFile, int batchNumber) throws Exception
	{
		HashMap<String, String> barcodeToSampleMap= getBarcodeToSampleMap(mapFile);

		HashMap<String, BufferedWriter> writerMap = new HashMap<>();		
		
		for(String s : barcodeToSampleMap.values())
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getEvanFeb2018Dir() + File.separator + "demultiplexFastaA" +
						File.separator + s + "_" + batchNumber 
						+ ".fasta" 
						)));
			
			writerMap.put(s,writer);
		}
		
		BufferedReader fastqReader = new BufferedReader(new FileReader(sequenceFile));
		
		long numFound =0;
		long numTested =0;
		
		for( FastQ fq = FastQ.readOneOrNull(fastqReader); fq != null; fq = FastQ.readOneOrNull(fastqReader))
		{
			boolean gotOne =false;
			
			for(String s : barcodeToSampleMap.keySet())
				if( fq.getSequence().startsWith(s))
				{
					gotOne = true;
					
					BufferedWriter writer = writerMap.get(barcodeToSampleMap.get(s));
					
					writer.write(">" + fq.getHeader() + "\n");
					writer.write( fq.getSequence() + "\n");
				}
					
			numTested++;
			
			if( gotOne)
				numFound++;
			
			if( numTested % 10000 == 0 )
				System.out.println(numFound + " " + numTested);
		}
		
		fastqReader.close();
		
		for( BufferedWriter w : writerMap.values())
		{
			w.flush(); w.close();
		}
	}
	
	private static HashMap<String, String> getBarcodeToSampleMap(File inFile) throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		HashSet<String> vals = new HashSet<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			
			String[] splits = s.split("\t");
			
			if(map.containsKey(splits[1]))
				throw new Exception("No");
			
			map.put(splits[1], splits[0]);
			
			if( vals.contains(splits[0]))
				throw new Exception("No");
			
			vals.add(splits[0]);
		}
		
		return map;
	}
}

