package gaStool;

import java.io.File;
import java.util.StringTokenizer;

public class RenameFiles
{
	public static void main(String[] args) throws Exception
	{
		File file = new File("/nobackup/afodor_research/datasets/gaStool");
		
		String[] files= file.list();
		
		for(String s : files)
		{
			if( s.startsWith("Run1"))
			{
				File oldFile =new File(file + File.separator + s);
				
				StringTokenizer sToken =new StringTokenizer(s,"_");
				sToken.nextToken();
				
				String read = sToken.nextToken();
				String group = sToken.nextToken().replaceAll(".fasta.gz", "");
				
				String newName = group + "_" + read + ".fasta.gz";
				
				System.out.println(oldFile .getAbsolutePath() + File.separator + " " + newName);
			}
		}
	}
}
