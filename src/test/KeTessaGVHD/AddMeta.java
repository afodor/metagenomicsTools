package test.KeTessaGVHD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class AddMeta
{
	public static void main(String[] args) throws Exception
	{
		/*
		File inFile = new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_Genus_Transposed.txt");
		
		OtuWrapper wrapper = new OtuWrapper(inFile);
		
		File logNorm = new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_Genus_TransposedLogNorm.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(logNorm);
		*/
		
		HashMap<String, String> gvnMap = getMetaGvNMap();
		
	}
	
	@SuppressWarnings("resource")
	private static HashMap<String, String> getMetaGvNMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\ke_tessa_test\\GVHDProject-main\\metaGvN.txt")));
		
		reader.readLine();
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("No");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], splits[1]);
			
		}
		
		return map;
	}
}
