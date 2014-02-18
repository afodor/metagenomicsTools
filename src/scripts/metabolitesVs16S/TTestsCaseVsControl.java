package scripts.metabolitesVs16S;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class TTestsCaseVsControl
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMetabolitesCaseControl() + 
				File.separator + "topeFeb2014_logNorm_gen.txt")));
		
		List<String> taxaNames= (getTaxaNames(reader.readLine()));
		HashMap<String, Holder> taxaMap = new LinkedHashMap<String, Holder>();
		
		for( String s : taxaNames)
		{
			Holder h = new Holder();
			h.taxaName = s;
			taxaMap.put(s, h);
		}
		
		
		reader.close();
	}
	
	private static class Holder
	{
		String taxaName;
		HashMap<String, Double> caseMap = new HashMap<String,Double>();
		HashMap<String, Double> controlMap = new HashMap<String,Double>();
	}
	
	private static List<String> getTaxaNames(String headerLine) throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		StringTokenizer sToken = new StringTokenizer(headerLine);
		
		sToken.nextToken(); sToken.nextToken();
		
		while( sToken.hasMoreTokens())
			list.add(sToken.nextToken());
			
		return list;
	}
	
}
