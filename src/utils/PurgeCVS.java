package utils;

import java.io.File;

public class PurgeCVS
{
	public static void purgeCVS( File file ) throws Exception
	{
		String[] list = file.list();
		
		for(String s : list)
		{
			File subFile = new File(file.getAbsolutePath() + File.separator + s);
			
			if( s.equals("CVS"))
			{
				File cvsDir = new File(file.getAbsolutePath() + File.separator + s);
				System.out.println("rmdir " + cvsDir.getAbsolutePath());
				
				deleteAllFile(cvsDir);
			}
			
			if( subFile.isDirectory())
				purgeCVS(subFile);
		}
	}
	
	private static void deleteAllFile(File dirFile) throws Exception
	{
		if( !dirFile.getName().equals("CVS"))
			throw new Exception("No");
		
		String[] list =dirFile.list();
		
		for( String s : list)
		{
			System.out.println("rm " + dirFile.getAbsolutePath()+ File.separator + s);
		}
	}
	
	
	public static void main(String[] args) throws Exception
	{
		purgeCVS( new File( "C:\\PeakStudio\\"));
	}
}
