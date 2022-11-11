package scratch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import parsers.OtuWrapper;

public class QuickNormalize
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set = new HashSet<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("c:\\temp\\ForwardReads_DADA2.txt")));
		
		String[] firstList = reader.readLine().split("\t");
		
		for(String s : firstList)
		{
			if(set.contains(s))
				throw new Exception("Duplicate " + s);
			
			set.add(s);
		}
		
		System.out.println(set.size());
		
		
		OtuWrapper wrapper = new OtuWrapper("c:\\temp\\ForwardReads_DADA2.txt");
		
		wrapper.writeNormalizedLoggedDataToFile("c:\\temp\\ForwardReads_DADA2LogNorm.txt");
	}
}	
