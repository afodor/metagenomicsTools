package scripts.katieBlast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class CheckConservedFromFullBlastOutput
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<Integer,Character>> map = 
		getMap(new File(ConfigReader.getKatieBlastDir() + File.separator + "2YAJToGutFull.txt"));
		
		System.out.println("Got map size " + map.size());
		
		HashMap<Integer, Character> innerMap = map.get("SRS011239.206985-T1-C");
			
		for( Integer i : innerMap.keySet())
				System.out.println("\t\t" + i + " " + innerMap.get(i));
		
		int numFound =0;
		
		for( String target : map.keySet())
		{
			if( CheckConserved.isConserved(map.get(target)) )
				numFound++;
		}
		
		System.out.println(numFound);
	}
	
	/*
	 * Outer key is targetID
	 */
	private static HashMap<String, HashMap<Integer,Character>> getMap(File blastFile) throws Exception
	{
		HashMap<String, HashMap<Integer,Character>> outerMap = new HashMap<>();
		
		String refSeq = FastaSequence.readFastaFile(ConfigReader.getKatieBlastDir() + File.separator + 
				"2YAJ.txt").get(0).getSequence();
		
		BufferedReader reader = new BufferedReader(new FileReader(blastFile));
		
		int numScanned =0;
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.startsWith(">"))
			{
				String target = s.substring(1).trim();
				
				HashMap<Integer, Character> innerMap = outerMap.get(target);
				
				if( innerMap == null)
				{
					innerMap = new LinkedHashMap<>();
					outerMap.put(target, innerMap);
				}
				
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
								
								Character mapVal = innerMap.get(queryStartPos + index);
								
								if( mapVal == null)
								{
									innerMap.put(queryStartPos + index-1, targetC);
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
