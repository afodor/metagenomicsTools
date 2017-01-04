package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadataMergedQiimeClosedRef
{
	private static final String[] IN_QIIME = {"k", "p", "c", "o", "f", "g","s", "otu"};

	private static final String[] OUT_RDP= {"kingdom",
				"phylum", "class", "order", "family", "genus", "species", "otu"};

	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> fileSet3=  AddMetadataMergedKraken.getFileSet(3);
		HashSet<String> fileSet4 = AddMetadataMergedKraken.getFileSet(4);
		
		for( int x=0; x < IN_QIIME.length; x++)
		{
			String taxa=  IN_QIIME[x];
			System.out.println(taxa);
			
			File unloggedFile = new File(ConfigReader.getTopeOneAtATimeDir() + 
				File.separator + "qiimeSummary" + File.separator + 
				"diverticulosis_closed_" + taxa + "_AsColumns.txt");
			
			File loggedFile = new File(ConfigReader.getTopeOneAtATimeDir() + 
							File.separator + "qiimeSummary" + File.separator + 
							"diverticulosis_closed_" + OUT_RDP[x] + "_AsColumnsLogNormal.txt");
			
			OtuWrapper wrapper = new OtuWrapper( unloggedFile);
			wrapper.writeNormalizedDataToFile(loggedFile);
			
			File outFile = new File( ConfigReader.getTopeOneAtATimeDir() + 
					File.separator + "qiimeSummary" + File.separator + 
					"diverticulosis_closed_" + OUT_RDP[x] + "_AsColumnsLogNormalWithMetadata.txt");
			
			AddMetadataMergedKraken.addMetadata(wrapper, loggedFile, outFile,false, fileSet3, fileSet4);
		}
		
	}
	
	static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
			boolean fromR, HashSet<String> file3Set, HashSet<String> file4Set) throws Exception
	{
		HashMap<String, Integer> caseControlMap = AddMetadataMergedKraken.getCaseControlMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tcaseContol\tunclassified");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			Integer val = caseControlMap.get( new StringTokenizer(key, "_").nextToken());
			int readNum =  AddMetadataMergedKraken.getReadNum(key);
			
			if( readNum ==1 && val != null && (val==0 || val == 1))
			{
				writer.write(key + "\t" + val);
				
				for( int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
			}	
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
		
}
