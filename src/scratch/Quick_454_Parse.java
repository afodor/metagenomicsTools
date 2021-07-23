package scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.FastaSequence;

public class Quick_454_Parse
{
	public static final String PRIMER_TO_TRIM = "GCCTCCCTCGCGCCATCAG";
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, BufferedWriter> outputMap = new HashMap<String, BufferedWriter>();
		HashMap<String, String> primerMap = getPrimerToSampleMap();
		
		System.out.println(primerMap.keySet());
	
		List<FastaSequence> list = FastaSequence.readFastaFile(new File("C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\uhnto.1tca454rea.fna"));
		
		int numMatches =0;
		
		for(FastaSequence fs : list)
		{
			if( fs.getSequence().length() >= 200)
			{
				boolean foundAMatch = false;
				
				for(String s : primerMap.keySet())
				{
					if( fs.getSequence().startsWith(s))
					{
						foundAMatch = true;
						String fileName = s + ".txt";
						
						BufferedWriter writer = outputMap.get(fileName);
						
						if( writer == null)
						{
							writer = new BufferedWriter(new FileWriter(
									"C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\demultiplexed\\" + fileName));
							
							outputMap.put(fileName, writer);
						}
						
						writer.write(fs.getHeader() + "\n");
						writer.write(fs.getSequence().replace(s, "") + "\n");
						//System.out.println(fs.getSequence());
					}
						
				}
				
				if( foundAMatch )
					numMatches++;
			}
		}
			
		System.out.println( numMatches + " " +  list.size() + " " + ((float)numMatches / list.size()) ); 
		
		for( BufferedWriter w : outputMap.values())
		{
			w.flush();  w.close();
		}
		
	}
	
	private static HashMap<String, String> getPrimerToSampleMap() throws Exception
	{
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\wolfgangDonaldsonCombinedDir\\seqs_May_2021\\2010_08_20_454_run_keys.txt")));
		
		reader.readLine();
		
		for(String s=reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 4)
				throw new Exception("No");
			
			String primer = splits[1];
			
			if( ! primer.startsWith(PRIMER_TO_TRIM))
				throw new Exception("No " + primer);
			
			primer = primer.replace(PRIMER_TO_TRIM, "");
			
			map.put(primer, splits[3]);
		}
		
		HashSet<String> primers =new HashSet<String>();
		
		for(String s : map.keySet())
		{
			if( primers.contains(s))
				throw new Exception("No");
			
			primers.add(s);
			
		}
		
		return map;
	}
	
	private static String reverseTranscribe(String s) throws Exception
	{
		StringBuffer buff = new StringBuffer(s);
		s=buff.reverse().toString();
		
		StringBuffer s2 =new StringBuffer();
		
		for( int x=0; x < s.length(); x++)
		{
			char c = s.charAt(x);
			
			if( c == 'A')
				s2.append("T");
			else if ( c== 'T')
				s2.append("A");
			else if ( c== 'G')
				s2.append("C");
			else if ( c== 'C')
				s2.append("G");
			else throw new Exception("No");
		}
		
		return s2.toString();
	}
}
