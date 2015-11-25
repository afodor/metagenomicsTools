package cogFunctionalCategoryParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CogFunctionalMap
{
	public static HashMap<String, String> getCogFunctionalMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"COG_Annotations" + File.separator + 
				"fun.txt")));
		
		for(String s = reader.readLine();
				s != null;
					s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			String key = sToken.nextToken();
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			StringBuffer buff = new StringBuffer();
			
			buff.append(sToken.nextToken());
			
			while( sToken.hasMoreTokens())
				buff.append(" " + sToken.nextToken());
			
			map.put(key, buff.toString());
		}
		
		reader.close();
		
		return map;
	}
}
