package scripts.TopeDiverticulosisDec2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;

public class Demultiplex
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader2 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
						ConfigReader.getTopeDiverticulosisDec2015Dir() + File.separator + 
						"local" + File.separator +  "scratch" + File.separator  + "mhumphrys" + 
								File.separator + 
									"APJR3_20151123_M01994_IL100064365_NoIndex_L001_R2.fastq.gz"))));
		
		BufferedReader reader3 = 
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream(
						ConfigReader.getTopeDiverticulosisDec2015Dir() + File.separator + 
						"local" + File.separator +  "scratch" + File.separator  + "mhumphrys" + 
								File.separator + 
									"APJR3_20151123_M01994_IL100064365_NoIndex_L001_R3.fastq.gz"))));
		
		long numDone = 0;
		for( FastQ fastq2 = FastQ.readOneOrNull(reader2); fastq2 != null;
				fastq2 = FastQ.readOneOrNull(reader2))
		{
			FastQ fastq3 =  FastQ.readOneOrNull(reader3);
			
			if( fastq3 == null)
				throw new Exception("No");
			
			if( ! fastq2.getFirstTokenOfHeader().equals(fastq3.getFirstTokenOfHeader()))
					throw new Exception("No");
			
			numDone++;
			
			if( numDone % 1000 == 0)
				System.out.println(numDone);
		}
		
		if(  FastQ.readOneOrNull(reader3) != null )
			throw new Exception("No");
	}
}
