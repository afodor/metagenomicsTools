package scripts.lactoCheck.OtuPick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class WriteOTUFastQ
{
	public static void main(String[] args) throws Exception
	{
		writeForAGenus("Escherichia/Shigella", "Escherichia_Shigella");
	}
	
	public static void writeForAGenus(String genusString, String cleanString) throws Exception
	{
		
		HashMap<String, String> sequenceToSampleMap = getSequenceToSampleMap(genusString);
		
		System.out.println(sequenceToSampleMap.size());
		
		File outDirectory = new File( ConfigReader.getLactoCheckDir() + File.separator +
						cleanString);
		
		outDirectory.mkdir();
		
		BufferedReader reader = 
		new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( ConfigReader.getLactoCheckDir() + File.separator + "fastq" + File.separator + 
						"Run1-Sample-Name-1_S1_L001_R1_001.fastq.gz") ) ));
		
		HashMap<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
		
		int numScreened =0;
		int numHits =0;
		for(FastQ fastq = FastQ.readOneOrNull(reader); fastq != null; fastq = FastQ.readOneOrNull(reader))
		{
			numScreened++;
			String key = fastq.getFirstTokenOfHeader();
			
			String sample = sequenceToSampleMap.get(key);
			
			if( sample != null)
			{
				numHits++;
				BufferedWriter writer = writerMap.get(key);
				
				if( writer == null)
				{
					writer = new BufferedWriter(new FileWriter(new File(outDirectory.getAbsolutePath() + 
							File.separator + sample.replaceAll("/", "_") +".fastq")));
				}
				
				fastq.writeToFile(writer);
			}
			
			if( numScreened % 10000 == 0 )
				System.out.println(numScreened + " " + numHits);
		}
		
		for(BufferedWriter writer : writerMap.values())
		{
			writer.flush();  writer.close();
		}
		
		System.out.println("Finished");
		
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
								map.put("@" + rdpLine.getSequenceId(),sample);
							}
						}
						
					}
				}
			}
		}
		
		return map;
	}
}
