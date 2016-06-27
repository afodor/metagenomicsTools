package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.FastaSequence;
import utils.ConfigReader;
import utils.Translate;

public class QuickTranslate 
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, FastaSequence> map =
			FastaSequence.getFirstTokenSequenceMap(
					ConfigReader.getCREOrthologsDir() + 
					File.separator +"quickTranslation" + File.separator 
					+ "klebsiella_pneumoniae_chs_76.0.scaffolds.fasta");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + 
				File.separator +"quickTranslation" + File.separator 
				+ "klebsiella_pneumoniae_chs_76.0.genes.gtf")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"quickTranslation" + File.separator + "predictedGenes.txt")));
		
		BufferedWriter protWriter = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"quickTranslation" + File.separator + "predictedProteins.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			FastaSequence fs = map.get(splits[0]);
			
			if( fs == null)
				throw new Exception("No");
			
			if( splits[2].equals("CDS"))
			{	
				String dna=null;
				
				if( splits[6].equals("-"))
				{
					dna =  fs.getSequence().substring(
							Integer.parseInt(splits[3])-4, Integer.parseInt(splits[4])	);
					
					dna = Translate.safeReverseTranscribe(dna);
				
				}
				else if ( splits[6].equals("+"))
				{
					dna = fs.getSequence().substring(
							Integer.parseInt(splits[3])-1, Integer.parseInt(splits[4])+3	);
				
				}else
					throw new Exception("No " + splits[6]);
				
				writer.write( ">" + 
						splits[8] + " " +  splits[6] + "\n" +  dna  + "\n");
				
				protWriter.write(">" + 
						splits[8] + " " +  splits[6] + "\n" + 
						Translate.getSafeProteinSequence(dna)  + "\n");
			}
		}
		
		reader.close();
		
		writer.flush();  writer.close();
		protWriter.flush(); protWriter.close();
	}
}
