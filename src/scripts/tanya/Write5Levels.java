package scripts.tanya;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class Write5Levels
{
	public static void main(String[] args) throws Exception
	{
		// this file was edited to only the relevant data here..
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTanyaDir() + File.separator + 
				"SOLAR2AF011715Corrected.txt")));
		
		String firstLine = reader.readLine();
		
		String[] splits = firstLine.split("\t");
		int index=1;
		
		for(String s : splits)
		{
			
		}
	}
}
