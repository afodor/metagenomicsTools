package scripts.evanFeb2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.FastQ;
import utils.ConfigReader;

public class Demultiplex
{

	public static void main(String[] args) throws Exception
	{
		File mappingFile1 = new File(ConfigReader.getEvanFeb2018Dir() + File.separator + 
				"Batch_1_2015" + File.separator + "batch_1_2015_mapping.txt");
		
		HashMap<String, String> barcode1 = getBarcodeToSampleMap(mappingFile1);

		for(String s : barcode1.keySet())
			System.out.println(s +  " "+ barcode1.get(s));
		
		BufferedReader fastq1Reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getEvanFeb2018Dir() + File.separator + 
				"Batch_1_2015" + File.separator + "R_2015_03_31_14_16_19_user_Nikki.fastq")));
		
		long numFound =0;
		long numTested =0;
		
		for( FastQ fq = FastQ.readOneOrNull(fastq1Reader); fq != null; fq = FastQ.readOneOrNull(fastq1Reader))
		{
			boolean gotOne =false;
			
			for(String s : barcode1.keySet())
				if( fq.getSequence().startsWith(s))
					gotOne = true;
			
			numTested++;
			
			if( gotOne)
				numFound++;
			
			if( numTested % 10000 == 0 )
				System.out.println(numFound + " " + numTested);
		}
	}
	
	private static HashMap<String, String> getBarcodeToSampleMap(File inFile) throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			
			String[] splits = s.split("\t");
			
			if(map.containsKey(splits[1]))
				throw new Exception("No");
			
			map.put(splits[1], splits[0]);
		}
		
		return map;
	}
	
}

