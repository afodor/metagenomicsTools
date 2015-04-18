package scripts.JobinApril2015.cjej;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeForwardBackward
{
	public static void main(String[] args) throws Exception
	{
		File inFileF = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
						"cjej_Freads.txt");
		File outFileF = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"cjej_FreadsNoTax.txt");
		
		addTag(inFileF, outFileF, "_1");
		
		File inFileR = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"cjej_Rreads.txt");
		File outFileR = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"cjej_RreadsNoTax.txt");

		addTag(inFileR, outFileR, "_2");
		
		File transposedFFile =  new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"cjejF_taxaAsColumns.txt");
		

		File transposedRFile =  new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"cjejR_taxaAsColumns.txt");
		
		
		OtuWrapper.transpose(outFileF.getAbsolutePath(), transposedFFile.getAbsolutePath(), false);
		OtuWrapper.transpose(outFileR.getAbsolutePath(), transposedRFile.getAbsolutePath(), false);
		
		File mergedFile = new File(ConfigReader.getJobinApril2015Dir() + File.separator + 
				"cjejR_taxaAsColumns_mergedF_R.txt");
		
		OtuWrapper.merge(transposedFFile, transposedRFile, mergedFile);
		
	}
	
	private static void addTag(File inFile, File outFile, String tag) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] firstTokens = reader.readLine().split("\t");
		
		writer.write(firstTokens[0]);
		
		for( int x=1; x < firstTokens.length - 1; x++)
			writer.write("\t" + firstTokens[x] + "_" + tag);
		
		writer.write("\n");
		
		for( String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			writer.write(splits[0]);
			
			for( int x=1; x < splits.length-1; x++ )
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
