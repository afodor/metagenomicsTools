package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class ConfirmRowID
{
	public static void main(String[] args) throws Exception
	{
		runOne("PLASMA");
		runOne("URINE");
		System.out.println("Global pass");
	}
	
	private static void runOne(String type) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"fAsColumnsLogNormalized.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMicrboesVsMetabolitesDir()
				+ File.separator + type +  "AsColumns.txt")));
		
		reader.readLine();
		
		int index =0;
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			Integer anInt = Integer.parseInt(splits[0]);
			
			if( anInt != Integer.parseInt(wrapper.getSampleNames().get(index).replace("sample", "")))
				throw new Exception("No");
			else
				System.out.println(anInt + " " + wrapper.getSampleNames().get(index));
			
			index = index +1;
		}
		
		if( index != wrapper.getSampleNames().size())
			throw new Exception("No");
		
		System.out.println("passed");
	}
}
