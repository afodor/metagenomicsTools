package mbqc.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class CheckDiversities
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> divMap = getDiversities();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefiedplusMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String expected = divMap.get(splits[0]);
			
			if( expected == null)
				throw new Exception("No " + splits[0]);
			
			if( expected.equals("nan"))
			{
				if( !expected.equals(splits[10]))
					throw new Exception("No");
			}
			else
			{
				double diff = Math.abs( Double.parseDouble(expected) - Double.parseDouble(splits[10]));
				if( diff > 0.00000000000000001)
						throw new Exception("No");
			}
			

		}
		
		checkExtractions();
		simpleTokenCheck();
		checkMBQC_ID();
		System.out.println("Passed");
	}
	
	private static void simpleTokenCheck() throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefiedplusMetadata.txt")));
		
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
	
	
	static HashMap<String, String> quickMBQC_IDMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getMbqcDir() + File.separator + "dropbox" +
						File.separator + "raw_design_matrix.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine() )
		{
			String[] splits =s.split("\t");
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[3]);
		}
		
		reader.close();
		return map;
	}
	
	private static void checkMBQC_ID() throws Exception
	{
		HashMap<String, String> exMap = quickMBQC_IDMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefiedplusMetadata.txt")));
		
		reader.readLine();
		
		for( String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String ex = exMap.get(splits[0]);
			
			if( ex == null)
				throw new Exception("No");
			
			if( ! ex.equals(splits[6]))
				throw new Exception("No " + splits[6] );
		}
		
		reader.close();
		
		System.out.println("MBQC ID test passed");
	}
	
	
	private static void checkExtractions() throws Exception
	{
		HashMap<String, String> exMap = TestMDSMeta.quickExtractionMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefiedplusMetadata.txt")));
		
		reader.readLine();
		
		for( String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String ex = exMap.get(splits[0]);
			
			if( ex == null)
				throw new Exception("No");
			
			if( ! ex.equals(splits[4]))
				throw new Exception("No " + splits[4] );
		}
		
		reader.close();
		
		System.out.println("Extraction test passed");
	}
	
	private static HashMap<String, String> getDiversities() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
				 "merged-final-unrarefied.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			map.put(splits[0], splits[1]);
		}
		
		return map;
	}
}
