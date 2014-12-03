package ruralVsUrban.abundantOTU;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	private static int getPatientId(String s) 
	{
		String first = new StringTokenizer(s, "_").nextToken().replace("A", "").replace("B", "").replace("\"", "");
		return Integer.parseInt(first);
	}
	
	private static String getTimepoint(String s)
	{
		s=s.replaceAll("\"", "");
		if(s.startsWith("B"))
			return "second_B";
		
		return "first_A";
	}
	
	private static void addSomeMetadata( OtuWrapper wrapper, BufferedReader reader, 
					BufferedWriter writer) throws Exception
	{
		writer.write("sampleID\tpatientID\truralUrban\ttimepoint\tshannonDiversity\tshannonEveness\tunRarifiedRichness");
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0] + "\t");
		
			int patientID = getPatientId(splits[0]);
			writer.write(patientID+ "\t");
			
			if( patientID >=1 && patientID <=39)
				writer.write("rural\t");
			else if (patientID >=81 && patientID <= 120)
				writer.write("urban\t");
			else throw new Exception("No");
			
			writer.write(getTimepoint(s) + "\t");
			
			writer.write(wrapper.getShannonEntropy(splits[0]) + "\t");
			writer.write(wrapper.getEvenness(splits[0]) + "\t");
			writer.write(wrapper.getRichness(splits[0]) + "");
			
			for( int y=1; y < splits.length; y++)
				writer.write("\t" + splits[y]);
			
			writer.write("\n");	
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getChinaDir() + 
				File.separator + "abundantOTU" + File.separator + 
				"abundantOTUForwardTaxaAsColumns.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + 
				File.separator + "abundantOTU" + File.separator + 
				"abundantOTUForwardTaxaAsColumnsLogNormal.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getChinaDir() + 
				File.separator + "abundantOTU" + File.separator + 
				"abundantOTUForwardTaxaAsColumnsLogNormalWithMetadata.txt"	)));
		
		addSomeMetadata(wrapper, reader, writer);
	}
}
