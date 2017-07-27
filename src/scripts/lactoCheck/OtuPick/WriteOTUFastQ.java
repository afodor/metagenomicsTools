package scripts.lactoCheck.OtuPick;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteOTUFastQ
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> sequenceToSampleMap = getSequenceToSampleMap("Escherichia/Shigella");
		
		System.out.println(sequenceToSampleMap.size());
		
	}
	
	private static HashMap<String, String> getSequenceToSampleMap(String genusString) throws Exception
	{
		HashMap<String, String> map = new HashMap<String,String>();
		
		File rdpDirectory = new File( ConfigReader.getLactoCheckDir() + File.separator + "rdpResults");
		
		for(String s : rdpDirectory.list())
		{
			if( s.endsWith("txt.gz"))
			{
				String[] splits = s.split("_");
				
				if( splits[1].equals("Run1") && splits[2].equals("R1") )
				{
					String sample = splits[3].replace(".txt.gz", "");
					
					if( sample.startsWith("G") || sample.startsWith("neg"))
					{
						File rdpFile = new File(
								rdpDirectory.getAbsolutePath() + File.separator+ s );
						
						List<NewRDPParserFileLine> rdpList = NewRDPParserFileLine.getRdpListSingleThread(rdpFile);
						System.out.println(rdpFile.getAbsolutePath() + " " + map.size()	);
						
						for(NewRDPParserFileLine rdpLine : rdpList)
						{
							NewRDPNode node = rdpLine.getTaxaMap().get(NewRDPParserFileLine.GENUS);
							
							if( node != null && node.getTaxaName().equals(genusString))
							{
								map.put(rdpLine.getSequenceId(),sample);
							}
						}
						
					}
				}
			}
		}
		
		return map;
	}
}
