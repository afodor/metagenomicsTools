package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import sequenceRuns.RunDescription;
import utils.FilePathGenerator;
import utils.OrderedSequenceRange;
import utils.TabReader;

/*
 *  Holds the results of blastall output with -m 8
 */
public class HitScores implements Comparable<HitScores>
{
	//# Fields: Query id, Subject id, % identity, alignment length, mismatches, gap op
	//enings, q. start, q. end, s. start, s. end, e-value, bit score
	private String queryId;
	private final String targetId;
	private final float percentIdentity;
	private final int alignmentLength;
	private final int numMismatches;
	private final int gapOpenings;
	private final int queryStart;
	private final int queryEnd;
	private final int targetStart;
	private final int targetEnd;
	private final double eScore;
	private final float bitScore;
	
	//Queryid	Subjectid	percentIdentity	alignmentlength	mismatches	gap_openings	querystart	queryend	targetstart	targetEnd	evalue	bitScore
	
	
	public static class PercentIdentitySorter implements Comparator<HitScores>
	{
		public int compare(HitScores o1, HitScores o2)
		{
			return Float.compare(o2.percentIdentity, o1.percentIdentity);
		}
	}
	
	public void setQueryId(String queryId)
	{
		this.queryId = queryId;
	}
	
	/*
	 *   The file line is the result of a blast run with the blast output set to 8
	 *   //# Fields: Query id, Subject id, % identity, alignment length, mismatches, gap op
	//enings, q. start, q. end, s. start, s. end, e-value, bit score
	 */
	public HitScores(String fileLine) throws Exception
	{
		TabReader sToken = new TabReader(fileLine);
		
		this.queryId = sToken.nextToken();
		this.targetId = sToken.nextToken();
		this.percentIdentity = Float.parseFloat(sToken.nextToken());
		this.alignmentLength = Integer.parseInt(sToken.nextToken());
		this.numMismatches = Integer.parseInt(sToken.nextToken());
		this.gapOpenings = Integer.parseInt(sToken.nextToken());
		
		this.queryStart = Integer.parseInt(sToken.nextToken());
		this.queryEnd = Integer.parseInt(sToken.nextToken());
		
		this.targetStart = Integer.parseInt(sToken.nextToken()); 
		this.targetEnd = Integer.parseInt( sToken.nextToken());
		this.eScore = Double.parseDouble(sToken.nextToken());
		this.bitScore = Float.parseFloat(sToken.nextToken());
		
		if( sToken.hasMore())
			throw new Exception("Parsing error");
	}
	
	public static HashMap<String, HitScores> 
		getTopHitsFromCacheOrCreate( RunDescription rd ) throws Exception
	{
		File resultsFile = FilePathGenerator.getTopHitsFile(rd);
		System.out.println("Looking for " + resultsFile.getAbsolutePath());
		
		if( ! resultsFile.exists() )
		{
			System.out.println("Could not find " + resultsFile.getAbsolutePath());
			HashMap<String, HitScores> returnMap = getTopHits(rd);
			writeToFile(returnMap.values(), resultsFile, true);
			return returnMap;
		}
		else
		{
			System.out.println("Found cached " + resultsFile.getAbsolutePath());
		}
			
		
		return getUniqueQueryIDMap(resultsFile);
	}
	
	public OrderedSequenceRange getTargetRange()
	{
		return new OrderedSequenceRange(this.targetStart, this.targetEnd);
	}
	
	public OrderedSequenceRange getQueryRange()
	{
		return new OrderedSequenceRange(this.queryStart, this.queryEnd);
	}
	
	/*
	 * Returns the gi identifier the gi identifier ( e.g. 119668705 ) 
	 */
	public String getTargetGenbankId()
	{
		String returnString = getTargetId();
		returnString = returnString.substring(3);
		returnString = returnString.substring(0, returnString.indexOf("|"));
		return returnString;
	}
	
	public static HashSet<String> getAllGenbankIdsForCollection( Collection<HitScores> c ) 
	{
		HashSet<String> set = new HashSet<String>();
		
		for( HitScores hs : c )
			set.add(hs.getTargetGenbankId());
		
		return set;
	}
	
