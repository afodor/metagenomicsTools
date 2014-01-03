package sandraJune2012Rivers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class SandraJune2012Metadata
{
	public static HashMap<String, Integer> getBarcodeToSampleMap() throws Exception
	{
		HashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getSandraRiverJune2012Dir() + File.separator + "2011-09-10_ClintonGSMNP_454Key.txt")));
		
		reader.readLine();
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken();
			
			String barcode = new String( sToken.nextToken());
			barcode = barcode.replace("GCCTCCCTCGCGCCATCAG", "").trim();
			
			if( map.containsKey(barcode))
				throw new Exception("No");
			
			map.put(barcode, Integer.parseInt(sToken.nextToken()));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Integer> barcodeMap = getBarcodeToSampleMap();
		for(String s: barcodeMap.keySet())
		{
			System.out.println(s + " "+ barcodeMap.get(s));
		}
	}
}
