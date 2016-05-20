package chapelHillWorkshop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

import utils.ConfigReader;

public class StripRare
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(
			ConfigReader.getChapelHillWorkshopDir() + File.separator + 
			"humann2_genefamilies.LABELS2.tsv")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
			ConfigReader.getChapelHillWorkshopDir() + File.separator + 
				"humann2_genefamilies-RemovedRare.tsv")));
		
		writer.write(reader.readLine() + "\n");
		
		int x=1;
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 21)
				throw new Exception("unexpected tokens");
			
			if( getRowSum(splits) >= 5 *  1e-05 )
			{
				//String firstToken = splits[0].replaceAll("\"", "").replaceAll(".", "_");
				writer.write(x + "_" +x);
				
				for(int y=1; y < splits.length; y++)
					writer.write("\t" + splits[y]);
				
				writer.write("\n");
				x++;
			}
		}
		
		System.out.println("Wrote " + x);
		writer.flush(); writer.close();
		reader.close();
	}
	
	private static double getRowSum(String[] a)
	{
		double sum=0;
		
		for( int x=1; x < a.length; x++)
			sum += Double.parseDouble( a[x] );
		
		return sum;
	}
	
}
