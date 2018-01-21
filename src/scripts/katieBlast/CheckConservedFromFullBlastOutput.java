package scripts.katieBlast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class CheckConservedFromFullBlastOutput
{
	public static final String[] EXPECTED_CONSERVED = 
		{
				"F214",
				"R223",
				"S344",
				"G345",
				"F405",
				"C503",
				"E505",
				"H536",
				"F537",
				"E637",
				"G873"
		};
	
	static int getNumberConserved(HashMap<Integer, Character> map) throws Exception
	{
		int val =0;
		for( String s : EXPECTED_CONSERVED)
		{
			char c = s.charAt(0);
			
			int position = Integer.parseInt(s.substring(1)) - 1;
			
			if( map.get(position) != null && map.get(position) == c) 
				val++;
		}
		
		return val;
	}
	
	static String getConservationString(HashMap<Integer, Character> map) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for( String s : EXPECTED_CONSERVED)
		{
			char c = s.charAt(0);
			
			int position = Integer.parseInt(s.substring(1)) - 1;
			
			if( map.get(position) != null ) 
				buff.append( "" +  c + (position+1) + map.get(position) + " " );
			else
				buff.append( "" +  s.charAt(0) + (position+1) + "-" + " " );
		}
		
		return buff.toString().trim();
	}
	
	
	public static void main(String[] args) throws Exception
	{
		File blastResultsFile = new File(ConfigReader.getKatieBlastDir() + File.separator + "2YAJToGutFull.txt");
		
		HashMap<String, Holder> map = 
		getMap(blastResultsFile);
		
		System.out.println("Got map size " + map.size());
		
		HashMap<Integer, Character> innerMap = map.get("SRS011239.206985-T1-C").map;
			
		for( Integer i : innerMap.keySet())
				System.out.println("\t\t" + i + " " + innerMap.get(i));
		
		HashSet<String> found = new LinkedHashSet<>();
		HashSet<String> allblastIDS = new LinkedHashSet<>();
		
		for( String target : map.keySet())
		{
			if( CheckConserved.isConserved(map.get(target).map) )
				found.add(target);
			
			allblastIDS.add(target);
		}
		
		System.out.println(found.size());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKatieBlastDir() +
				File.separator + "matchingFromLocalAlignment.txt")));
		
		HashMap<String, String> allBlastSeqs = new LinkedHashMap<>();
		
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(ConfigReader.getKatieBlastDir() + File.separator
				+ 	"all.fst");
		
		for(FastaSequence fs = fsoat.getNextSequence(); fs != null; fs= fsoat.getNextSequence())
		{
			if( found.contains(fs.getFirstTokenOfHeader()))
			{
				writer.write(fs.getHeader() + "\n");
				writer.write(fs.getSequence() + "\n");
			}
			
			if( allblastIDS.contains(fs.getFirstTokenOfHeader()))
				allBlastSeqs.put(fs.getFirstTokenOfHeader(), fs.getSequence());
		}
		
		System.out.println(allBlastSeqs.size() + " unique ");
		writer.flush();  writer.close();
		
		writeAnnotatedSummary(map, allBlastSeqs, blastResultsFile);
	}
	
	private static void writeAnnotatedSummary(HashMap<String, Holder> map,
			HashMap<String, String> allBlastSeqs,
				File blastResultsFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(blastResultsFile));
		
		while( ! reader.readLine().startsWith("Sequences producing"))
			;
		
		reader.readLine();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getKatieBlastDir() + File.separator + "annotatedBlastHits.txt"	)));
		
		writer.write("target\tbitScore\teScore\tnumMatching\tisUnique\tsubstitutionString\nsequence\n");
		
		HashSet<String> seqs = new HashSet<>();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			String target = sToken.nextToken();
			double bitScore = Double.parseDouble(sToken.nextToken());
			double eScore = Double.parseDouble(sToken.nextToken());
			
			if( eScore < 10e-10)
			{
				Holder h = map.get(target);
				
				if( h != null)
				{

					writer.write(target + "\t" + bitScore + "\t" + eScore);
					
					String targetSeq = allBlastSeqs.get(target);
					
					int numMatch = getNumberConserved(h.map);
					
					writer.write("\t" + numMatch);
					
					if( targetSeq == null)
						throw new Exception("No " + target);
					
					writer.write( "\t" +  (!seqs.contains(targetSeq)) + "\t" +
							getConservationString(h.map) + "\t" + targetSeq + "\n");
					writer.flush();
					
					seqs.add(targetSeq);
				}
				else
				{
					System.out.println("Warning could not find holder for " + target + " " + bitScore + " " + eScore);
				}
			}
		}
			
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static class Holder
	{
		 HashMap<Integer,Character> map = new HashMap<Integer,Character>();
	}
	
	/*
	 * Outer key is targetID
	 */
	private static HashMap<String, Holder> getMap(File blastFile) throws Exception
	{
		HashMap<String,Holder> outerMap = new LinkedHashMap<>();
		
		String refSeq = FastaSequence.readFastaFile(ConfigReader.getKatieBlastDir() + File.separator + 
				"2YAJ.txt").get(0).getSequence();
		
		BufferedReader reader = new BufferedReader(new FileReader(blastFile));
		
		int numScanned =0;
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.startsWith(">"))
			{
				String target = s.substring(1).trim();
				
				Holder h  = outerMap.get(target);
				
				if( h != null)
					throw new Exception("Duplicate");
				
				h = new Holder();
				outerMap.put(target, h);
				
				System.out.println(target);
			
				for( int x=0;x  < 5; x++)
					reader.readLine();
				
				boolean keepGoing = true;
				
				while(keepGoing)
				{

					String queryLine = reader.readLine();
					
					if( queryLine.trim().length() == 0 )
					{
						keepGoing = false;
					}
					else
					{
						if( ! queryLine.startsWith("Query"))
							throw new Exception("No " + queryLine);
						
						StringTokenizer sToken = new StringTokenizer(queryLine);
						
						sToken.nextToken();
						
						int queryStartPos = Integer.parseInt(sToken.nextToken());
						
						String queryString = sToken.nextToken().trim();
						
						reader.readLine();
						
						String targetLine = reader.readLine();
						StringTokenizer targetToken = new StringTokenizer(targetLine);
						targetToken.nextToken(); targetToken.nextToken();
						String targetString = targetToken.nextToken();
						
						if( ! targetLine.startsWith("Sbjct"))
							throw new Exception("No");
					
						
						int index =0;
						for( int x=0; x < queryString.length(); x++)
						{
							char queryC = queryString.charAt(x);
							char targetC = targetString.charAt(x);
							
							if( queryC != '-')
							{
								if( refSeq.charAt(queryStartPos + index-1) != queryC)
									throw new Exception("No");
								
								Character mapVal = h.map.get(queryStartPos + index);
								
								if( mapVal == null)
								{
									h.map.put(queryStartPos + index-1, targetC);
								}
								else
								{
									if( !mapVal.equals(targetC))
										throw new Exception("No");
								}
								
								index++;
							}
						}
						
						
						System.out.println(queryLine + "\n" + targetLine + "\n");
						
						reader.readLine();				
					}
				}
				
			}
			
			numScanned++;
		}
		
		System.out.println("Scanned " + numScanned);
		return outerMap;
	}
}
