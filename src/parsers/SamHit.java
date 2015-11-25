package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;


public class SamHit
{
	public static final int MISSING_VAL = 99;
	
	private final String querySequence;
	private final int flag;
	private final String targetSequence;
	private final int startPoisiton;
	private final int mapQScore;
	private final String cigarString;
	private int editDistance = MISSING_VAL;
	
	public int getEditDistance()
	{
		return editDistance;
	}



	public void setEditDistance(int editDistance)
	{
		this.editDistance = editDistance;
	}



	public String getQuerySequence()
	{
		return querySequence;
	}



	public int getFlag()
	{
		return flag;
	}



	public String getTargetSequence()
	{
		return targetSequence;
	}



	public int getStartPoisiton()
	{
		return startPoisiton;
	}



	public int getMapQScore()
	{
		return mapQScore;
	}



	public String getCigarString()
	{
		return cigarString;
	}



	public SamHit(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		/*
		System.out.println(s);
		
		for(int x=0; x < splits.length; x++)
			System.out.println( x + " " + splits[x] );
		*/
		
		this.querySequence = splits[0];
		this.flag = Integer.parseInt(splits[1]);
		this.targetSequence = splits[2];
		this.startPoisiton = Integer.parseInt(splits[3]);
		this.mapQScore = splits[4].equals("*") ? MISSING_VAL :  Integer
				.parseInt(splits[4]);
		this.cigarString = splits[5];
		
		for( int x=6; x < splits.length ; x++)
			if(splits[x].startsWith("NM:i:"))
			{
				this.editDistance = Integer.parseInt(splits[x].replace("NM:i:",""));
			}
		
		}
	
	public static void main(String[] args) throws Exception
	{
		writeHitsToFile("c:\\rnaSeq\\10MM19_TAGCTT_L008_R1_001_output.sam.gz", 
				"c:\\rnaSeq\\10MM19_TAGCTT_L008_R1_001_hits.txt");
	}
	
	public static void writeHitsToFile(String zippedFileToParse, String outputFile ) throws Exception
	{
		float numSequences =0;
		float numHits=0;
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFile)));
		writer.write("querySequence\ttargetSequence\tstartPosition\tflag\tmapQScore\tcigarString\teditDistance\n");
		
		BufferedReader reader = 
		new BufferedReader(new InputStreamReader( 
				new GZIPInputStream( new FileInputStream( zippedFileToParse))));
		
		String s= reader.readLine();
		
		while ( s.startsWith("@"))
			s= reader.readLine();
		
		for(s= reader.readLine();
				s != null;
				 s = reader.readLine())
		{
			numSequences++;
			SamHit sh = new SamHit(s);
			
			if( sh.getFlag() != 4 )
			{
				writer.write(sh.getQuerySequence() + "\t" + sh.getTargetSequence() + "\t" + sh.getStartPoisiton() + 
						"\t" + sh.getFlag() + "\t" + sh.mapQScore + "\t" + 
						sh.getCigarString() + "\t" + sh.getEditDistance() + "\n");
				numHits++;
			}	
		}
		
		writer.flush();  writer.close();
		System.out.println( zippedFileToParse + " " +  numHits + " " +numSequences + " " + (numHits/numSequences));
	}
}
