package scripts.PeterAntibody;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WriteSortedFeatureTable
{	
	
	public static void main(String[] args) throws Exception
	{
		writeFile();
	}
	
	private static HashMap<String,Double> writeFile() throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getPeterAntibodyDirectory() + File.separator + 
			"mergedWithSortTerm.txt")));
		
		writer.write("classification\tchain\toriginalLabel\tsortLabel\taaChar\trawCount\tnormalizedCount\tn\n");
		
		HashMap<String,Double> map = new HashMap<>();
		
		for( String c : WriteFeatureTable.CHAINS)
			for( int x=1; x <=6; x++)
			{
				 addToWriter(writer, c, x, map);
			}
		
		writer.flush();  writer.close();
		
		return map;
	}
	
	
	// outer key is classification_position_aaChar
	private static void addToWriter(BufferedWriter writer, String hcLc, int num,HashMap<String,Double> map) throws Exception
	{
		String chotString = "Chotia";
		
		if( hcLc.equals("HC"))
			chotString = "Chothia";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				hcLc + "_STATS_"  + chotString + "_" + num + ".txt" )));
		
		String firstLine = reader.readLine();
		String[] firstSplits = firstLine.split("\t");
		
		StringTokenizer sToken = new StringTokenizer( firstSplits[0].trim().replace(" " , "-"));
		
		String classificationKey = sToken.nextToken();
		System.out.println(classificationKey);
		
		for(String s= reader.readLine() ; s != null && s.trim().length()>0; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			String position = splits[0];
			String sortPosition = position;
			
			while(sortPosition.length() < 6)
				sortPosition= sortPosition.charAt(0) + "0" + sortPosition.substring(1);
			
			if( ! Character.isDigit(sortPosition.charAt(sortPosition.length()-1)))
			{
				sortPosition = sortPosition.charAt(0) + "9_" + sortPosition.substring(1);
			}
			
			//System.out.println(position);
			
			int[] vals = new int[WriteFeatureTable.AMINO_ACID_CHARS.length];
			int sum =0;
			
			for( int x=0; x < WriteFeatureTable.AMINO_ACID_CHARS.length; x++)
			{
				vals[x] = Integer.parseInt(splits[x+1]);
				sum += vals[x];
			}
				
			if( sum != Integer.parseInt(splits[22]))
				System.out.println("Warning " +  position + " " + sum + " " + Integer.parseInt(splits[22]));
			

		//	writer.write("classification\tchain\toriginalLabel\tsortLabel\taaChar\trawCount\tnormalizedCount\tnumAAs\n");
		
			for( int x=0; x < WriteFeatureTable.AMINO_ACID_CHARS.length; x++)
			{
				writer.write(classificationKey + "\t");
				writer.write(hcLc + "\t");
				writer.write(position + "\t");
				writer.write(sortPosition + "\t");
				writer.write(WriteFeatureTable.AMINO_ACID_CHARS[x] + "\t");
				writer.write(vals[x] + "\t");
				writer.write( (vals[x] / ((double)sum)) + "\t"); ;
				writer.write(sum + "\n");
			}
			
			
		}	
		
		writer.flush();
		reader.close();
	}
}
