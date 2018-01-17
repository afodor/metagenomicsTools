package scripts.emilyJan2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		File meta1 =new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"2018-01-10_AN703_16S metadata.txt");
		
		File meta2 = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
				"2018-01-10_AN40_16S metadata.txt");
		
		File meta3 = new File(ConfigReader.getEmilyJan2018Dir() + File.separator + 
					"2018-01-10_AN34_16S metadata.txt");
		
		String top1= getFirstLine(meta1);
		String top2 = getFirstLine(meta2);
		String top3 = getFirstLine(meta3);
		
		if( ! top1.equals(top2))
			throw new Exception("No");
		
		if( ! top1.equals(top3))
			throw new Exception("No");
		
		if( ! top2.equals(top3))
			throw new Exception("Logic error");
	}
	
	private static String getFirstLine(File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String firstLine = reader.readLine();
		
		reader.close();
		
		return firstLine;
	}
}
