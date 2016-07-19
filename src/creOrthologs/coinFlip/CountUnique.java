package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class CountUnique
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getBioLockJDir() +
					File.separator + "resistantAnnotation" + File.separator + 
							"carolinaVsSuc_To11.gwas"));
		
		int numRead = 0;
		int numDiff = 0;
		Double lastDouble = null;
		
		reader.readLine();
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Double val = Double.parseDouble(splits[3]);
			
			if( lastDouble == null)
			{
				lastDouble = val;
				numDiff++;
			}
			
			numRead++;
			
			if( ! lastDouble.equals(val))
			{
				numDiff++;
				lastDouble = val;
			}
		}
		
		System.out.println( numRead +  " "  + numDiff);
		reader.close();
		
	}
}
