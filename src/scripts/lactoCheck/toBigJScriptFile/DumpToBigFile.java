package scripts.lactoCheck.toBigJScriptFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DumpToBigFile
{
	public static void main(String[] args) throws Exception
	{
		List<File> jpegs = getJPegs();
		
		for(File f : jpegs)
			System.out.println(f.getAbsolutePath());
		
		
	}
	
	
	private static List<File> getJPegs()
	{
		List<File> list = new ArrayList<File>();
		
		File topDir =new File("C:\\lactoCheck\\rdp");
		
		for(String s : topDir.list())
			if( s.endsWith(".jpg"))
				list.add(new File(topDir.getAbsolutePath() + File.separator + s));
		
		return list;
	}
}
