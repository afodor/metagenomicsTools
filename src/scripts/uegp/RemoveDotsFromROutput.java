package scripts.uegp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class RemoveDotsFromROutput
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getUEGPDir() + File.separator + "pcoa_shortbred_withMetadataallData.txt")));
		
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getUEGPDir() + File.separator +
				"pcoa_shortbred_withMetadataallDataDotsRemoved.txt")));
		
		writer.write("id\t" + reader.readLine().replaceAll("\\.", "_").replaceAll("\\|", "_").replaceAll("#", "@") + "\n");
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			writer.write(s + "\n");
		}
		
		writer.flush();writer.close();
		reader.close();
		
	}
	
}
