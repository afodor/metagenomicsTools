package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class GatherMDSAcrossTree
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getRachSachReanalysisDir()
				+ File.separator + "rdpAnalysis" 
				+ File.separator + "collapsedMDS.txt")));
		
		writer.write("level\tminCecum\tminColon\n");
		
		String[] levels = { "phylum","class","order","family","genus", "otu" };
		
		for(String level : levels )
		{
			writer.write(level + "\t");
			writer.write(getVal(level, "Cecal Content")  + "\t");
			writer.write(getVal(level, "Colon content")  + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static double getVal(String level, String tissue)
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader( new File(
				ConfigReader.getRachSachReanalysisDir()
				+ File.separator + "rdpAnalysis" 
				+ File.separator + "pValuesForTime_pcoa_" + tissue +  "_" + level +".txt")));
						
		double minValue = Double.MAX_VALUE;
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 3)
				throw new Exception("Parsing error");
			
			minValue = Math.min(minValue, Double.parseDouble(splits[2]));
		}
		
		return minValue;
	}
}
