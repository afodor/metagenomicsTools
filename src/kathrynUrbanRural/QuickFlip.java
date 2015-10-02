package kathrynUrbanRural;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class QuickFlip
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getChinaDir() + 
				File.separator + "KathrynTables" + File.separator +  "otuModel_pValues_otu.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getChinaDir() + 
				File.separator + "KathrynTables"  + File.separator + "otuModel_pValues_otuFlipped.txt" )));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for( int x=0; x <=8; x++)
				writer.write(splits[x] + "\t");
			
			double aVal = Math.log10(Double.parseDouble(splits[9]));
			
			if( Double.parseDouble(splits[2]) > Double.parseDouble(splits[4]) )
				aVal = -aVal;
			
			writer.write(aVal + "\t");
			
			aVal = Math.log10(Double.parseDouble(splits[10]));
			
			if( Double.parseDouble(splits[3]) > Double.parseDouble(splits[5]) )
				aVal = -aVal;
			
			writer.write(aVal + "");
			
			for( int x=10; x < splits.length;x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
			
		}
		
		reader.readLine();
		
	}
}
