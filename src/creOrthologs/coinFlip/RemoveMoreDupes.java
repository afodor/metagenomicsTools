package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

import utils.ConfigReader;

public class RemoveMoreDupes
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(
						ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
								File.separator + "nonRedundantPValsVsCons_ResVsSuc.txt"	)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator + "nonRedundantPValsVsCons_ResVsSucNoDupes.txt"	)));
		
		writer.write(reader.readLine() + "\n");
		
		HashSet<String> set = new HashSet<String>();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0] + splits[4] + splits[5];
			
			if( ! set.contains(key))
			{
				set.add(key);
				writer.write(s + "\n");
			}
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
