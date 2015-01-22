package scripts.ratSach2.rdpAnalysis.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class ReCheckMetadata
{
	public static void main(String[] args) throws Exception
	{
		File topDir = new File(ConfigReader.getRachSachReanalysisDir() + File.separator + 
					"rdpAnalysis" );
		
		for(String s : topDir.list())
		{
			if( s.toLowerCase().indexOf("metadata") != -1)
			{
				File f=  new File(topDir.getAbsolutePath() + File.separator + s);
				verifyAFile(f.getAbsolutePath());
			}
		}
		
		System.out.println("Global pass");
	}
	
	private static void verifyAFile(String filepath) throws Exception
	{
		HashMap<String, String> animalToCageMap = getAnimalToCageMap();
		HashMap<String, Holder> metaMap = parseMetadataFile();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			String line = splits[1].replaceAll("\"", "");
			String id = splits[3].replaceAll("\"", "");
			String tissue = splits[2].replaceAll("\"", "");
			String cage = splits[4].replaceAll("\"", "");
			
			Holder h = metaMap.get(key);
			
			if( ! h.tissue.equals(tissue))
				throw new Exception("No");
			
			if( ! h.animal.equals(id))
				throw new Exception("No");
			
			if( ! h.line.equals(line))
				throw new Exception("No");
			
			String expectedCage = animalToCageMap.get(id);
			
			if( expectedCage == null && ! cage.equals("null") )
				throw new Exception("No");
			else if ( expectedCage != null &&  ! expectedCage.equals(cage))
				throw new Exception("No " + expectedCage + " " + cage);
		}
		
		reader.close();
		System.out.println("Pass " + filepath);
	}
	
	private static class Holder
	{
		String animal;
		String line;
		String tissue;
	}
	
	private static HashMap<String, String> getAnimalToCageMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getRachSachReanalysisDir()+
		File.separator + "TTULyteCages.txt")));
		
		reader.readLine();
		
		for(String s=  reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			String animal = splits[1].replaceAll("\"", "");
			String cage = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(animal))
				throw new Exception("No");
			
			map.put(animal, cage);
		}
		reader.close();
		
		return map;
	}
	
	private static HashMap<String, Holder> parseMetadataFile() throws Exception
	{
		HashMap<String, Holder> map = new HashMap<String, ReCheckMetadata.Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getRachSachReanalysisDir() + 
		File.separator + "Dess150_05262014_try02_mapping NKD.txt")));
		
		reader.readLine(); 
		
		for( String s= reader.readLine(); s != null; s = reader.readLine() )
		{
			String[] splits =s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			if( map.containsKey(key))
				throw new Exception("No");
			
			Holder h = new Holder();
			h.animal = splits[9].replaceAll("\"", "");
			h.line = splits[7].replaceAll("\"", "");
			h.tissue = splits[4].replaceAll("\"", "");
			
			map.put(key,h);
		}
		
		reader.close();
				
		return map;
	}
}
