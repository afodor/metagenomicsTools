package ruralVsUrban;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class MakeRDPThreeColumnFile
{
	public static int THRESHOLD =80;
	
	public static int getReadNumber(String s) throws Exception
	{
		int readNum = Integer.parseInt( s.charAt(s.indexOf("fq")-2) + "");
		
		if (readNum < 1 || readNum > 2)
			throw new Exception("No");
		
		return readNum;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			pivotALevel( NewRDPParserFileLine.TAXA_ARRAY[x]  );
	}
	
	private static String getSampleID(String s) 
	{
		s =s.replace(".fasta_TO_RDP.txt.gz", "");
		String[] splits = s.split("_");
		return splits[splits.length-1];
	}
	
	private static void pivotALevel(String level) throws Exception
	{
		System.out.println(level);
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getChinaDir() +
				File.separator + "threeColumn_" + level + ".txt"));
		
		File topDir = new File(ConfigReader.getChinaDir()  + File.separator + 
				"rdpResults");
		
		for(String s: topDir.list())
		{
			File rdpFile = new File(topDir.getAbsoluteFile() + File.separator + s);
			String sampleId = getSampleID(rdpFile.getName()) + "_" + getReadNumber(rdpFile.getName());
			List<NewRDPParserFileLine> rdpList = 
					NewRDPParserFileLine.getRdpListSingleThread(rdpFile);
			
			for(NewRDPParserFileLine rdp : rdpList)
			{
				NewRDPNode node = rdp.getTaxaMap().get(level);
				if( node != null && node.getScore()>= THRESHOLD)
				{
					writer.write(rdp.getSequenceId() + "\t");
					writer.write(sampleId  + "\t");
					writer.write(node.getScore() + "\n");
				}
			}
		}
		
		writer.flush();  writer.close();
	}
}
