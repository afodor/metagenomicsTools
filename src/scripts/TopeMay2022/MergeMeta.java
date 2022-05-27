package scripts.TopeMay2022;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import parsers.OtuWrapper;

public class MergeMeta
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String,String> map = getRaceMap();
		System.out.println(map);
		
		File newFile5 = new File("C:\\topeMayData\\0522_FF_OTUsSamplesGenusName.txt");
		
		File newFile5Logged = new File("C:\\topeMayData\\0522_FF_OTUsSamplesGenusNameLogged.txt");
		
		OtuWrapper wrapper = new OtuWrapper(newFile5);
		wrapper.writeNormalizedLoggedDataToFile(newFile5Logged.getAbsoluteFile());
		
		File metaFile = new File("C:\\topeMayData\\0522_FF_OTUsSamplesGenusNameLoggedPlusMeta.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader(newFile5Logged));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(metaFile));
		
		writer.write("sample\trace");
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length; x++)
			writer.write("\t" + splits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			splits= s.split("\t");
			
			writer.write(splits[0] + "\t" + map.get(splits[0]));
			

			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
			
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static HashMap<String,String> getRaceMap() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			"C:\\topeMayData\\mapping.txt"	)));
		
		reader.readLine();
		
		for(int x=0; x < 21; x++)
		{
			String s =reader.readLine();
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("_", "-");
			
			if( map.containsKey(splits[0]))
				throw new Exception("Duplciate");
			
			map.put(key, splits[8].trim());
		}
		
		return map;
	}
}
