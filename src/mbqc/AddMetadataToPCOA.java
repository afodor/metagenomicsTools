package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class AddMetadataToPCOA
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, RawDesignMatrixParser> metaMap = 
				RawDesignMatrixParser.getByFullId();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "dropbox" + File.separator + 
				"alpha-beta-div" + File.separator +  "beta-div" + File.separator + "attic" + 
						File.separator + "pcoa-qiime.txt")));
		
		int numFound =0;
		int numMissed=0;
		
		for(String s= reader.readLine();  s !=null; s = reader.readLine())
		{
			System.out.println(s);
			String[] splits = s.split("\t");
			
			if( metaMap.containsKey(splits[0]))
				numFound++;
			else
				numMissed++;
		}
		
		System.out.println(numFound + " " + numMissed);
	}
}
