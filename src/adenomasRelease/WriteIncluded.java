package adenomasRelease;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import utils.ConfigReader;

public class WriteIncluded
{
	public static void main(String[] args) throws Exception
	{
		List<File> fileList =  getOriginalFiles();
		
		for(File f : fileList)
			System.out.println(f.getAbsolutePath());
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getAdenomasReleaseDir() + File.separator + 
				"caseControlTwoColumn.txt")));
		
		reader.readLine();
		
		HashSet<String> set = new HashSet<String>();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0];
			
			if( set.contains(key))
				throw new Exception("No");
			
			set.add(key);
			
			File sourceFile = findExactlyOne(fileList, splits[0]);
			
			File destinationFile = new File(ConfigReader.getAdenomasReleaseDir() + 
					File.separator + "included" + File.separator + key + ".gz");
			
			copyFile(sourceFile, destinationFile);
		}
		
		System.out.println(set.size());
		reader.close();
	}
	
	//http://stackoverflow.com/questions/106770/standard-concise-way-to-copy-a-file-in-java
	public static void copyFile(File sourceFile, File destFile) throws Exception {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	private static File findExactlyOne(List<File> fileList,String s ) throws Exception
	{
		File returnVal = null;
		
		for( File f : fileList)
		{
			if( f.getName().indexOf(s) != -1)
			{
				if( returnVal != null)
					throw new Exception("Duplicate");
				
				returnVal = f;
			}
		}
		
		if( returnVal == null)
			throw new Exception("Could not find " + s);
		
		return returnVal;
	}
	
	private static List<File> getOriginalFiles() throws Exception
	{
		List<File> list = new ArrayList<File>();
		
		File originalDir = new File(ConfigReader.getAdenomasReleaseDir() + File.separator +
				"OriginalFiles");
		
		String[] names = originalDir.list();
		
		for(String s : names)
		{
			if( s.endsWith(".gz"))
			{
				list.add(new File(originalDir.getAbsolutePath() + File.separator + s));
			}
		}
		
		return list;
	}
}
