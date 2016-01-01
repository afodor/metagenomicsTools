package scripts.IanNovember2015.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class TestRDPIds
{
	public static void main(String[] args) throws Exception
	{
		File topDir = new File(ConfigReader.getIanNov2015Dir() + File.separator + 
				"fastaFiles");
		
		for(String s : topDir.list())
		{
			int numPassed =0;
			List<FastaSequence> list = 
					FastaSequence.readFastaFile(topDir.getAbsolutePath() + File.separator + s);
			
			HashSet<String> set = new HashSet<String>();
			
			for(FastaSequence fs : list)
				set.add(fs.getFirstTokenOfHeader());
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getIanNov2015Dir() + File.separator + 
				"rdpOut" + File.separator + s.substring(0,s.indexOf(".")) + ".fastatoRDP.txt")));
			
			for(String s2 = reader.readLine(); s2 != null; s2= reader.readLine())
			{
				String name2 = new StringTokenizer(s2).nextToken();
				if( ! set.contains(name2))
					throw new Exception("Could not find " + name2);
				
				numPassed++;
			}
			
			reader.close();
			System.out.println("Passed " + s + " with " + numPassed);
		}
		
		System.out.println("Global pass");
	}
}
