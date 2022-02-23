package scripts.hansenAlleleData_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class CollectDuplicates
{
	public static HashMap<String, Integer> getCounts(File inFile) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[1] + "@"+  Long.parseLong(splits[2]);
			
			Integer val = map.get(key);
			
			if( val == null)
				val =0;
			
			val++;
			
			map.put(key,val);
		}
		
		return map;
	}
	
	private static void addAlleleDepthToOutput(HashMap<String, Integer> map, File inFile, File outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write(reader.readLine() + "\talleleDepth\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[1] + "@" + Long.parseLong(splits[2]);
			writer.write(s + "\t" + map.get(key) + "\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		File inFile = new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\ErectalepValuesPlusAnnotation.txt");
		
		HashMap<String, Integer> map = getCounts(inFile);
		
	//	for(String s : map.keySet())
		//	if( map.get(s) > 1)
			//	System.out.println(s + " " + map.get(s));
		
		addAlleleDepthToOutput(map, inFile, 
				new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\ErectalepValuesPlusAnnotationPlusAlleleDepth.txt"));
		
	

		inFile = new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\eColi101_pValuesPlusAnnonation.txt");
		
		map = getCounts(inFile);
		
	//	for(String s : map.keySet())
		//	if( map.get(s) > 1)
			//	System.out.println(s + " " + map.get(s));
		
		addAlleleDepthToOutput(map, inFile, 
				new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\eColi101_pValuesPlusAnnonationPlusAlleleDepth.txt"));
	
	}
}
