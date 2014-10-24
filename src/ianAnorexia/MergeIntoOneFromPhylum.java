package ianAnorexia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeIntoOneFromPhylum
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getIanAnorexiaDir() + File.separator + 
				"mergedFamily.txt")));
		
		///writer.write("sample\ttime\ttype\tMDS1\tMDS2\tMDS3\tage\tweight1\tweight2\tweightDiff\tbai\tbdi\n");
		
		HashMap<Integer, HumanMetadataParser> map = 
					HumanMetadataParser.getAsMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getIanAnorexiaDir() + File.separator + 
			"AN Data_07.29.14_with clinical data_FAMILY.txt")));
		
		writer.write( reader.readLine()
				+ "\tage\tweight1\tweight2\tweightDiff\tbai\tbdi\n"	);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for( int x=0;x  < splits.length; x++)
				writer.write( splits[x] + "\t");
			
			HumanMetadataParser meta = map.get(Integer.parseInt(splits[0]));
			
			if( meta == null)
				throw new Exception("No");
			
			writer.write("" + meta.getAge() );
			writer.write( "\t" + getValOrNone(meta.getWeightT1()) );
			writer.write("\t" + getValOrNone(meta.getWeightT2()) );
			writer.write("\t" + getValOrNone(meta.getWeightDiff()) );
			writer.write("\t" + getValOrNone(meta.getBai()) );
			writer.write("\t" + getValOrNone(meta.getBdi()) + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static String getValOrNone(Float f)
	{
		return f== null ? "" : (f+"");
	}
}
