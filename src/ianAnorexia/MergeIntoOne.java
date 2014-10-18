package ianAnorexia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class MergeIntoOne
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getIanAnorexiaDir() + File.separator + 
				"merged.txt")));
		
		writer.write("sample\tpatientID\ttype\tMDS1\tMDS2\tMDS3\tage\tweight1\tweight2\tweightDiff\n");
		
		HashMap<Integer, HumanMetadataParser> map = 
					HumanMetadataParser.getAsMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getIanAnorexiaDir() + File.separator + 
			"pcoaOut.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for( int x=0;x  < 6; x++)
				writer.write(splits[x] + "\t");
			
			HumanMetadataParser meta = map.get(Integer.parseInt(splits[1]));
			
			if( meta == null)
				throw new Exception("No");
			
			writer.write(meta.getAge() + "");
			writer.write( getValOrNone(meta.getWeightT1()) + "\t");
			writer.write(getValOrNone(meta.getWeightT2()) + "\t");
			writer.write(getValOrNone(meta.getWeightDiff()) + "\n");
		}
		
		writer.flush();  writer.close();
		
		
	}
	
	private static String getValOrNone(Float f)
	{
		return f== null ? "" : (f+"");
	}
}
