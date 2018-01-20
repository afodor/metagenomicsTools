package scripts.katieBlast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.FastaSequence;
import utils.ConfigReader;

public class CheckConservedFromFullBlastOutput
{
	public static void main(String[] args) throws Exception
	{
		getMap(new File(ConfigReader.getKatieBlastDir() + File.separator + "2YAJToGutFull.txt"));
	}
	
	/*
	 * Outer key is targetID
	 */
	private static HashMap<String, HashMap<Integer,Character>> getMap(File blastFile) throws Exception
	{
		String refSeq = FastaSequence.readFastaFile(ConfigReader.getKatieBlastDir() + File.separator + 
				"2YAJ.txt").get(0).getSequence();
		
		BufferedReader reader = new BufferedReader(new FileReader(blastFile));
		
		int numScanned =0;
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.startsWith(">"))
			{
				String target = s.substring(1).trim();
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
						
						int startPos = Integer.parseInt(sToken.nextToken());
						
						String queryString = sToken.nextToken().trim();
						
						int index =0;
						for( int x=0; x < queryString.length(); x++)
						{
							char c = queryString.charAt(x);
							
							if( c != '-')
							{
								if( refSeq.charAt(startPos + index-1) != c)
									throw new Exception("No");
								
								index++;
							}
						}
						
						reader.readLine();
						
						String targetLine = reader.readLine();
						
						if( ! targetLine.startsWith("Sbjct"))
							throw new Exception("No");
						
						System.out.println(queryLine + "\n" + targetLine + "\n");
						
						reader.readLine();				
					}
				}
				
			}
			
			numScanned++;
		}
		
		System.out.println("Scanned " + numScanned);
		return null;
	}
}
