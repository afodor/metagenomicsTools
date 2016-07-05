package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class CoinFlipTest
{
	private static class Holder 
	{
		private int stop;
		private int start;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<Holder>> map = parseGTFFile();
		
		for(String s : map.keySet())
			System.out.println(s);
	}
	
	private static HashMap<String, List<Holder>> parseGTFFile() throws Exception
	{
		HashMap<String, List<Holder>> map = new LinkedHashMap<String, List<Holder>>();
		
		BufferedReader reader =new BufferedReader(new FileReader(
			ConfigReader.getBioLockJDir() + File.separator + 
				"resistantAnnotation" + File.separator + 
						"klebsiella_pneumoniae_chs_11.0.genes.gtf"	));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = new StringTokenizer(splits[8], ";").nextToken();
			key = key.replace("gene_id", "").replaceAll("\"", "");
			map.put(key, null);
		}
		
		reader.close();
		
		return map;
	}
}
