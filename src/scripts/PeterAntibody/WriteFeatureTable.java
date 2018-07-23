package scripts.PeterAntibody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WriteFeatureTable
{
	public static final String[] AMINO_ACID_CHARS=
		{"A"	,"C",	"D",	"E",	"F",	"G", 	
				"H",	"I",	"L",	"K",	"M"	, "N",	"P",	"Q",	"R",	"S"	, "T",	"V"
				,"W"	,"Y"};
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String,Double>> map = buildMap();
	}
	
	private static HashMap<String, HashMap<String,Double>> buildMap() throws Exception
	{
		HashMap<String, HashMap<String,Double>>  map = new HashMap<>();
		
		String[] chains =  {"LC", "HC" };
		
		for( String c : chains)
			for( int x=1; x <=6; x++)
			{
				 addToMap(c, x, map);
			}
		
		return map;
	}
	
	
	// outer key is classification_position_aaChar
	private static void addToMap(String hcLc, int num,HashMap<String, HashMap<String,Double>> map) throws Exception
	{
		String chotString = "Chotia";
		
		if( hcLc.equals("HC"))
			chotString = "Chothia";
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getPeterAntibodyDirectory() + File.separator + 
				hcLc + "_STATS_"  + chotString + "_" + num + ".txt" )));
		
		StringTokenizer sToken = new StringTokenizer( reader.readLine());
		
		String classificationKey = sToken.nextToken();
		System.out.println(classificationKey);
		
		for(String s= reader.readLine() ; s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
		}
		
		reader.close();
	}
}
