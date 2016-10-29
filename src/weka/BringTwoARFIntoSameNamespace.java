package weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class BringTwoARFIntoSameNamespace
{
	private static HashMap<String,Integer> getNumericAttributes( File file ) throws Exception
	{
		HashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int x=0;
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if( s.startsWith("@attribute") && s.endsWith("numeric"))
			{
				StringTokenizer sToken = new StringTokenizer(s);
				sToken.nextToken();
				
				map.put(new String(sToken.nextToken()),x);
				x++;
			}
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
				File logNormalMetadata1 = new File(ConfigReader.getAdenomasReleaseDir() + File.separator + 
						"spreadsheets" + File.separator + 
						"pivoted_" +  NewRDPParserFileLine.TAXA_ARRAY[x] + 
						"LogNormalWithMetadata.arff");
				
				HashMap<String, Integer> map1 = 
						getNumericAttributes(logNormalMetadata1);
				
				File logNormalMetadata2 = new File(ConfigReader.getTopeSep2015Dir() + File.separator +
						"spreadsheets" + File.separator + 
						NewRDPParserFileLine.TAXA_ARRAY[x]
								+ "asColumnsLogNormalPlusMetadataFilteredCaseControl.arff");
				
				HashMap<String, Integer> map2 = 
						getNumericAttributes(logNormalMetadata2);
				
				for(String s : map2.keySet())
					System.out.println(s + " " + map2.get(s));
				
				
		}
	}
}
