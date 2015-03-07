package mbqc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class TestMDSMeta
{
	public static void main(String[] args) throws Exception
	{
		simpleTokenCheck();
	}
	
	public static void simpleTokenCheck() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getMbqcDir() + File.separator + 
			"dropbox" + File.separator +  "alpha-beta-div" + File.separator +  "beta-div"
					+ File.separator + "metadataForMergedSpecies.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( ! splits[0].startsWith(splits[1]))
				throw new Exception("No ");
		}
		
		reader.close();
		System.out.println("Passed simple token check");
	}
}
