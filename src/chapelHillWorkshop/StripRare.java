package chapelHillWorkshop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class StripRare
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(
			ConfigReader.getChapelHillWorkshopDir() + File.separator + 
			"humann2_genefamilies-2.tsv")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
			ConfigReader.getChapelHillWorkshopDir() + File.separator + 
				"humann2_genefamilies-2AtLeastThreeNonZero.tsv")));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 21)
				throw new Exception("unexpected tokens");
			
			if( countNonZero(splits) >=3)
				writer.write(s + "\n");
		}
		
		writer.flush(); writer.close();
		reader.close();
	}
	
	private static int countNonZero(String[] a)
	{
		int count =0;
		
		for( int x=1; x < a.length; x++)
			if( Double.parseDouble( a[x] )  != 0 ) 
				count++;
		
		return count;
	}
	
}
