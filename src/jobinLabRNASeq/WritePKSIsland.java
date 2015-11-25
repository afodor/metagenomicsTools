package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class WritePKSIsland
{
	public static void main(String[] args) throws Exception
	{
		write("c:\\temp\\blah.txt", 340000, 360000, ConfigReader.getJobinLabRNASeqDir() + File.separator + "AEFA01000017genomeWithAnnotations.txt"	);
		//write("c:\\temp\\blah.txt", 632329 , 663035, ConfigReader.getJobinLabRNASeqDir() + File.separator + "AEFA01000017genomeWithAnnotations.txt"	);
		write("c:\\temp\\blah2.txt", 1, 22448, ConfigReader.getJobinLabRNASeqDir() + File.separator + "AEFA01000016genomeWithAnnotations.txt"	);
	}
	
	public static void write(String outFilePath, int start, int end, String inFilePath) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(inFilePath)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( outFilePath)));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s = reader.readLine(); 
				s != null;
					s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( Integer.parseInt(splits[2]) >= start && Integer.parseInt(splits[2]) <= end)
			{
				writer.write(s + "\n");
				StringTokenizer sToken = new StringTokenizer(splits[4]);
				StringBuffer buff = new StringBuffer();
				buff.append(sToken.nextToken());
				
				while( sToken.hasMoreTokens() )
				{
					String val = sToken.nextToken();
					
					if( val.indexOf("ECNC") == -1)
						buff.append(" " + val);
					
					set.add(buff.toString());
				}
			}
				
		}
		
		writer.flush();  writer.close();
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		
		for(String s : list)
		{
			System.out.println(s);
		}
			

	}
}
