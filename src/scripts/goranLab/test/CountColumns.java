package scripts.goranLab.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class CountColumns
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getGoranTrialDir() + File.separator + 
			"otuCounts.txt")));
		
		String first = reader.readLine();
		
		String[] splits = first.split("\t");
		
		System.out.println(splits.length);
	}
}
