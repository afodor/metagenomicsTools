package javascriptFileSlicing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import scripts.lactoCheck.toBigJScriptFile.DumpToBigFile;
import scripts.lactoCheck.toBigJScriptFile.DumpToBigFile.Holder;

public class AdenomasToBioBlob
{
	public static void main(String[] args) throws Exception
	{
		List<File> jpegs = new ArrayList<File>(); //getJPegs();
		
		File jsonFile = new File(
				"C:\\Users\\afodor\\git\\afodor.github.io\\ismeJRDP_CaseControl.json");
		
		if( ! jsonFile.exists())
			throw new Exception("Could not find " + jsonFile.getAbsolutePath());
		
		int last=0;
		
		List<Holder> list = new ArrayList<Holder>();
		Holder h = new Holder();
		h.f = jsonFile;
		h.start = 4 * DumpToBigFile.BYTE_LENGTH + 1;
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
		
		String tableString = DumpToBigFile.buildTableString(list);
		
		File outFile = new File("C:\\temp\\adenomasBlob.bioblob");
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
				outFile));
		
		// first two numbers are positions of the json
		bos.write(DumpToBigFile.getPaddedString(list.get(0).start));
		bos.write(DumpToBigFile.getPaddedString(list.get(0).stop));
		
		System.out.println("LENGTH = " +DumpToBigFile.getPaddedString(list.get(0).stop).length );
				
		// next two numbers are positions of the table
		bos.write(DumpToBigFile.getPaddedString(list.get(list.size()-1).stop + 1));
		bos.write(DumpToBigFile.getPaddedString(list.get(list.size()-1).stop + 1 + tableString.length()));
		
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
		
		for( byte b: tableString.getBytes())
			bos.write(b);
		
		bos.flush();  bos.close();
		
		System.out.println(outFile.length() + " " + last);
		
	}
}
