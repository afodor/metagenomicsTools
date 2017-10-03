package scripts.lactoCheck.toBigJScriptFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DumpToBigFile
{
	public static void main(String[] args) throws Exception
	{
		List<File> jpegs = getJPegs();
		
		for(File f : jpegs)
			System.out.println(f.getAbsolutePath() + " " + f.length());
		
		File jsonFile = new File(
				"C:\\Users\\afodor\\git\\metagenomicsTools\\src\\scripts\\lactoCheck\\toBigJScriptFile\\lactoExample.json");
		
	
		int start =1;
		
		List<Holder> list = new ArrayList<Holder>();
		Holder h = new Holder();
		h.start = start;
		h.stop= start + jsonFile.length();
	}
	
	private static class Holder
	{
		String name;
		long start;
		long stop;
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
