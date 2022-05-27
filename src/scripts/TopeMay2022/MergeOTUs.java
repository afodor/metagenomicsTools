package scripts.TopeMay2022;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import parsers.OtuWrapper;

public class MergeOTUs
{
	public static void main(String[] args) throws Exception
	{
		File file4 = new File("C:\\topeMayData\\042822_Samples.txt");
		File file5 = new File("C:\\topeMayData\\0522_FF_OTUs.txt");
		
		File newFile4 = new File("C:\\topeMayData\\042822_SamplesGenusName.txt");
		File newFile5 = new File("C:\\\\topeMayData\\\\0522_FF_OTUsSamplesGenusName.txt");
		
		writeToGenus(file4, newFile4);
		writeToGenus(file5, newFile5);
		
		//OtuWrapper wrapper04 = new OtuWrapper(file4);
		OtuWrapper wrapper05 = new OtuWrapper(file5);
		
		File mergedFile = new File("C:\\\\topeMayData\\\\merged4_5.txt");
		OtuWrapper.merge(newFile4, newFile5, mergedFile);
		
		for(String s : wrapper05.getSampleNames())
		{
			
			StringTokenizer sToken = new StringTokenizer(s,"-");
			
			String matchString = sToken.nextToken() + "-" + sToken.nextToken() + "-U" + sToken.nextToken() + "-F";
			System.out.println("plot (log10(myT$" + s.replaceAll("-", "\\.") + "+1), log10(myT$" + matchString.replaceAll("-", "\\.") + "+1))" );
		}
	}
	
	private static void writeToGenus( File oldFile, File newFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(oldFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
		
		String topLine = reader.readLine();
		
		String[] splits  = topLine.split("\t");
		
		writer.write(splits[0]);
		
		
		for( int x=1; x < splits.length; x++)
			writer.write("\t" + getGenus(splits[x]));
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			writer.write(s + "\n");
		}
		
		reader.close();
		
		writer.flush();  writer.close();
	}
	
	private static String getGenus(String s)
	{
		if( s.trim().length() == 0  )
			return "blank";
		
		int index = s.indexOf("g__");
		
		if( index == -1)
			return s;
		
		String returnVal = s.substring(index + 3);

		if( returnVal.trim().length() == 0)
			return "null";
		
		return returnVal;
	}
	
}
