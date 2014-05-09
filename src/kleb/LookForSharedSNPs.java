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
					addToSNPMap(snpMap, sequenceMap.get(aList.get(x)), sequenceMap.get(aList.get(y)));
				}
		
		writePivotedByEvents(snpMap);
		
		ColumnHolder[] counts = getColumnCounts(new ArrayList<FastaSequence>(sequenceMap.values()));
		System.out.println("Got counts");
		HashMap<String, Integer> distanceMap = getDistanceMap(sequenceMap);
		System.out.println("Got distance");
		writeSnpVsAlleleFrequency(snpMap, distanceMap, counts);
	}
	
	private static void writePivotedByEvents(HashMap<String, SNPEvent> snpMap  )
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
	
	private static class ColumnHolder
	{
		int numA=0;
		int numC=0;
		int numG=0;
		int numT=0;
		
		
		private int getNum(char c) throws Exception
		{
			if( c=='A')
				return numA;
			else if ( c == 'C')
				return numC;
			else if (c == 'G')
				return numG;
			else if ( c=='T')
				return numT;
			else throw new Exception("Parsing error " + c);
		}
	}
	
	private static void writeSnpVsAlleleFrequency( HashMap<String, SNPEvent> snpEventMap, 
			HashMap<String, Integer> distanceMap, ColumnHolder[] columnCounts)
				throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKlebDir() +
				File.separator + "snpVsAlleleFrequency.txt")));
		
		writer.write("genome1\tgenome2\tcharIn1\tcharIn2\toverallDistance\tnumSharedWith1\tnumSharedWith2\tsnpEventNumber\n");
		
		int numDone =0;
		for( SNPEvent se : snpEventMap.values() )
		{
			for( Pairing pair : se.pairingList )
			{
				writer.write(pair.firstGenomeID + "\t");
				writer.write(pair.secondGenokeID + "\t");
				writer.write(pair.firstGenomeChar + "\t");
				writer.write(pair.secondGenomeChar + "\t");
				writer.write(distanceMap.get(pair.firstGenomeID + "_" + pair.secondGenokeID) + "\t");
				writer.write(columnCounts[se.position].getNum(pair.firstGenomeChar) + "\t");
				writer.write(columnCounts[se.position].getNum(pair.secondGenomeChar) + "\t");
				writer.write( se.pairingList.size() + "\n" );
				
			}
			
			if(++numDone % 1000 == 0 )
				System.out.println("Wrote " + numDone);
		}
		
		writer.flush();  writer.close();
	}
	
	
	
	private static ColumnHolder[] getColumnCounts( List<FastaSequence> seqList) throws Exception
	{
		ColumnHolder[] holders = new ColumnHolder[seqList.get(0).getSequence().length()];
		
		for( int x=0; x < holders.length; x++)
			holders[x] = new ColumnHolder();
		
		for(FastaSequence fs : seqList)
		{
			String seq = fs.getSequence();
			
			if( seq.length() != holders.length)
				throw new Exception("Parsing error");
			
			for(int x=0; x < seq.length(); x++)
			{
				char c = seq.charAt(x);
				
				if( c == 'A')
					holders[x].numA++;
				else if ( c== 'C')
					holders[x].numC++;
				else if ( c == 'G')
					holders[x].numG++;
				else if  (c=='T')
					holders[x].numT++;
				else throw new Exception("Parsing error");
			}
		}
		
		return holders;
		
	}
	
	private static HashMap<String, Integer> getDistanceMap(HashMap<String, FastaSequence> sequenceMap )
		throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		List<String> fastaSequenceNames =new ArrayList<String>(sequenceMap.keySet());
		Collections.sort(fastaSequenceNames);
		
		for( int x=0; x < fastaSequenceNames.size() -1; x++ )
		{
			System.out.println("Distance " + x);
			for(int y=0; y < fastaSequenceNames.size() -1; y++)
			{
				int numDifferent = QuickSnpDistance.getNumDifferent(sequenceMap.get(fastaSequenceNames.get(x)), 
						sequenceMap.get(fastaSequenceNames.get(y)));
				
				String key = fastaSequenceNames.get(x) + "_" + fastaSequenceNames.get(y);
				map.put(key, numDifferent);
			}
		}
			
		
		return map;
	}
	
	private static void addToSNPMap( HashMap<String, SNPEvent> map, FastaSequence fs1, FastaSequence fs2 ) throws Exception
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
				
				oldEvent.pairingList.add(new Pairing(
						fs1.getFirstTokenOfHeader(), fs2.getFirstTokenOfHeader(),seq1.charAt(x), seq2.charAt(x)));
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
		final char firstGenomeChar;
		final char secondGenomeChar;
		
		Pairing(String id1, String id2, char firstChar, char secondChar) throws Exception
		{
			if( id1.equals(id2))
				throw new Exception("Logic error");
			
			if( id1.compareTo(id2) < 0)
			{
				this.firstGenomeID = id1;
				this.secondGenokeID = id2;
				this.firstGenomeChar = firstChar;
				this.secondGenomeChar = secondChar;
			}
			else
			{
				this.firstGenomeID = id2;
				this.secondGenokeID= id1;
				this.firstGenomeChar = secondChar;
				this.secondGenomeChar = firstChar;
			}
		}
		
		@Override
		public String toString()
		{
			return firstGenomeID + "_" + secondGenokeID;
		}
	}
}
