package adenomasRelease;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class WriteIncluded
{
	public static void main(String[] args) throws Exception
	{
		List<File> fileList =  getOriginalFiles();
		
		for(File f : fileList)
			System.out.println(f.getAbsolutePath());
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
