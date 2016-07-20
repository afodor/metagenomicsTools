package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class AddAnnotationstToCoinFlip
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getAnnotationMap();
		
		for(String s : map.keySet())
			System.out.println(s);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator + "coinFlipsSucVsRes.txt"	)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator + "coinFlipsSucVsResWithAnnotations.txt"	)));
		
		writer.write(reader.readLine() + "\tannotation\n");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0].trim();
			
			String val = map.get(key);
			
			if( val == null)
				throw new Exception("Could not find  " + key);
			
			writer.write(s + "\t" + val + "\n");
			
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	static HashMap<String, String> getAnnotationMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader( new FileReader(
				ConfigReader.getBioLockJDir() + File.separator + 
						"resistantAnnotation" + File.separator + "chs11WithMBDGAnnotation.gtf"));
		
		for(String s=reader.readLine(); s != null; s = reader.readLine())
		{
			String val = s.split("\t")[8];
			
			StringTokenizer sToken = new StringTokenizer(val, ";");
			String key = sToken.nextToken();
			map.put(key.trim(), val.replace(key + ";", "").trim());
		}
		
		return map;
	}
}
