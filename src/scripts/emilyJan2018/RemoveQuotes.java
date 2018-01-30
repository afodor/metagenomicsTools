package scripts.emilyJan2018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class RemoveQuotes
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getEmilyJan2018Dir() + File.separator + "spreadsheets"
				+ File.separator + "pcoa_withMetadatagenus.txt")));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getEmilyJan2018Dir() + File.separator + "spreadsheets"
				+ File.separator + "pcoa_withMetadatagenusNoQuotes.txt")));
		
		for(String s= reader.readLine(); s!=null; s= reader.readLine())
		{
			writer.write(s.replaceAll("\"", "") + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
