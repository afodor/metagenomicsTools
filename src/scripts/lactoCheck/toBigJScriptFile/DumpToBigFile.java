package scripts.lactoCheck.toBigJScriptFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DumpToBigFile
{
	private static final int BYTE_LENGTH = 20;
	
	private static byte[] getPaddedString(int i) throws Exception
	{
		String s= Integer.toString(i);
		
		if( s.length() > BYTE_LENGTH)
			throw new Exception("too long");
		
		while( s.length() < BYTE_LENGTH)
			s = " " + s;
		
		return s.getBytes();
	}
	
	public static void main(String[] args) throws Exception
	{
		List<File> jpegs = getJPegs();
		
		File jsonFile = new File(
				"C:\\Users\\afodor\\git\\metagenomicsTools\\src\\scripts\\lactoCheck\\toBigJScriptFile\\lactoExample.json");
		
		if( ! jsonFile.exists())
			throw new Exception("Could not find " + jsonFile.getAbsolutePath());
		
		int last=0;
		
		List<Holder> list = new ArrayList<Holder>();
		Holder h = new Holder();
		h.f = jsonFile;
		h.start = 4 * BYTE_LENGTH + 1;
		h.stop= (int) jsonFile.length()+h.start-1;
		h.name= jsonFile.getName();
		last = h.stop;
		list.add(h);
		
		for(File f : jpegs)
		{
			h = new Holder();
			h.start = last + 1;
			h.stop =  (int)(h.start + f.length()-1);
			h.f = f;
			last = h.stop;
			h.name = f.getName();
			list.add(h);
		}
		
		File outFile = new File("C:\\lactoCheck\\rdp\\lactoBlob.bioblob");
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
				outFile));
		
		// first two numbers are positions of the json
		bos.write(getPaddedString(list.get(0).start));
		bos.write(getPaddedString(list.get(0).stop));
		
		System.out.println("LENGTH = " +getPaddedString(list.get(0).stop).length );
				
		// next two numbers are positions of the table
		bos.write(getPaddedString(0));bos.write(getPaddedString(0));
		
		
		for( Holder holder: list)
		{
			System.out.println(holder.start + " " + holder.stop + " " + holder.f.length() + " " + holder.name);
			
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(holder.f));
			
			int b = in.read();
			
			while( b != -1)
			{
				bos.write(b);
				b = in.read();
			}
			
			in.close();
		}
		
		
		bos.flush();  bos.close();
		
		System.out.println(outFile.length() + " " + last);
		
	}
	
	private static class Holder
	{
		File f;
		String name;
		int start;
		int stop;
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
