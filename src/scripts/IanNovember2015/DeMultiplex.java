package scripts.IanNovember2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class DeMultiplex
{
	public static HashMap<String, String> getSampleID() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getIanNov2015Dir() + File.separator + 
						"MAP_HC_R1.txt")));
		
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> sampleIDs = getSampleID();
	}
}
