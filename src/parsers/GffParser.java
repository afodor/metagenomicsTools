package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import jobinLabRNASeq.FindGeneForMappedSequence;

import utils.ConfigReader;

public class GffParser
{
	public static HashMap<String, Range> getAllRanges() throws Exception
	{
		HashMap<String, Range> map = new HashMap<String, GffParser.Range>();
		 
		HashMap<String, GffParser> geneFileMap =  FindGeneForMappedSequence.getGeneFileMap();	
		
		for(String s : geneFileMap.keySet())
		{
			GffParser parser = geneFileMap.get(s);
			
			for( Range r : parser.rangeList)
			{
				if( r.locusTag != null &&  map.containsKey(r.locusTag))
					throw new Exception("Duplicate " + r.locusTag + " " + map.size());
				
				map.put(r.locusTag, r);
			}
		}
		
		return map;
	}
	
	public static class Range
	{
		public int lower;
		public int upper;
		public String locusTag;
	}
	
	private final List<Range> rangeList = new ArrayList<GffParser.Range>();
	private final int regionStart;
	private final int regionEnd;
	
	public List<Range> getRangeList()
	{
		return rangeList;
	}
	
	public int getRegionStart()
	{
		return regionStart;
	}
	
	public int getRegionEnd()
	{
		return regionEnd;
	}
	
	public String getGeneLocusTag(int startPosition)
	{
		for(Range r :  this.rangeList)
			if(startPosition >= r.lower && startPosition <= r.upper)
				return r.locusTag;
		
		return null;
	}
	
	public GffParser(String filePath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		
		String s= reader.readLine();
		
		while( s.startsWith("#"))
			s= reader.readLine();
		
		StringTokenizer sToken = new StringTokenizer(s);
		sToken.nextToken();  sToken.nextToken();
		
		if( ! sToken.nextToken().equals("region"))
			throw new Exception("Wrong line " + s);
		
		this.regionStart = Integer.parseInt(sToken.nextToken());
		
		if( this.regionStart != 1)
			throw new Exception("Expecting a start of 1 " + this.regionStart);
		
		this.regionEnd = Integer.parseInt(sToken.nextToken());
		
		for(	; 
				s != null && ! s.equals("###"); 
					s = reader.readLine())
		{
			//System.out.println(s);
			sToken = new StringTokenizer(s);
			sToken.nextToken();  sToken.nextToken();
			
			if( sToken.nextToken().equals("gene"))
			{
				Range r = new Range();
				this.rangeList.add(r);
				r.lower = Integer.parseInt(sToken.nextToken());
				r.upper = Integer.parseInt(sToken.nextToken());
				sToken.nextToken(); sToken.nextToken(); sToken.nextToken();
				StringTokenizer innerTok = new StringTokenizer(sToken.nextToken(), ";");
				while(innerTok.hasMoreTokens())
				{
					String s2 = innerTok.nextToken();
					
					if( s2.startsWith("locus_tag"))
						r.locusTag= s2.replace("locus_tag=", "");
					
				}	
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		GffParser parser = new GffParser(ConfigReader.getJobinLabRNASeqDir()+
				File.separator +  "AEFA01000009.gff");
		
		for( Range r : parser.rangeList )
			System.out.println(r.lower + " " + r.upper + " " + r.locusTag);
	}
}
