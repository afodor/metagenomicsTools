package adenomasFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

public class FilterSequences
{
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set = getSampleNames();
		System.out.println(set);
		
		File sourceDir = new File("D:\\NinaWithDuplicates\\OriginalFiles");
		File targetDir = new File("D:\\adenomasSet");
		String[] names = sourceDir.list();
		
		for(String s : names)
		{
			if( isInSet(s, set))
			{
				File oldFile = new File( sourceDir.getAbsolutePath() + File.separator +  s );
				File newFile = new File(targetDir.getAbsolutePath() + File.separator + s);
				System.out.println(oldFile + " to " + newFile);
				copyFileUsingFileStreams(oldFile, newFile);
				
			}
		}
		
		writeKeyFile();
	}
	
	private static void writeKeyFile() throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("D:\\adenomasSet\\names.txt")));
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"D:\\MachineLearningJournalClub\\testData.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String firstString = s.split("\t")[0];
			writer.write(firstString + "\n");
		}
		
		writer.flush(); writer.close();
	}
	
	/*
	 *  from http://examples.javacodegeeks.com/core-java/io/file/4-ways-to-copy-file-in-java/
	 * 
	 */private static void copyFileUsingFileStreams(File source, File dest)
				throws IOException {
			InputStream input = null;
			OutputStream output = null;
			try {
				input = new FileInputStream(source);
				output = new FileOutputStream(dest);
				byte[] buf = new byte[1024];
				int bytesRead;
				while ((bytesRead = input.read(buf)) > 0) {
					output.write(buf, 0, bytesRead);
				}
			} finally {
				input.close();
				output.close();
			}
		}
	
	private static boolean isInSet(String s, HashSet<String> set)
	{
		for(String s2 : set)
			if( s.indexOf(s2) != -1)
				return true;
		
		return false;
	}
	
	private static HashSet<String> getSampleNames() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"D:\\MachineLearningJournalClub\\testData.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String firstString = s.split("\t")[0];
			firstString = firstString.replace("case", "").replace("control", "");
			set.add(firstString);
			
		}
		
		reader.close();
		return set;
	}
	
}
