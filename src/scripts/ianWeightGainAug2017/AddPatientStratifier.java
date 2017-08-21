package scripts.ianWeightGainAug2017;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class AddPatientStratifier
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(
				ConfigReader.getIanMouseAug2017Dir() + File.separator + 
					"2017-08-09_AN40_AN703_AN34_combined.txt"));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getIanMouseAug2017Dir() + File.separator + 
				"2017-08-09_AN40_AN703_AN34_combinedPlusStratifier.txt"
				)));
		
		writer.write(reader.readLine() + "\tstratifier\n");
		
		for(String s = reader.readLine(); s != null ; s = reader.readLine())
		{
			writer.write(s + "\t" +  getStratifier(s) + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static String getStratifier(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		if( splits[2].equals("HC"))
			return "HC_" + splits[1];
		
		return "T_" + splits[1];
	}
}
