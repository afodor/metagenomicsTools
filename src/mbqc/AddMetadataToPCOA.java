package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class AddMetadataToPCOA
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "dropbox" + File.separator + 
				"alpha-beta-div" + File.separator +  "beta-div" + File.separator + "attic" + 
						File.separator + "pcoa-qiime.txt")));
		
		for(String s= reader.readLine();  s !=null; s = reader.readLine())
		{
			System.out.println(s);
		}
	}
}
