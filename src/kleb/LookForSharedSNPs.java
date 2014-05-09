package kleb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class LookForSharedSNPs
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, FastaSequence> sequenceMap =  getAsMap();
		HashMap<String, SNPEvent> snpMap = new HashMap<String, SNPEvent>();
		
		List<String> aList = new ArrayList<String>(sequenceMap.keySet());
		Collections.sort(aList);
		
		for( int x=0; x < aList.size()-1 ; x++)
			for(int y=x+1; y < aList.size(); y++)
				{
					System.out.println(x + " " + y);
					addToSNPList(snpMap, sequenceMap.get(aList.get(x)), sequenceMap.get(aList.get(y)));
				}
		
		writeResults(snpMap);
		
	}
	
	private static void writeResults(HashMap<String, SNPEvent> snpMap  )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( ConfigReader.getKlebDir()+ 
				File.separator + "snpEvents.txt")));
		
		writer.write("position\taChar\tbChar\tnumberOfTIMEs\tpositionList\n");
		
		for(SNPEvent se : snpMap.values())
		{
			writer.write(se.position + "\t");
			writer.write(se.firstChar + "\t");
			writer.write(se.secondChar + "\t");
			writer.write(se.pairingList.size() + "\t");
			writer.write(se.pairingList + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	private static HashMap<String, FastaSequence> getAsMap() throws Exception
	{
		HashMap<String, FastaSequence> map = new LinkedHashMap<String, FastaSequence>();
		
		List<FastaSequence> list = 
				FastaSequence.readFastaFile(ConfigReader.getKlebDir() +File.separator + 
						"Klebs.mfa");
		
		for(FastaSequence fs : list)
		{
			String key = fs.getFirstTokenOfHeader().replace("_V1", "");
			
			if( map.containsKey(key))
				throw new Exception("Duplicate key " + key);
			
			map.put(key, fs);
		}
		
		return map;
		
	}
	
	private static void addToSNPList( HashMap<String, SNPEvent> map, FastaSequence fs1, FastaSequence fs2 ) throws Exception
	{

		String seq1 = fs1.getSequence();
		String seq2 = fs2.getSequence();
		
		if( seq1.length() != seq2.length())
			throw new Exception("No");
		
		for( int x=0; x < seq1.length(); x++)
			if( seq1.charAt(x) != seq2.charAt(x))
			{
				SNPEvent newEvent = new SNPEvent(x, seq1.charAt(x), seq2.charAt(x));
				String idString = newEvent.getIdString();
				
				SNPEvent oldEvent = map.get(idString);
				
				if( oldEvent == null)
				{
					map.put(idString, newEvent);
					oldEvent = newEvent;
				}
				
				oldEvent.pairingList.add(new Pairing(fs1.getFirstTokenOfHeader(), fs2.getFirstTokenOfHeader()));
			}
				
	}
	
	
	private static class SNPEvent
	{
		final int position;
		final char firstChar;
		final char secondChar;
		private List<Pairing> pairingList = new ArrayList<Pairing>();
		
		private String getIdString()
		{
			return position + "_" + firstChar + "_" + secondChar;
		}
		
		public SNPEvent(int position, char firstChar, char secondChar)
			throws Exception
		{
			if( firstChar ==secondChar)
				throw new Exception("Logic error");
			
			this.position = position;
			
			if( firstChar < secondChar)
			{
				this.firstChar = firstChar;
				this.secondChar = secondChar;
			}
			else
			{
				this.firstChar = secondChar;
				this.secondChar = firstChar;
			}
			
		}
	}
	
	
	
	private static class Pairing
	{
		final String firstGenomeID;
		final String secondGenokeID;
		
		Pairing(String id1, String id2) throws Exception
		{
			if( id1.equals(id2))
				throw new Exception("Logic error");
			
			if( id1.compareTo(id2) < 0)
			{
				this.firstGenomeID = id1;
				this.secondGenokeID = id2;
			}
			else
			{
				this.firstGenomeID = id2;
				this.secondGenokeID= id1;
			}
		}
		
		@Override
		public String toString()
		{
			return firstGenomeID + "_" + secondGenokeID;
		}
	}
}
