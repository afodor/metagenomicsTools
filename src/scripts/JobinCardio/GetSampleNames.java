package scripts.JobinCardio;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.ConfigReader;

public class GetSampleNames
{
	public static void main(String[] args) throws Exception
	{
		File topDir= new File(ConfigReader.getJobinCardioDir() + File.separator + 
				"MiSeq1_MDS_Jobin-7706699");
		
		List<String> list = new ArrayList<String>();
		
		for(String s : topDir.list())
		{
			File f = new File(topDir.getAbsolutePath() + File.separator + s);
			
			if( f.isDirectory())
				list.add(s);
		}
		
		Collections.sort(list);
		
		for(String s : list)
			System.out.println(s);
			
	}
}
