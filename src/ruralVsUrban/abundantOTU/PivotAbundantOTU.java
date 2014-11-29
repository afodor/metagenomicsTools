package ruralVsUrban.abundantOTU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import utils.ConfigReader;

public class PivotAbundantOTU
{	
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getChinaDir() +
				File.separator + "forwardReadToSample"));
		
		HashSet<String> set = new HashSet<String>();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
			set.add(s.split("\t")[1].substring(2));
		
		for(String s: set)
			System.out.println(s);
		
		System.out.println(set.size());
		
		int num=0;
		
		for(String s: set)
			if( s.startsWith("B"))
				num++;
		
		System.out.println(num);
	}
}