	/*
	 * Throws if there any duplicate queryIDs in the results file
	 */
	public static HashMap<String, HitScores> getUniqueQueryIDMap(File fileToParse) 
					throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(fileToParse));
		
		HashMap<String, HitScores> map = new HashMap<String, HitScores>();
		
		reader.readLine();
		
		String nextLine = reader.readLine();
		
		while(nextLine != null)
		{
			if( ! nextLine.startsWith("#"))
			{
				HitScores hs = new HitScores(nextLine);
				
				if( map.get(hs.getQueryId()) != null )
					throw new Exception("Error!  Duplicate keys for " + hs.getQueryId());
				
				map.put(hs.getQueryId(), hs);
			}
			
			nextLine = reader.readLine();
		}
		
		reader.close();
		
		return map;
	}
	
	/*
	 * Writes the top hit for each query to the file defined by 
	 * FilePathGenerator.getTopHitsFile().
	 */
	public static void writeToFile( Collection<HitScores> scores, File file, boolean writeHeader)
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		if( writeHeader)
			writeHeader(writer, true);
		
		for( HitScores hs : scores )
			hs.writeALine(writer, true);
		
		writer.flush();  writer.close();
	}
	
	public static HashSet<HitScores> filter(Collection<HitScores> scores,
			double eScore, double length) throws Exception
	{
		HashSet<HitScores> returnSet = new HashSet<HitScores>();
		
		for( HitScores hs : scores )
			if( hs.getEScore() <= eScore && hs.getQueryAlignmentLength() >= length)
				returnSet.add(hs);
		
		return returnSet;
	}
		
	public static HashMap<String,HitScores> getTopHitsFilteredByPercentIdentityAtMinLength(
			String filepath, int minLength) throws Exception
	{
		//Query id, Subject id, % identity, alignment length
		
		HashMap<String, HitScores> map= new HashMap<String, HitScores>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		String nextLine = reader.readLine();
		
		int index=0;
		while( nextLine != null)
		{
			String[] splits = nextLine.split("\t");
			
			if( splits.length != 12)
				throw new Exception("Parsing error");
			
			HitScores hs = new HitScores(nextLine);
			HitScores oldHit= map.get(hs.getQueryId());
			
			if( hs.getAlignmentLength() >= minLength)
			{
				if(oldHit== null || hs.getPercentIdentity() > oldHit.getPercentIdentity() )
				{
					map.put(hs.getQueryId(), hs);
				}
			}
				
			nextLine= reader.readLine();
			
			if( index % 100000==0)
				System.out.println(index + " " + map.size());
			
			index++;
		}
		
		reader.close();
		
		return map;
	}
	
	public static HashMap<String, HitScores> getTopHitsAsQueryMap(String filepath) throws Exception
	{	
		HashMap<String, HitScores> map= new LinkedHashMap<String, HitScores>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		String nextLine = reader.readLine();
		
		int index=0;
		while( nextLine != null)
		{
			HitScores hs = new HitScores(nextLine);
			HitScores oldHit = map.get(hs.getQueryId());
			
			if( oldHit == null || hs.getBitScore()> oldHit.getBitScore())
				map.put(hs.getQueryId(), hs);
			
			nextLine= reader.readLine();
			
			if( index % 100000==0)
				System.out.println(index + " " + map.size());
			
			index++;
		}
		
		
		reader.close();
		
		return map;
	}
	
	public static List<HitScores> getTopHits(String filepath) throws Exception
	{
		return new ArrayList<HitScores>(getTopHitsAsQueryMap(filepath).values());
	}
	
	public static List<HitScores> getTopHits(List<HitScores> inHits) throws Exception
	{
		HashMap<String, HitScores> map=  new LinkedHashMap<String, HitScores>();
		List<HitScores> returnList= new ArrayList<HitScores>();
		
		for( HitScores hs : inHits)
		{
			HitScores oldScore = map.get(hs.getQueryId());
			
			if( oldScore == null || hs.getEScore() < oldScore.getEScore())
				map.put(hs.getQueryId(), hs);
		}
		
		for( String s : map.keySet() )
			returnList.add(map.get(s));
		
		return returnList;
	}
	
	public static HashMap<String, HitScores> getTopHits( RunDescription rd ) throws Exception
	{
		HashMap<String, HitScores> topHits = new HashMap<String, HitScores>();
		
		for( File f : rd.getSearchResultFiles() )
		{
			System.out.println("Starting " + f.getAbsolutePath() );
			
			BufferedReader reader = 
				rd.resultFilesGZipped() ?
					new BufferedReader(new InputStreamReader( 
							new GZIPInputStream( new FileInputStream( f) ) ))
					:
						new BufferedReader(new FileReader(f));
					
			String nextLine = reader.readLine();
			
			while( nextLine != null)
			{
				if( ! nextLine.startsWith("#"))
				{
					HitScores newHitScore = new HitScores(nextLine);
					
					HitScores oldHitScore = topHits.get(newHitScore.getQueryId());
					
					if( oldHitScore == null || newHitScore.getEScore() <  oldHitScore.getEScore())
					{
						topHits.put( newHitScore.getQueryId(), newHitScore );
					}
				}
				
				nextLine = reader.readLine();
			}
			
			reader.close();
		}
		
		return topHits;
	}
	
	public static HashSet<String> getUniqueQueryIds( Collection<HitScores> collection )
	{
		HashSet<String> returnSet = new HashSet<String>();
		
		for( HitScores hs : collection)
			returnSet.add(hs.getQueryId());
		
		return returnSet;
	}
	
	public static void writeHeader(BufferedWriter writer, boolean endWithNewline ) throws Exception
	{
		writer.write("queryId\t");
		writer.write("targetId\tpercentIdentity\t");
		writer.write("alignmentLength\tmismatches\t");
		writer.write("gapOpenings\tqueryStart\tqueryEnd\t");
		writer.write("targetStart\ttargetEnd\teValue\tbitScore");
		
		writer.write( endWithNewline ? "\n" : "\t" );
	}
	
	public void writeALine( BufferedWriter writer, boolean endWithNewline) throws Exception
	{
		writer.write(queryId + "\t");
		writer.write(targetId + "\t");
		writer.write(percentIdentity + "\t");
		writer.write(alignmentLength + "\t");
		writer.write(numMismatches + "\t");
		writer.write(gapOpenings + "\t");
		writer.write(queryStart + "\t");
		writer.write(queryEnd + "\t");
		writer.write(targetStart + "\t");
		writer.write(targetEnd + "\t");
		writer.write(eScore + "\t");
		writer.write("" + bitScore);
		writer.write( endWithNewline ? "\n" : "\t" );
	}
	
	public static HashSet<String> getQueryIds(File file, double cutoffEScore, int cutoffSequenceLength) 
		throws Exception
	{
		HashSet<String> queryIds = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String nextLine = reader.readLine();
		
		while( nextLine != null )
		{
			if( ! nextLine.startsWith("#"))
			{
				HitScores hs = new HitScores(nextLine);
				
				if( hs.getEScore() <= cutoffEScore )
					if( Math.abs(hs.getTargetEnd()-hs.getTargetStart()) <= cutoffSequenceLength )
					{
						String id = hs.getQueryId();
						id = id.substring( id.lastIndexOf("|") + 1, id.length() );
						
						queryIds.add(id);
						
						//System.out.println(hs.getQueryId() + " " +  id);
					}
			}
			
			nextLine = reader.readLine();
		}
			
		return queryIds;
	}
	
	public static List<HitScores> getAsList( String filepath) throws Exception
	{
		return getAsList(new File(filepath), false);
	}
	
	@SuppressWarnings("unused")
	public static List<HitScores> getAsList(  File file, boolean gzipped) throws Exception
	{
		System.out.println("PARSING: " + file.getAbsolutePath());
		BufferedReader reader = 
			gzipped ?
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( file) ) ))
				:
					new BufferedReader(new FileReader(file));
		
		List<HitScores> list = new ArrayList<HitScores>();
		
		String nextLine = reader.readLine();
		
		// empty file
		if( nextLine == null )
			return list;
		
		if(nextLine.startsWith("queryId"))
			nextLine = reader.readLine();
		
		while( nextLine != null )
		{
			if( ! nextLine.startsWith("#"))
			{
				list.add(new HitScores(nextLine));
				
			}
			
			nextLine = reader.readLine();
		}
		
		// Bugs in earlier VMs sometimes made this line throw
		// as hard as that is to believe!!
		if( nextLine != null)
			throw new Exception("Parsing error");
		
		return list;
	}
	
	public static HashSet<String> getKeysForCutoff( RunDescription rd, 
			double maxEScore, int minAlignmentLength) throws Exception
	{
		HashMap<String, HitScores> map = getTopHitsFromCacheOrCreate(rd);
		return getKeysForCutoff(map, maxEScore, minAlignmentLength);
	}
	
	public static HashSet<String> getKeysForCutoff(HashMap<String,HitScores> map,
									double maxEScore, int minAlignmentLength) 
	{
		HashSet<String> returnSet = new HashSet<String>();
		
		for( HitScores hs : map.values() )
			if( hs.getEScore() <= maxEScore && hs.getQueryAlignmentLength() >= minAlignmentLength)
				returnSet.add(hs.getQueryId());
		
		return returnSet;
	}

	public static List<HitScores> getAsList(File file, double maxEScore, int minAlignmentLength) 
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		List<HitScores> list = new ArrayList<HitScores>();
		
		String nextLine = reader.readLine();
		
		while( nextLine != null )
		{
			if( ! nextLine.startsWith("#"))
			{
				HitScores hs = new HitScores(nextLine);
				
				if( hs.getEScore() <= maxEScore && hs.getQueryAlignmentLength() >= minAlignmentLength ) 
					list.add(hs);
				
			}
			
			nextLine = reader.readLine();
		}
		
		return list;
	}
	
	public int getNumMismatches()
	{
		return numMismatches;
	}

	public float getPercentIdentity()
	{
		return percentIdentity;
	}

	public double getEScore()
	{
		return eScore;
	}

	public String getTargetId()
	{
		return targetId;
	}
	
	public int compareTo(HitScores other)
	{
		return Double.compare(this.eScore, other.eScore);
	}

	public String getQueryId()
	{
		return queryId;
	}

	public int getQueryEnd()
	{
		return queryEnd;
	}

	public int getQueryStart()
	{
		return queryStart;
	}

	public int getTargetEnd()
	{
		return targetEnd;
	}

	public int getTargetStart()
	{
		return targetStart;
	}

	public int getAlignmentLength()
	{
		return alignmentLength;
	}
	
	public int getQueryAlignmentLength()
	{
		return Math.abs(queryEnd - queryStart);
	}

	public float getBitScore()
	{
		return bitScore;
	}

	public int getGapOpenings()
	{
		return gapOpenings;
	}
	
	public static void main(String[] args)  throws Exception
	{
		
	}
}
