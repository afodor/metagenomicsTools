package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

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
					File.separator +"carolina" + File.separator 
					+ "klebsiella_pneumoniae_chs_11.0.scaffolds.fasta");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + 
				File.separator +"carolina" + File.separator 
				+ "klebsiella_pneumoniae_chs_11.0.genes.gtf")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"carolina" + File.separator + "predictedGenes_chs11.txt")));
		
		BufferedWriter protWriter = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
				"carolina" + File.separator + "predictedProteins_chs11.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			//System.out.println(s);
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
							Math.max(0,Integer.parseInt(splits[3])-4), Integer.parseInt(splits[4])	);
					
					dna = Translate.safeReverseTranscribe(dna);
					

					if(Integer.parseInt(splits[3])-4  < 0)
						System.out.println("Warning possible frame shift " + dna);
				
				}
				else if ( splits[6].equals("+"))
				{
					dna = fs.getSequence().substring(
							Integer.parseInt(splits[3])-1, Math.min( Integer.parseInt(splits[4])+3, fs.getSequence().length()));
					
					if( Integer.parseInt(splits[4])+3 > fs.getSequence().length())
						System.out.println("Warning possible frame shift " + dna);
				
				}else
					throw new Exception("No " + splits[6]);
				
				StringTokenizer sToken = new StringTokenizer(splits[8]);
				sToken.nextToken();
				String shortKey = 
						sToken.nextToken().replaceAll("\"", "").replace(";", "").replace("gene_id", "")+ " ";
				
				writer.write( ">" +shortKey + 
						splits[8] + " " +  splits[6] + "\n" +  dna  + "\n");
				
				//System.out.println(splits[8]);
				protWriter.write(">"+ shortKey+   
						splits[8] + " " +  splits[6] + "\n" + 
						Translate.getSafeProteinSequence(dna)  + "\n");
			}
		}
		
		reader.close();
		
		writer.flush();  writer.close();
		protWriter.flush(); protWriter.close();
	}
}
