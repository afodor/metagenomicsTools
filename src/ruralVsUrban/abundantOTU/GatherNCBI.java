package ruralVsUrban.abundantOTU;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class GatherNCBI
{
	public static void main(String[] args) throws Exception
	{
		File topDir = new File("/projects/afodor_research/ncbi");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				topDir.getAbsolutePath() + File.separator + "all16S.fasta"
				)));
		
		String[] list = topDir.list();
		
		
		
		writer.flush();  writer.close();
	}
}
