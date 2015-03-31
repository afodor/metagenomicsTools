package scripts.JenniferTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllFastQToFastA
{
	public static void main(String[] args) throws Exception
	{
		File startDir = new File("/projects/afodor_research/jwellerTestRunRawData");
		
		List<File> list = new ArrayList<File>();
		recurse(startDir, list);
		
		for( File f : list)
		{
			if (f.getName().endsWith("fastq.gz"))
				System.out.println(f.getAbsolutePath());
		}
	}
	
	private static void recurse(File dir, List<File> list) throws Exception
	{
		for(String s : dir.list())
		{
			File f = new File(dir.getAbsolutePath() + File.separator + s);
			
			if( f.isDirectory())
				recurse(f, list);
			else
				list.add(f);
		}
	}
}
