package scripts.uegp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class RemoveDotsFromFirstLine
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getUEGPDir() + File.separator + "shortBREDwMetadata.txt")));
		
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getUEGPDir() + File.separator + "shortBREDwMetadataDotsRemoved.txt")));
		
		writer.write(reader.readLine().replaceAll("\\.", "_").replaceAll("\\|", "_") + "\n");
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			writer.write(s + "\n");
		}
		
		writer.flush();writer.close();
		reader.close();
		
	}
	
}
