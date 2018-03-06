package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;

public class WriteRareficationTable
{
	public static void main(String[] args) throws Exception
	{
		File fastqDir = new File(ConfigReader.getLactoCheckDir() + File.separator + 
				"fastqDemultiplexed");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLactoCheckDir() + File.separator + "readSummary.txt")));
		
		writer.write("sample\tnumReads\n");
		
		String[] files = fastqDir.list();
		
		for(String s : files)
		{
			String[] splits = s.split("_");
			
			if( splits[0].equals("Run1") && ! splits[2].startsWith("S"))
				System.out.println(s);
			
			BufferedReader reader =  new BufferedReader(new InputStreamReader( 
					new GZIPInputStream( new FileInputStream( fastqDir + File.separator + s ) ) ));
			
			long count =0;
			
			for(FastQ fq = FastQ.readOneOrNull(reader); fq != null; fq= FastQ.readOneOrNull(reader))
				count++;
			
			writer.write(s + "\t" + count + "\n");
			writer.flush();
		}
		
		writer.flush();  writer.close();
	}
}
