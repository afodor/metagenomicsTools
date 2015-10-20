package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;


public class GenbankAnnotations
{
	private int beginPos;
	private int endPos;
	private List<String> descriptionLines=new ArrayList<String>();
	
	private int xPixelStart;
	private int xPixelEnd;
	private String product = "not named";
	
	public String getProduct() 
	{
		return product;
	}
	
	public int getLowestXPixel()
	{
		return Math.min(xPixelStart, xPixelEnd);
	}

	public void setXPixelStart(int pixelStart)
	{
		xPixelStart = pixelStart;
	}

	public int getHighestXPixel()
	{
		return Math.max(xPixelStart, xPixelEnd);
	}

	public void setXPixelEnd(int pixelEnd)
	{
		xPixelEnd = pixelEnd;
	}

	public int getBeginPos()
	{
		return beginPos;
	}
	
	public int getEndPos()
	{
		return endPos;
	}
	
	public List<String> getDescriptionLines()
	{
		return descriptionLines;
	}
	
	public static float getLowVal(List<GenbankAnnotations> list)
	{
		float lowVal = Float.POSITIVE_INFINITY;
		
		for( GenbankAnnotations ga : list )
		{
			lowVal = Math.min(ga.getBeginPos(), lowVal);
			lowVal = Math.min(ga.getEndPos(), lowVal);
		}
		
		return lowVal;
			
	}
	
	public static float getHighVal(List<GenbankAnnotations> list)
	{
		float highVal = Float.NEGATIVE_INFINITY;
		
		for( GenbankAnnotations ga : list )
		{
			highVal = Math.max(ga.getBeginPos(), highVal);
			highVal = Math.min(ga.getEndPos(), highVal);
		}
		
		return highVal;
			
	}
	
	private static GenbankAnnotations parseGeneLine(String geneLine)
		throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(geneLine);
		
		String firstToken = sToken.nextToken();
		
		if( ! firstToken.equals("gene"))
			throw new Exception("Parsing error " + geneLine);
		
		String stringToParse = sToken.nextToken();
		
		if(stringToParse.startsWith("complement("))
		{
			stringToParse = stringToParse.substring(stringToParse.indexOf('(') +1,
					stringToParse.length()-1);
		}
		
		StringTokenizer sToken2 = new StringTokenizer(stringToParse, ".");
		GenbankAnnotations ga = new GenbankAnnotations();
		ga.beginPos = Integer.parseInt(sToken2.nextToken());
		ga.endPos = Integer.parseInt(sToken2.nextToken());
		
		return ga;
	}
	
	
	public static List<HitScores> getAsList(  File file, 
						boolean gzipped, 
							int minQueryAlignmentLength) 
			throws Exception
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
		
		if(nextLine.startsWith("queryId"))
			nextLine = reader.readLine();
		
		while( nextLine != null )
		{
			if( ! nextLine.startsWith("#"))
			{
				HitScores hs = new HitScores(nextLine);
				
				if( hs.getQueryAlignmentLength() > minQueryAlignmentLength)
					list.add(new HitScores(nextLine));
				
			}
			
			nextLine = reader.readLine();
		}
		
		return list;
	}
	
	public static List<GenbankAnnotations> getGenbankAnnotations(File file)
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader( 
				file));
		
		List<GenbankAnnotations> returnList = new ArrayList<GenbankAnnotations>();
		
		String nextLine = reader.readLine();
		
		while(! nextLine.startsWith("gene "))
		{
			nextLine = reader.readLine().trim();
		}
		
		boolean keepGoing = true;
		
		while(keepGoing)
		{
			// we should start on the gene line
			GenbankAnnotations ga = parseGeneLine(nextLine);
			returnList.add(ga);
			
			nextLine = reader.readLine().trim();
			
			while(! nextLine.startsWith("gene ") && ! nextLine.startsWith("ORIGIN"))
			{
				ga.descriptionLines.add(nextLine);
				
				if( nextLine.startsWith("/product"))
				{
					ga.product = nextLine.substring(10, nextLine.length() -1);
				}
				
				nextLine = reader.readLine().trim();
			}
				
			if( nextLine.startsWith("ORIGIN"))
				keepGoing = false;
		}
		
		reader.close();  
		return returnList;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<GenbankAnnotations> list = getGenbankAnnotations(new File( 
			ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
			"NC_008782_GenbankAnnotations.txt"));
		
		System.out.println(list.size());
	}
	
}
