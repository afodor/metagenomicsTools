package mbqc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class CheckAllSampleNames
{
	
	public static void main(String[] args) throws Exception
	{
		List<File> fileList = getAllSamples();
		
		for(File f : fileList)
			System.out.println(f.getAbsolutePath());
	}
	
	private static List<File> getAllSamples() throws Exception
	{
		List<File> list = new ArrayList<File>();
		
		File topDir =new File(ConfigReader.getMbqcDir() + File.separator +"bioinformatics_distribution");
		
		for(String s : topDir.list())
		{
			File labDir =new File(topDir.getAbsolutePath() + File.separator + s);
			
			if(labDir.isDirectory())
			{
				File r1Dir = new File(labDir.getAbsolutePath() + File.separator + "R1");
				
				if( ! r1Dir.exists())
					throw new Exception("No");
				
				for(String s1 : r1Dir.list())
					list.add(new File(r1Dir.getAbsolutePath() + File.separator + s1));
				
				File r2Dir = new File(labDir.getAbsolutePath() + File.separator + "R2");
				
				if( r2Dir.exists())
				{
					for(String s1 : r2Dir.list())
						list.add(new File(r2Dir.getAbsolutePath() + File.separator + s1));
					
				}
			}
		}
		
		return list;
	}
	
}
