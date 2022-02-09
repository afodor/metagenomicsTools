package scripts.hansenAlleleData_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class AddAnnotationsToROutput
{
	public static void main(String[] args) throws Exception
	{
		addAnnotations(new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\ErectalepValues.txt"), 
						new File("C:\\JonathanHansenAlleles\\inputTextFiles_2\\Erectale annotations.txt"), 
						new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\ErectalepValuesPlusAnnotation.txt"));
		

		addAnnotations(new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\ErectaleForR.txt"), 
						new File("C:\\JonathanHansenAlleles\\inputTextFiles_2\\Erectale annotations.txt"), 
						new File("C:\\JonathanHansenAlleles\\outputTextFiles_2\\ErectaleForRPlusAnnotation.txt"));
	}
	
	private static void addAnnotations(File rOutputFile, File annotationFile, File newOutputFile) throws Exception
	{
		HashMap<String, String> annotationMap = getAnnotationMap(annotationFile);
		
		BufferedReader reader = new BufferedReader(new FileReader(rOutputFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(newOutputFile));
		
		writer.write(reader.readLine() + "\tgeneAnnotation\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String geneID = splits[6].replaceAll("\"", "");
			
			writer.write(s + "\t" + annotationMap.get(geneID) + "\n");
		}
		
		reader.close();
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, String> getAnnotationMap(File file) throws Exception
	{
		HashMap<String, String> map = new HashMap<>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("No");
			
			if( map.containsKey(splits[1]))
				throw new Exception("No " + splits[0] + " " +  splits[1]);
			
			map.put(splits[1], splits[0]);
		}
		
		reader.close();
		
		return map;
	}
}
