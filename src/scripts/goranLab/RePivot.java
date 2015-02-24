package scripts.goranLab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class RePivot
{
	private static void writeOnlyNonBlankFields(String inFile, String outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(inFile)));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
		
		for(String s= reader.readLine(); s != null && s.trim().length() >0; s= reader.readLine() )
		{
			String[] splits = s.split("\t");
			boolean needATab = false;
			
			for( int x=0;x  < splits.length; x++)
			{
				if( splits[x].trim().length() > 0 )
				{
					if( needATab)
						writer.write("\t");
					
					writer.write(splits[x]);
					
					needATab = true;
				}
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
		
	
	public static void main(String[] args) throws Exception
	{
		String level = "phylum";
		
		String withDupTabs = 
				ConfigReader.getGoranTrialDir() + File.separator + level + "OutNo2ndLine.txt";
		
		String withNoDupTabs = 
				ConfigReader.getGoranTrialDir() + File.separator + level + "OutNo2ndLineNoDupTabs.txt";
	
		writeOnlyNonBlankFields(withDupTabs, withNoDupTabs);
		
		File outFile = new File(ConfigReader.getGoranTrialDir() + File.separator + level+ "AsColumns.txt");
		OtuWrapper.transpose(
				withNoDupTabs,
			outFile.getAbsolutePath(), false);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getGoranTrialDir() + File.separator + level + "AsColumnsLogNorm.txt")));
		
		BufferedReader reader = new BufferedReader(new FileReader(outFile));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			writer.write( splits[0]);
			
			for( int x=1; x < splits.length; x++)
			{
				writer.write("\t" + (3 + Math.log10(Double.parseDouble(splits[x])+0.001)));
			}
			
			writer.write("\n");
		}
		
		reader.close();
		
		writer.flush();  writer.close();
		
	}
}
