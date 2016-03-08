package scripts.RoshondaCaseControlCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class QuickCheck
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getRoshondaCaseControlDir() + File.separator + 
				"caseControl.txt")));
		
		reader.readLine();
		
		for(String s=  reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 1)
				throw new Exception("No " + s + " " + splits.length);
			
			// D5-686-525_2_1_380control
			File testFile = new File(ConfigReader.getRoshondaCaseControlDir() + File.separator +
					"OriginalFiles" + File.separator + "Tope_" 
					+ splits[0].replaceAll("case", "").replaceAll("control", "") + ".fas");
			
			System.out.println(testFile.getAbsolutePath());
			
			if( ! testFile.exists())
				throw new Exception("No");
		}
		
		System.out.println("Passed");
	}
}
