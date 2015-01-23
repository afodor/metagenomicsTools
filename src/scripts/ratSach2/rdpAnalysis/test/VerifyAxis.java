package scripts.ratSach2.rdpAnalysis.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;


public class VerifyAxis
{
	public static void main(String[] args) throws Exception
	{
		File topDir = new File(ConfigReader.getRachSachReanalysisDir() + File.separator + 
						"rdpAnalysis" );
			
		for(String s : topDir.list())
			{
				if( s.toLowerCase().indexOf("metadata") != -1)
				{
					File f=  new File(topDir.getAbsolutePath() + File.separator + s);
					verifyAFile(f.getAbsolutePath());
				}
			}
			
			System.out.println("Global pass");
	}

	private static void verifyAFile(String s)  throws Exception
	{
		System.out.println(s);
		String metadataFree = s.replaceAll("PlusMetadata", "").
								replaceAll("WithMetadata", "");
		
		BufferedReader metadataReader = new BufferedReader(new FileReader(new File(
				s	)));
		
		BufferedReader nonMetaReader = new BufferedReader(new FileReader(new File(
				metadataFree)));
		
		metadataReader.readLine();  nonMetaReader.readLine();
		
		for(String nonMetaS= nonMetaReader.readLine(); nonMetaS!= null; nonMetaS = nonMetaReader.readLine())
		{
			String[] metaSplits = metadataReader.readLine().split("\t");
			String[] nonMetaSplits = nonMetaS.split("\t");
			
			if(metaSplits.length - 7 != nonMetaSplits.length)
				throw new Exception("No");
		}
	}
	
}
